package lk.ac.kln.stu.shopping.payments.mobile.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MobilePayment {

    private float amount;
    private String mobileNumber;

}
