package fr.driv.n.cook.repository.customer.order.entity;

import fr.driv.n.cook.repository.menu.entity.MenuItemEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "customer_order_items")
@Getter
@Setter
public class CustomerOrderItemEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_order_id", nullable = false)
    private CustomerOrderEntity customerOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItemEntity menuItem;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "line_cash_total", precision = 10, scale = 2)
    private BigDecimal lineCashTotal;

    @Column(name = "line_points_total")
    private Integer linePointsTotal;
}

