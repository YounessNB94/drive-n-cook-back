package fr.driv.n.cook.repository.sale.entity;

import fr.driv.n.cook.repository.customer.order.entity.CustomerOrderEntity;
import fr.driv.n.cook.repository.menu.entity.MenuItemEntity;
import fr.driv.n.cook.shared.SaleChannel;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
@Getter
@Setter
public class SaleEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id")
    private MenuItemEntity menuItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_order_id", nullable = false)
    private CustomerOrderEntity customerOrder;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SaleChannel channel;

    @Column(nullable = false)
    private LocalDateTime date;
}

