package db.spring.order.domain.repository;

import db.spring.order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderRepository, Long> {
    Optional<Order> findByOrderCode(String orderCode);
}
