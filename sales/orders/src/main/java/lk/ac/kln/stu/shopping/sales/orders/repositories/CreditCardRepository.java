package lk.ac.kln.stu.shopping.sales.orders.repositories;

import lk.ac.kln.stu.shopping.sales.orders.models.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
}
