package db.spring.common.init;

import db.spring.customer.domain.repository.CustomerRepository;
import db.spring.order.domain.entity.Order;
import db.spring.order.domain.entity.OrderItem;
import db.spring.order.domain.enums.PaymentMethod;
import db.spring.order.domain.enums.Status;
import db.spring.order.domain.repository.OrderItemRepository;
import db.spring.order.domain.repository.OrderRepository;
import db.spring.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private void createOrder(String orderCode, String customerEmail, Instant orderedAt,
                             Status status, PaymentMethod pm) {
        var customer = customerRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new IllegalStateException("Customer not found: " + customerEmail));
        Order order = new Order(orderCode, customer, orderedAt, status, pm);
        orderRepository.save(order);
    }

    private void addItem(String orderCode, String sku, int quantity) {
        var order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new IllegalStateException("Order not found: " + orderCode));
        var product = productRepository.findBySku(sku)
                .orElseThrow(() -> new IllegalStateException("Product not found: " + sku));

        OrderItem item = new OrderItem(order, product, quantity, product.getUnitPrice());
        orderItemRepository.save(item);
    }

    /** "2025-08-01 09:15+09" 같은 SQL 표기와 동일한 KST 시각을 Instant로 */
    private Instant kstInstant(int year, int month, int day, int hour, int minute) {
        ZonedDateTime zdt = ZonedDateTime.of(
                LocalDateTime.of(year, month, day, hour, minute),
                ZoneId.of("Asia/Seoul")
        );
        return zdt.toInstant();
    }
}
