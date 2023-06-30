package lk.ac.kln.stu.shopping.payments.card.controllers;

import lk.ac.kln.stu.shopping.payments.card.models.CreditCardPayment;
import lk.ac.kln.stu.shopping.payments.card.services.CardPaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping({"payments/card", "payments/card/"})
public class CardPaymentsController {

    private final CardPaymentsService cardPaymentsService;

    @Autowired
    public CardPaymentsController(CardPaymentsService cardPaymentsService) {
        this.cardPaymentsService = cardPaymentsService;
    }

    @PostMapping
    public Map<String, String> submitCardPayment(@RequestBody CreditCardPayment creditCardPayment) {
        return this.cardPaymentsService.getDummyPaymentSuccessResponse(creditCardPayment);
    }

}
