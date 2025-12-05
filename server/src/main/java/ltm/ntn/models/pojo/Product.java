package ltm.ntn.models.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Product {
    private String id;
    private String name;
    private String sku;
    private String description;
    private double price;
    private int quantity;
    private int sold;
    private boolean isActive;
}