package lk.ac.kln.stu.shopping.sales.orders.models;

import io.hypersistence.utils.hibernate.type.json.JsonStringType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import java.util.Map;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter
public class SalesOrder {

    @Id
    @SequenceGenerator(name = "sequence_sales_order", sequenceName = "sequence_sales_order", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_sales_order")
    private @Setter(AccessLevel.PROTECTED) Long id;

    private @Setter(AccessLevel.PROTECTED) Long buyerId;

    @OneToOne(cascade = CascadeType.PERSIST)
    private SalesOrderDelivery deliveryRecord;

    @OneToOne(cascade = CascadeType.PERSIST)
    private PaymentRecord paymentRecord;

    @Type(JsonStringType.class)
    private Map<String, Integer> salesItems;

    @OneToOne(cascade = CascadeType.PERSIST)
    private SalesOrderCommunication salesOrderCommunication;

    public SalesOrder(Long buyerId) {
        this.buyerId = buyerId;
    }
}
