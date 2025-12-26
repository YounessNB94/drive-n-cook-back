package fr.driv.n.cook.repository.menu.entity;

import fr.driv.n.cook.repository.franchisee.entity.FranchiseeEntity;
import fr.driv.n.cook.shared.MenuStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menus")
@Getter
@Setter
public class MenuEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MenuStatus status;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchisee_id", nullable = false)
    private FranchiseeEntity franchisee;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MenuItemEntity> items = new ArrayList<>();
}

