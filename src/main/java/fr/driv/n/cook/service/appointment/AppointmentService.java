package fr.driv.n.cook.service.appointment;

import fr.driv.n.cook.presentation.appointment.dto.Appointment;
import fr.driv.n.cook.presentation.appointment.dto.AppointmentPatch;
import fr.driv.n.cook.repository.appointment.AppointmentRepository;
import fr.driv.n.cook.repository.appointment.entity.AppointmentEntity;
import fr.driv.n.cook.repository.franchisee.FranchiseeRepository;
import fr.driv.n.cook.repository.franchisee.entity.FranchiseeEntity;
import fr.driv.n.cook.repository.supply.order.SupplyOrderRepository;
import fr.driv.n.cook.repository.supply.order.entity.SupplyOrderEntity;
import fr.driv.n.cook.repository.truck.TruckRepository;
import fr.driv.n.cook.repository.truck.entity.TruckEntity;
import fr.driv.n.cook.repository.warehouse.WarehouseRepository;
import fr.driv.n.cook.repository.warehouse.entity.WarehouseEntity;
import fr.driv.n.cook.service.appointment.mapper.AppointmentMapper;
import fr.driv.n.cook.shared.AppointmentStatus;
import fr.driv.n.cook.shared.AppointmentType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class AppointmentService {

    @Inject
    AppointmentRepository appointmentRepository;

    @Inject
    WarehouseRepository warehouseRepository;

    @Inject
    SupplyOrderRepository supplyOrderRepository;

    @Inject
    TruckRepository truckRepository;

    @Inject
    FranchiseeRepository franchiseeRepository;

    @Inject
    AppointmentMapper mapper;

    @Transactional
    public Appointment create(Appointment appointmentDto, Long franchiseeId) {
        validateAppointmentPayload(appointmentDto);
        AppointmentEntity entity = new AppointmentEntity();
        entity.setType(appointmentDto.type());
        entity.setStatus(AppointmentStatus.SCHEDULED);
        entity.setDatetime(appointmentDto.datetime());
        entity.setWarehouse(fetchWarehouse(appointmentDto.warehouseId()));
        entity.setFranchisee(fetchFranchisee(franchiseeId));

        if (appointmentDto.type() == AppointmentType.SUPPLY_PICKUP) {
            entity.setSupplyOrder(fetchSupplyOrder(appointmentDto.supplyOrderId()));
        } else {
            entity.setTruck(fetchTruck(appointmentDto.truckId()));
        }

        appointmentRepository.persist(entity);
        return mapper.toDto(entity);
    }

    public List<Appointment> listForAdmin(AppointmentType type, Long warehouseId, LocalDateTime from, LocalDateTime to) {
        return appointmentRepository.listByFilters(type, warehouseId, from, to).stream()
                .map(mapper::toDto)
                .toList();
    }

    public Appointment getById(Long appointmentId) {
        return mapper.toDto(fetchAppointment(appointmentId));
    }

    @Transactional
    public Appointment patch(Long appointmentId, AppointmentPatch patch) {
        AppointmentEntity entity = fetchAppointment(appointmentId);
        if (patch.datetime() != null) {
            entity.setDatetime(patch.datetime());
        }
        if (patch.status() != null) {
            validateStatusTransition(entity.getStatus(), patch.status());
            entity.setStatus(patch.status());
        }
        return mapper.toDto(entity);
    }

    public List<Appointment> listForFranchisee(Long franchiseeId, AppointmentType type, Long warehouseId, LocalDateTime from, LocalDateTime to) {
        return appointmentRepository.listByFranchisee(franchiseeId, type, warehouseId, from, to).stream()
                .map(mapper::toDto)
                .toList();
    }

    public void assertAppointmentBelongsToFranchisee(Long appointmentId, Long franchiseeId) {
        AppointmentEntity entity = fetchAppointment(appointmentId);
        if (!isOwnedByFranchisee(entity, franchiseeId)) {
            throw new ForbiddenException("Rendez-vous inaccessible");
        }
    }

    private void validateAppointmentPayload(Appointment appointment) {
        if (appointment.type() == AppointmentType.SUPPLY_PICKUP) {
            if (appointment.supplyOrderId() == null) {
                throw new BadRequestException("supplyOrderId requis pour un retrait de commande");
            }
        } else if (appointment.type() == AppointmentType.TRUCK_PICKUP) {
            if (appointment.truckId() == null) {
                throw new BadRequestException("truckId requis pour un retrait de camion");
            }
        } else {
            throw new BadRequestException("Type de rendez-vous inconnu");
        }
        if (appointment.warehouseId() == null) {
            throw new BadRequestException("warehouseId requis");
        }
        if (appointment.datetime() == null) {
            throw new BadRequestException("datetime requis");
        }
    }

    private void validateStatusTransition(AppointmentStatus current, AppointmentStatus next) {
        if (current == AppointmentStatus.COMPLETED || current == AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Rendez-vous déjà clôturé");
        }
        if (next == AppointmentStatus.SCHEDULED && current != AppointmentStatus.SCHEDULED) {
            throw new BadRequestException("Retour à SCHEDULED interdit");
        }
    }

    private AppointmentEntity fetchAppointment(Long appointmentId) {
        return appointmentRepository.findByIdOptional(appointmentId)
                .orElseThrow(() -> new NotFoundException("Rendez-vous introuvable"));
    }

    private WarehouseEntity fetchWarehouse(Long warehouseId) {
        return warehouseRepository.findByIdOptional(warehouseId)
                .orElseThrow(() -> new NotFoundException("Entrepôt introuvable"));
    }

    private SupplyOrderEntity fetchSupplyOrder(Long supplyOrderId) {
        return supplyOrderRepository.findByIdOptional(supplyOrderId)
                .orElseThrow(() -> new NotFoundException("Commande d'approvisionnement introuvable"));
    }

    private TruckEntity fetchTruck(Long truckId) {
        return truckRepository.findByIdOptional(truckId)
                .orElseThrow(() -> new NotFoundException("Camion introuvable"));
    }

    private boolean isOwnedByFranchisee(AppointmentEntity entity, Long franchiseeId) {
        return entity.getFranchisee().getId().equals(franchiseeId);
    }

    private FranchiseeEntity fetchFranchisee(Long franchiseeId) {
        return franchiseeRepository.findByIdOptional(franchiseeId)
                .orElseThrow(() -> new NotFoundException("Franchisé introuvable"));
    }
}
