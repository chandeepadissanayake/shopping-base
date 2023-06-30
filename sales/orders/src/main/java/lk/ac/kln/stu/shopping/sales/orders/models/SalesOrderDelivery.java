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
public class SalesOrderDelivery {

    @Id
    @SequenceGenerator(name = "sequence_sales_order_delivery", sequenceName = "sequence_sales_order_delivery", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_sales_order_delivery")
    private @Setter(AccessLevel.PROTECTED) Long id;

    private String deliveryMode;
    // The following parameter is used to determine a record w.r.t the exact delivery service that is used.
    // For instance, this should identify the delivery person involved and further details.
    private String deliveryServiceId;

    public SalesOrderDelivery(String deliveryMode, String deliveryServiceId) {
        this.deliveryMode = deliveryMode;
        this.deliveryServiceId = deliveryServiceId;
    }
}
