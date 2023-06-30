package lk.ac.kln.stu.shopping.sales.orders.controllers;

import lk.ac.kln.stu.shopping.sales.orders.services.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "sales/orders/{orderId}/items")
public class SalesOrderItemController {

    private final SalesOrderService salesOrderService;

    @Autowired
    public SalesOrderItemController(SalesOrderService salesOrderService) {
        this.salesOrderService = salesOrderService;
    }

    @PutMapping(path = "/{itemId}/amount={amount}")
    public ResponseEntity<Void> add(@PathVariable Long orderId, @PathVariable Long itemId, @PathVariable int amount) {
        try {
            this.salesOrderService.addItemsToSalesOrder(orderId, itemId, amount);

            return ResponseEntity.ok().build();
        }

        catch (IllegalStateException illegalStateException) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/{itemId}")
    public ResponseEntity<String> remove(@PathVariable Long orderId, @PathVariable Long itemId) {
        try {
            this.salesOrderService.removeItemFromSalesOrder(orderId, itemId);

            return ResponseEntity.ok().build();
        }

        catch (IllegalStateException illegalStateException) {
            return new ResponseEntity<>(illegalStateException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
