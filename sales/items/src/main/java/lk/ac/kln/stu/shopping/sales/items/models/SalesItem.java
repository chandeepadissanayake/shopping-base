package lk.ac.kln.stu.shopping.sales.items.models;

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
public class SalesItem {

    @Id
    @SequenceGenerator(name = "sequence_sales_item", sequenceName = "sequence_sales_item", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_sales_item")
    private @Setter(AccessLevel.PROTECTED) Long id;

    private String name;
    private String description;
    private float unitPrice;

    public SalesItem(String name, String description, float unitPrice) {
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
    }

    public SalesItem(Long id, String name, String description, float unitPrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
    }

}
