package lk.ac.kln.stu.shopping.communication.sms.controllers;

import lk.ac.kln.stu.shopping.communication.sms.models.SMSMessage;
import lk.ac.kln.stu.shopping.communication.sms.service.SMSSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping({"communication/sms", "communication/sms/"})
public class SMSSenderController {

    private final SMSSenderService smsSenderService;

    @Autowired
    public SMSSenderController(SMSSenderService smsSenderService) {
        this.smsSenderService = smsSenderService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> sendSMS(@RequestBody SMSMessage smsMessage) {
        String smsRef = this.smsSenderService.sendSMSDummy(smsMessage.getTo(), smsMessage.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("smsId", smsRef);

        return ResponseEntity.ok(response);
    }

}
