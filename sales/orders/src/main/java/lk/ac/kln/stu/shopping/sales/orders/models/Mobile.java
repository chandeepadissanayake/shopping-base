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
public class Mobile {

    @Id
    @SequenceGenerator(name = "sequence_sales_order_mobile_payment", sequenceName = "sequence_sales_order_mobile_payment", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_sales_order_mobile_payment")
    private @Setter(AccessLevel.PROTECTED) Long id;

    private String mobileNumber;

}
