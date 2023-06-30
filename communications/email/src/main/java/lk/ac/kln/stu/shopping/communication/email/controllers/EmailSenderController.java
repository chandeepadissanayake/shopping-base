package lk.ac.kln.stu.shopping.communication.email.controllers;

import lk.ac.kln.stu.shopping.communication.email.models.Email;
import lk.ac.kln.stu.shopping.communication.email.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping({"communication/email", "communication/email/"})
public class EmailSenderController {

    private final EmailSenderService emailSenderService;

    @Autowired
    public EmailSenderController(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> sendEmail(@RequestBody Email email) {
        String emailId = this.emailSenderService.sendEmailDummy(email.getTo(), email.getSubject(), email.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("emailId", emailId);

        return ResponseEntity.ok(response);
    }
}
