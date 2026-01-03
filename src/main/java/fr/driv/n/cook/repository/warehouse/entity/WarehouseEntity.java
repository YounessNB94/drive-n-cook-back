package fr.driv.n.cook.repository.warehouse.entity;

import fr.driv.n.cook.repository.supply.order.entity.SupplyOrderEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "warehouses")
@Getter
@Setter
public class WarehouseEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(length = 30)
    private String phone;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<InventoryItemEntity> inventoryItems = new ArrayList<>();

    @OneToMany(mappedBy = "pickupWarehouse", fetch = FetchType.LAZY)
    private List<SupplyOrderEntity> pickUpOrders = new ArrayList<>();
}
