package lk.ac.kln.stu.shopping.sales.orders.services;

import jakarta.transaction.Transactional;
import lk.ac.kln.stu.shopping.sales.orders.models.*;
import lk.ac.kln.stu.shopping.sales.orders.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderDeliveryRepository salesOrderDeliveryRepository;
    private final PaymentRecordRepository paymentRecordRepository;
    private final CreditCardRepository creditCardRepository;
    private final MobilePaymentRepository mobilePaymentRepository;

    private final SalesItemRemoteService salesItemRemoteService;

    private final DeliveryRemoteService deliveryRemoteService;

    private final CardPaymentRemoteService cardPaymentRemoteService;

    @Autowired
    public SalesOrderService(SalesOrderRepository salesOrderRepository, SalesOrderDeliveryRepository salesOrderDeliveryRepository, PaymentRecordRepository paymentRecordRepository, CreditCardRepository creditCardRepository, MobilePaymentRepository mobilePaymentRepository, SalesItemRemoteService salesItemRemoteService, WebClient webClient, DeliveryRemoteService deliveryRemoteService, CardPaymentRemoteService cardPaymentRemoteService) {
        this.salesOrderRepository = salesOrderRepository;
        this.salesOrderDeliveryRepository = salesOrderDeliveryRepository;
        this.paymentRecordRepository = paymentRecordRepository;
        this.creditCardRepository = creditCardRepository;
        this.mobilePaymentRepository = mobilePaymentRepository;
        this.salesItemRemoteService = salesItemRemoteService;
        this.deliveryRemoteService = deliveryRemoteService;
        this.cardPaymentRemoteService = cardPaymentRemoteService;
    }

    public List<SalesOrder> getAllSalesOrders() {
        return this.salesOrderRepository.findAll();
    }

    public Optional<SalesOrder> getSalesOrder(Long id) {
        return this.salesOrderRepository.findById(id);
    }

    public Long createSalesOrder(String buyerId) {
        SalesOrder salesOrder = new SalesOrder(buyerId);
        this.salesOrderRepository.save(salesOrder);

        return salesOrder.getId();
    }

    public void addItemsToSalesOrder(Long orderId, Long salesItemId, int amount) {
        Optional<SalesOrder> salesOrderRecord = this.salesOrderRepository.findById(orderId);

        if (salesOrderRecord.isPresent()) {
            SalesOrder salesOrder = salesOrderRecord.get();

            Optional<SalesItem> salesItemRecord = this.salesItemRemoteService.getSalesItemById(salesItemId);
            if (salesItemRecord.isPresent()) {
                Map<String, Integer> salesItems = salesOrder.getSalesItems();
                if (salesItems == null) {
                    salesItems = new HashMap<>();
                }
                salesItems.put(String.valueOf(salesItemId), amount);
                salesOrder.setSalesItems(salesItems);

                this.salesOrderRepository.save(salesOrder);
            }
            else {
                throw new IllegalStateException("No such sales item found!");
            }
        }
        else {
            throw new IllegalStateException("No such order found!");
        }
    }

    public void removeItemFromSalesOrder(Long orderId, Long salesItemId) {
        Optional<SalesOrder> salesOrderRecord = this.salesOrderRepository.findById(orderId);

        if (salesOrderRecord.isPresent()) {
            SalesOrder salesOrder = salesOrderRecord.get();
            Map<String, Integer> salesItems = salesOrder.getSalesItems();

            if (salesItems != null && salesItems.containsKey(String.valueOf(salesItemId))) {
                salesItems.remove(String.valueOf(salesItemId));
                salesOrder.setSalesItems(salesItems);
                this.salesOrderRepository.save(salesOrder);
            }
            else {
                throw new IllegalStateException("The sales item do not exist in the items list for this order.");
            }
        }
        else {
            throw new IllegalStateException("No such order found!");
        }
    }

    @Transactional
    public void setSalesOrderDeliveryMethod(Long orderId, SalesOrderDelivery salesOrderDelivery) {
        Optional<SalesOrder> salesOrderRecord = this.salesOrderRepository.findById(orderId);

        if (salesOrderRecord.isPresent()) {
            // Request Delivery Services from the Remote Service
            Optional<Map<String, String>> deliveryServiceResponseRecord = this.deliveryRemoteService.requestDelivery(salesOrderDelivery.getDeliveryMode());
            if (deliveryServiceResponseRecord.isPresent()) {
                Map<String, String> deliveryServiceResponse = deliveryServiceResponseRecord.get();
                salesOrderDelivery.setDeliveryServiceId(deliveryServiceResponse.get("serviceId"));

                SalesOrder salesOrder = salesOrderRecord.get();
                this.salesOrderDeliveryRepository.save(salesOrderDelivery);

                // Get current delivery records if any and deletes them
                SalesOrderDelivery currentSalesOrderDelivery = salesOrder.getDeliveryRecord();
                if (currentSalesOrderDelivery != null) {
                    this.salesOrderDeliveryRepository.delete(currentSalesOrderDelivery);
                }

                salesOrder.setDeliveryRecord(salesOrderDelivery);
                this.salesOrderRepository.save(salesOrder);
            }
            else {
                throw new IllegalStateException("Could not request delivery services from the third-party service.");
            }
        }
        else {
            throw new IllegalStateException("No such order found!");
        }
    }

    public void setSalesOrderPaymentRecord(Long orderId, PaymentRecord salesOrderPaymentRecord) {
        Optional<SalesOrder> salesOrderRecord = this.salesOrderRepository.findById(orderId);

        if (salesOrderRecord.isPresent()) {
            if (salesOrderPaymentRecord.getPaymentMethod() == PaymentMethod.CREDIT_CARD) {
                // Make the Payment
                Optional<Map<String, String>> paymentResponseRecord = this.cardPaymentRemoteService.submitPayment(salesOrderPaymentRecord.getAmount(), salesOrderPaymentRecord.getCreditCard());
                if (paymentResponseRecord.isEmpty()) {
                    // Never going to happen since we use a dummy service.
                    throw new IllegalStateException("Payment Failed!");
                }
                else {
                    Map<String, String> paymentResponse = paymentResponseRecord.get();
                    salesOrderPaymentRecord.setPaymentReference(paymentResponse.get("paymentId"));
                }

                this.creditCardRepository.save(salesOrderPaymentRecord.getCreditCard());
            }
            else if (salesOrderPaymentRecord.getPaymentMethod() == PaymentMethod.MOBILE) {
                this.mobilePaymentRepository.save(salesOrderPaymentRecord.getMobilePayment());
            }
            this.paymentRecordRepository.save(salesOrderPaymentRecord);

            SalesOrder salesOrder = salesOrderRecord.get();
            PaymentRecord oldSalesOrderPaymentRecord = salesOrder.getPaymentRecord();
            salesOrder.setPaymentRecord(salesOrderPaymentRecord);
            this.salesOrderRepository.save(salesOrder);

            if (oldSalesOrderPaymentRecord != null) {
                this.paymentRecordRepository.delete(oldSalesOrderPaymentRecord);

                if (oldSalesOrderPaymentRecord.getPaymentMethod() == PaymentMethod.CREDIT_CARD) {
                    CreditCard oldCreditCard = oldSalesOrderPaymentRecord.getCreditCard();
                    this.creditCardRepository.delete(oldCreditCard);
                }
                else if (oldSalesOrderPaymentRecord.getPaymentMethod() == PaymentMethod.MOBILE) {
                    MobilePayment oldMobile = oldSalesOrderPaymentRecord.getMobilePayment();
                    this.mobilePaymentRepository.delete(oldMobile);
                }
            }
        }
        else {
            throw new IllegalStateException("No such order found!");
        }
    }

}
