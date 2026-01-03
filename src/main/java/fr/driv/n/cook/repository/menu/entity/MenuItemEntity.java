package fr.driv.n.cook.repository.menu.entity;

import fr.driv.n.cook.repository.customer.order.entity.CustomerOrderItemEntity;
import fr.driv.n.cook.repository.sale.entity.SaleEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu_items")
@Getter
@Setter
public class MenuItemEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuEntity menu;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "price_cash", precision = 10, scale = 2, nullable = false)
    private BigDecimal priceCash;

    @Column(name = "points_price")
    private Integer pointsPrice;

    @Column(nullable = false)
    private boolean available;

    @OneToMany(mappedBy = "menuItem", fetch = FetchType.LAZY)
    private List<SaleEntity> sales = new ArrayList<>();

    @OneToMany(mappedBy = "menuItem", fetch = FetchType.LAZY)
    private List<CustomerOrderItemEntity> orderItems = new ArrayList<>();
}

