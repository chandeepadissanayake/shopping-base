package lk.ac.kln.stu.shopping.sales.orders.repositories;

import lk.ac.kln.stu.shopping.sales.orders.models.SalesOrder;
import lk.ac.kln.stu.shopping.sales.orders.models.SalesOrderDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalesOrderDeliveryRepository extends JpaRepository<SalesOrderDelivery, Long> {

}
