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
public class SalesOrderCommunication {

    @Id
    @SequenceGenerator(name = "sequence_sales_order_communication", sequenceName = "sequence_sales_order_communication", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_sales_order_communication")
    private @Setter(AccessLevel.PROTECTED) Long id;

    @Column(nullable = true)
    private String smsRef;

    @Column(nullable = true)
    private String emailRef;

}
