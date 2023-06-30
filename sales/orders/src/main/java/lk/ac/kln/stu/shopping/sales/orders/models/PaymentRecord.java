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
public class PaymentRecord {

    @Id
    @SequenceGenerator(name = "sequence_sales_order_payment", sequenceName = "sequence_sales_order_payment", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_sales_order_payment")
    private @Setter(AccessLevel.PROTECTED) Long id;

    private float amount;

    @Enumerated(EnumType.ORDINAL)
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(nullable = true)
    private CreditCard creditCard;

    @ManyToOne
    @JoinColumn(nullable = true)
    private MobilePayment mobilePayment;

}
