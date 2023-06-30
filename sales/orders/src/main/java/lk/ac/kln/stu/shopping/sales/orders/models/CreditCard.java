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
public class CreditCard {

    @Id
    @SequenceGenerator(name = "sequence_sales_order_credit_card", sequenceName = "sequence_sales_order_credit_card", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_sales_order_credit_card")
    private @Setter(AccessLevel.PROTECTED) Long id;

    private String cardNumber;
    private int cvcNumber;
    private String cardHolderName;

}
