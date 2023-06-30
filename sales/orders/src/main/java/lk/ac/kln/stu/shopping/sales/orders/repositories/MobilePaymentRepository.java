package lk.ac.kln.stu.shopping.sales.orders.repositories;

import lk.ac.kln.stu.shopping.sales.orders.models.MobilePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MobilePaymentRepository extends JpaRepository<MobilePayment, Long> {
}
