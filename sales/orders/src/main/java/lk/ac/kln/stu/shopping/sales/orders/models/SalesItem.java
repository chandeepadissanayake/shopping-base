package lk.ac.kln.stu.shopping.sales.orders.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SalesItem {

    private Long id;
    private String name;
    private String description;
    private float unitPrice;

    public SalesItem(Long id, String name, String description, float unitPrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
    }

}
