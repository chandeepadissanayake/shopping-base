package lk.ac.kln.stu.shopping.sales.items.repositories;

import lk.ac.kln.stu.shopping.sales.items.models.SalesItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalesItemRepository extends JpaRepository<SalesItem, Long> {

    Optional<SalesItem> findSalesItemByName(String name);

}
