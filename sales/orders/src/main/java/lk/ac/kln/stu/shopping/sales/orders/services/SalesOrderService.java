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

    private final SalesOrderCommunicationRepository salesOrderCommunicationRepository;

    private final UserRemoteService userRemoteService;

    private final SalesItemRemoteService salesItemRemoteService;

    private final DeliveryRemoteService deliveryRemoteService;

    private final CardPaymentRemoteService cardPaymentRemoteService;

    private final MobilePaymentRemoteService mobilePaymentRemoteService;

    private final SMSSenderRemoteService smsSenderRemoteService;

    private final EmailSenderRemoteService emailSenderRemoteService;

    @Autowired
    public SalesOrderService(SalesOrderRepository salesOrderRepository, SalesOrderDeliveryRepository salesOrderDeliveryRepository, PaymentRecordRepository paymentRecordRepository, CreditCardRepository creditCardRepository, MobilePaymentRepository mobilePaymentRepository, SalesOrderCommunicationRepository salesOrderCommunicationRepository, UserRemoteService userRemoteService, SalesItemRemoteService salesItemRemoteService, WebClient webClient, DeliveryRemoteService deliveryRemoteService, CardPaymentRemoteService cardPaymentRemoteService, MobilePaymentRemoteService mobilePaymentRemoteService, SMSSenderRemoteService smsSenderRemoteService, EmailSenderRemoteService emailSenderRemoteService) {
        this.salesOrderRepository = salesOrderRepository;
        this.salesOrderDeliveryRepository = salesOrderDeliveryRepository;
        this.paymentRecordRepository = paymentRecordRepository;
        this.creditCardRepository = creditCardRepository;
        this.mobilePaymentRepository = mobilePaymentRepository;
        this.salesOrderCommunicationRepository = salesOrderCommunicationRepository;
        this.userRemoteService = userRemoteService;
        this.salesItemRemoteService = salesItemRemoteService;
        this.deliveryRemoteService = deliveryRemoteService;
        this.cardPaymentRemoteService = cardPaymentRemoteService;
        this.mobilePaymentRemoteService = mobilePaymentRemoteService;
        this.smsSenderRemoteService = smsSenderRemoteService;
        this.emailSenderRemoteService = emailSenderRemoteService;
    }

    public List<SalesOrder> getAllSalesOrders() {
        return this.salesOrderRepository.findAll();
    }

    public Optional<SalesOrder> getSalesOrder(Long id) {
        return this.salesOrderRepository.findById(id);
    }

    public Long createSalesOrder(String token) {
        Map<String, String> userRecord = this.userRemoteService.getUser(token).get(); // Guaranteed to be not null.

        // Create Communications record, since by default it should be there
        SalesOrderCommunication salesOrderCommunication = new SalesOrderCommunication();
        this.salesOrderCommunicationRepository.save(salesOrderCommunication);

        SalesOrder salesOrder = new SalesOrder(Long.parseLong(userRecord.get("id")));
        salesOrder.setSalesOrderCommunication(salesOrderCommunication);
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
                this.creditCardRepository.save(salesOrderPaymentRecord.getCreditCard());
            }
            else if (salesOrderPaymentRecord.getPaymentMethod() == PaymentMethod.MOBILE) {
                this.mobilePaymentRepository.save(salesOrderPaymentRecord.getMobile());
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
                    Mobile oldMobile = oldSalesOrderPaymentRecord.getMobile();
                    this.mobilePaymentRepository.delete(oldMobile);
                }
            }
        }
        else {
            throw new IllegalStateException("No such order found!");
        }
    }

    public void payForOrder(String token, Long orderId) {
        Optional<SalesOrder> salesOrderRecord = this.salesOrderRepository.findById(orderId);

        if (salesOrderRecord.isPresent()) {
            SalesOrder salesOrder = salesOrderRecord.get();
            PaymentRecord salesOrderPaymentRecord = salesOrder.getPaymentRecord();

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
                    this.paymentRecordRepository.save(salesOrderPaymentRecord);
                }
            }
            else if (salesOrderPaymentRecord.getPaymentMethod() == PaymentMethod.MOBILE) {
                // Make the payment
                Optional<Map<String, String>> paymentResponseRecord = this.mobilePaymentRemoteService.submitPayment(salesOrderPaymentRecord.getAmount(), salesOrderPaymentRecord.getMobile(), salesOrderPaymentRecord.getMobile().getPin());
                if (paymentResponseRecord.isEmpty()) {
                    // Never going to happen since we use a dummy service.
                    throw new IllegalStateException("Payment Failed!");
                }
                else {
                    Map<String, String> paymentResponse = paymentResponseRecord.get();
                    salesOrderPaymentRecord.setPaymentReference(paymentResponse.get("paymentId"));
                    this.paymentRecordRepository.save(salesOrderPaymentRecord);
                }
            }
            else {
                throw new IllegalStateException("Payment method not found for the order!");
            }

            SalesOrderCommunication salesOrderComm = salesOrder.getSalesOrderCommunication();
            if (salesOrderComm == null) {
                salesOrderComm = new SalesOrderCommunication();

                this.salesOrderCommunicationRepository.save(salesOrderComm);

                salesOrder.setSalesOrderCommunication(salesOrderComm);
                this.salesOrderRepository.save(salesOrder);
            }

            // Fetch user details
            Map<String, String > userRecord = this.userRemoteService.getUser(token).get();

            // Reaching here means the payment has been successful.
            // Send the SMS
            // TODO: Remove the following temporary variable with the buyer's mobile number when auth is avail.
            String smsTo = userRecord.get("mobile");
            String smsMessage = "Payment for your order #" + String.valueOf(salesOrder.getId()) + " has been completed!";
            Optional<Map<String, String>> smsResponseRecord = this.smsSenderRemoteService.sendSMS(smsTo, smsMessage);
            if (smsResponseRecord.isPresent()) {
                Map<String, String> smsResponse = smsResponseRecord.get();
                SalesOrderCommunication salesOrderCommunication = salesOrder.getSalesOrderCommunication();
                salesOrderCommunication.setSmsRef(smsResponse.get("smsId"));
                this.salesOrderCommunicationRepository.save(salesOrderCommunication);
            }
            else {
                System.out.println("SMS Sending to " + smsTo + " has been unsuccessful.");
            }

            // Send the Email
            String emailTo = userRecord.get("email");
            String emailSubject = "Payment Success Notification for the Order #" + String.valueOf(salesOrder.getId());
            String emailMessage = "Payment for your order #" + String.valueOf(salesOrder.getId()) + " has been completed!";
            Optional<Map<String, String>> emailResponseRecord = this.emailSenderRemoteService.sendEmail(emailTo, emailSubject, emailMessage);
            if (emailResponseRecord.isPresent()) {
                Map<String, String> emailResponse = emailResponseRecord.get();
                SalesOrderCommunication salesOrderCommunication = salesOrder.getSalesOrderCommunication();
                salesOrderCommunication.setEmailRef(emailResponse.get("emailId"));
                this.salesOrderCommunicationRepository.save(salesOrderCommunication);
            }
            else {
                System.out.println("Email Sending to " + emailTo + " has been unsuccessful.");
            }
        }
        else {
            throw new IllegalStateException("No such order found!");
        }
    }

}
