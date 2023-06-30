package lk.ac.kln.stu.shopping.sales.orders.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter
public class MobilePayment {

    @Id
    @SequenceGenerator(name = "sequence_sales_order_mobile_payment", sequenceName = "sequence_sales_order_mobile_payment", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_sales_order_mobile_payment")
    private @Setter(AccessLevel.PROTECTED) Long id;

    private String mobileNumber;

    // Following attribute refers to the payment reference that could be received from the mobile service provider
    // for each payment
    private String mobileSubscriptionRef;

}
