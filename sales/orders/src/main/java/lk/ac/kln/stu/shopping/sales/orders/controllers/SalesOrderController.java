package lk.ac.kln.stu.shopping.sales.orders.controllers;

import lk.ac.kln.stu.shopping.sales.orders.models.PaymentRecord;
import lk.ac.kln.stu.shopping.sales.orders.models.SalesOrder;
import lk.ac.kln.stu.shopping.sales.orders.models.SalesOrderDelivery;
import lk.ac.kln.stu.shopping.sales.orders.services.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping({"sales/orders", "sales/orders/"})
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @Autowired
    public SalesOrderController(SalesOrderService salesOrderService) {
        this.salesOrderService = salesOrderService;
    }

    @GetMapping
    public List<SalesOrder> index() {
        return this.salesOrderService.getAllSalesOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesOrder> getOrder(@PathVariable Long id) {
        Optional<SalesOrder> salesOrderRecord = this.salesOrderService.getSalesOrder(id);
        return salesOrderRecord.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/{buyerId}")
    public ResponseEntity<Map<String, Long>> createSalesOrder(@PathVariable String buyerId) {
        Long salesOrderId = this.salesOrderService.createSalesOrder(buyerId);

        Map<String, Long> response = new HashMap<>();
        response.put("sales_order_id", salesOrderId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{orderId}/delivery")
    public void setDeliveryMethod(@PathVariable Long orderId, @RequestBody SalesOrderDelivery salesOrderDelivery) {
        this.salesOrderService.setSalesOrderDeliveryMethod(orderId, salesOrderDelivery);
    }

    @PutMapping("/{orderId}/payment")
    public void setPaymentRecord(@PathVariable Long orderId, @RequestBody PaymentRecord salesOrderPaymentRecord) {
        this.salesOrderService.setSalesOrderPaymentRecord(orderId, salesOrderPaymentRecord);
    }

    @PostMapping("/{orderId}/payment")
    public void makePayment(@PathVariable Long orderId) {
        this.salesOrderService.payForOrder(orderId);
    }

}
