package lk.ac.kln.stu.shopping.payments.mobile.controllers;

import lk.ac.kln.stu.shopping.payments.mobile.models.MobilePayment;
import lk.ac.kln.stu.shopping.payments.mobile.services.MobilePaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping({"payments/mobile", "payments/mobile/"})
public class MobilePaymentsController {

    private final MobilePaymentsService mobilePaymentsService;

    @Autowired
    public MobilePaymentsController(MobilePaymentsService mobilePaymentsService) {
        this.mobilePaymentsService = mobilePaymentsService;
    }

    @PostMapping
    public Map<String, String> submitMobilePayment(@RequestBody MobilePayment mobilePayment) {
        return this.mobilePaymentsService.getDummyPaymentSuccessResponse(mobilePayment);
    }

}
