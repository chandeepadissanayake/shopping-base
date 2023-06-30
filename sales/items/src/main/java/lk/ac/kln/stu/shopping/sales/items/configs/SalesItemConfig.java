package lk.ac.kln.stu.shopping.sales.items.configs;

import lk.ac.kln.stu.shopping.sales.items.models.SalesItem;
import lk.ac.kln.stu.shopping.sales.items.repositories.SalesItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SalesItemConfig {

    @Bean
    CommandLineRunner commandLineRunner(SalesItemRepository salesItemRepository) {
        return args -> {
            if (salesItemRepository.findAll().isEmpty()) {
                List<SalesItem> initialSalesItems = List.of(
                        new SalesItem("Apple", "Fresh and juicy apple", 1.99f),
                        new SalesItem("Banana", "Ripe and nutritious banana", 0.99f),
                        new SalesItem("Bread", "Soft and freshly baked bread", 2.49f),
                        new SalesItem("Carrot", "Crunchy and nutritious carrot", 0.79f),
                        new SalesItem("Cheese", "Smooth and flavorful cheese", 3.99f),
                        new SalesItem("Chicken", "Tender and protein-rich chicken", 5.99f),
                        new SalesItem("Chocolate", "Rich and indulgent chocolate bar", 1.49f),
                        new SalesItem("Coffee", "Aromatic and energizing coffee beans", 6.99f),
                        new SalesItem("Eggs", "Farm-fresh eggs from free-range hens", 2.99f),
                        new SalesItem("Fish", "Fresh and flaky fish fillet", 7.99f),
                        new SalesItem("Flour", "High-quality all-purpose flour", 2.29f),
                        new SalesItem("Garlic", "Flavorful and aromatic garlic cloves", 0.49f),
                        new SalesItem("Grapes", "Sweet and juicy grapes", 3.49f),
                        new SalesItem("Honey", "Pure and natural honey", 4.99f),
                        new SalesItem("Ice Cream", "Creamy and delicious ice cream", 3.99f),
                        new SalesItem("Lettuce", "Crisp and refreshing lettuce", 1.29f),
                        new SalesItem("Milk", "Fresh and nutritious milk", 2.99f),
                        new SalesItem("Onion", "Versatile and essential cooking onion", 0.89f),
                        new SalesItem("Orange", "Vitamin C-rich juicy orange", 1.49f),
                        new SalesItem("Pasta", "Al dente pasta for delicious meals", 1.79f),
                        new SalesItem("Peanut Butter", "Smooth and creamy peanut butter", 3.49f),
                        new SalesItem("Potato", "Versatile and starchy potato", 0.69f),
                        new SalesItem("Rice", "Nutritious and fluffy rice grains", 2.99f),
                        new SalesItem("Salad Dressing", "Tasty and flavorful salad dressing", 2.99f),
                        new SalesItem("Salt", "Essential ingredient for seasoning", 0.99f),
                        new SalesItem("Soda", "Refreshing carbonated soda", 1.99f),
                        new SalesItem("Spinach", "Nutrient-packed leafy green spinach", 1.99f),
                        new SalesItem("Strawberries", "Plump and sweet strawberries", 3.99f),
                        new SalesItem("Sugar", "Granulated sugar for baking and sweetening", 1.49f),
                        new SalesItem("Tea", "Aromatic and soothing tea leaves", 2.49f),
                        new SalesItem("Tomato", "Juicy and versatile tomato", 0.79f),
                        new SalesItem("Tuna", "Flavorful and protein-rich canned tuna", 1.99f),
                        new SalesItem("Water", "Refreshing and hydrating bottled water", 0.99f),
                        new SalesItem("Yogurt", "Creamy and probiotic-rich yogurt", 1.79f),
                        new SalesItem("Avocado", "Creamy and nutritious avocado", 1.99f),
                        new SalesItem("Beef", "Tender and succulent beef cuts", 8.99f),
                        new SalesItem("Butter", "Creamy and smooth butter", 2.99f),
                        new SalesItem("Cereal", "Crunchy and nutritious breakfast cereal", 3.49f),
                        new SalesItem("Chips", "Crispy and flavorful potato chips", 2.49f),
                        new SalesItem("Cucumber", "Cool and refreshing cucumber", 0.69f),
                        new SalesItem("Eggplant", "Versatile and nutritious eggplant", 1.29f),
                        new SalesItem("Ginger", "Flavorful and aromatic ginger root", 0.99f),
                        new SalesItem("Hamburger Buns", "Soft and fluffy hamburger buns", 2.49f),
                        new SalesItem("Honeydew Melon", "Sweet and juicy honeydew melon", 3.99f),
                        new SalesItem("Lemon", "Tangy and versatile lemon", 0.79f),
                        new SalesItem("Mayonnaise", "Creamy and savory mayonnaise", 2.99f),
                        new SalesItem("Olive Oil", "High-quality and flavorful olive oil", 4.99f),
                        new SalesItem("Pineapple", "Sweet and tropical pineapple", 2.99f),
                        new SalesItem("Popcorn", "Light and crunchy popcorn", 1.99f),
                        new SalesItem("Sausages", "Savory and delicious sausages", 4.49f)
                );

                salesItemRepository.saveAll(initialSalesItems);
            }
        };
    }

}
