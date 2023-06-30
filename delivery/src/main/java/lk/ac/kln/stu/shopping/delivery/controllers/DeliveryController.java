package lk.ac.kln.stu.shopping.delivery.controllers;

import lk.ac.kln.stu.shopping.delivery.services.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping({"delivery", "delivery/"})
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> requestDelivery(@RequestBody String deliveryMode) {
        // Make a dummy call to the service.
        String deliverServiceId = this.deliveryService.getDummyDeliveryServiceId();

        Map<String, String> response = new HashMap<>();
        response.put("serviceId", deliverServiceId);

        return ResponseEntity.ok(response);
    }
}
