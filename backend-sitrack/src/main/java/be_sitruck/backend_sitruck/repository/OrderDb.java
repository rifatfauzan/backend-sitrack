package be_sitruck.backend_sitruck.repository;

import be_sitruck.backend_sitruck.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderDb extends JpaRepository<Order, String> {
    Order findByOrderId(String orderId);

    @Query(value = "SELECT o.order_id FROM order_table o ORDER BY o.order_id DESC LIMIT 1", nativeQuery = true)
    String findTopByOrderIdOrderByOrderIdDesc();

    Order findTopByOrderIdStartingWithOrderByOrderIdDesc(String prefix);
    List<Order> findByOrderStatus(int orderStatus);
    List<Order> findByOrderDateBetween(Date fromDate, Date endDate);
}