package fr.driv.n.cook.repository.franchisee.entity;

import fr.driv.n.cook.repository.customer.order.entity.CustomerOrderEntity;
import fr.driv.n.cook.repository.franchise.application.entity.FranchiseApplicationEntity;
import fr.driv.n.cook.repository.truck.entity.TruckEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "franchisees")
@Getter
@Setter
public class FranchiseeEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(length = 30)
    private String phone;

    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;

    @Column(nullable = false, length = 255)
    private String address;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "franchisee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FranchiseApplicationEntity> applications = new ArrayList<>();

    @OneToMany(mappedBy = "franchisee", fetch = FetchType.LAZY)
    private List<CustomerOrderEntity> orders = new ArrayList<>();

    @OneToMany(mappedBy = "franchisee", fetch = FetchType.LAZY)
    private List<TruckEntity> trucks = new ArrayList<>();
}
