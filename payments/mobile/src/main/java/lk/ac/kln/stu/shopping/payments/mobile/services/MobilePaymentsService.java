package lk.ac.kln.stu.shopping.payments.mobile.services;

import lk.ac.kln.stu.shopping.payments.mobile.models.MobilePayment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MobilePaymentsService {

    public Map<String, String> getDummyPaymentSuccessResponse(MobilePayment mobilePayment) {
        return new HashMap<>() {{
            put("paymentId", "66453-mmbw-122");
            put("message", "Payment success!");
        }};
    }

}
