package lk.ac.kln.stu.shopping.payments.card.services;

import lk.ac.kln.stu.shopping.payments.card.models.CreditCardPayment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CardPaymentsService {

    public Map<String, String> getDummyPaymentSuccessResponse(CreditCardPayment creditCardPayment) {
        return new HashMap<>() {{
            put("paymentId", "232123");
            put("message", "Payment success!");
        }};
    }

}
