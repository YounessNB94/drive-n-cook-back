package fr.driv.n.cook.service.truck;

import fr.driv.n.cook.presentation.incident.dto.Incident;
import fr.driv.n.cook.presentation.truck.dto.MaintenanceRecord;
import fr.driv.n.cook.presentation.truck.dto.Truck;
import fr.driv.n.cook.presentation.truck.dto.TruckPatch;
import fr.driv.n.cook.repository.franchisee.FranchiseeRepository;
import fr.driv.n.cook.repository.franchisee.entity.FranchiseeEntity;
import fr.driv.n.cook.repository.incident.IncidentRepository;
import fr.driv.n.cook.repository.truck.MaintenanceRecordRepository;
import fr.driv.n.cook.repository.truck.TruckRepository;
import fr.driv.n.cook.repository.truck.entity.MaintenanceRecordEntity;
import fr.driv.n.cook.repository.truck.entity.TruckEntity;
import fr.driv.n.cook.repository.warehouse.WarehouseRepository;
import fr.driv.n.cook.repository.warehouse.entity.WarehouseEntity;
import fr.driv.n.cook.service.incident.mapper.IncidentMapper;
import fr.driv.n.cook.service.truck.mapper.MaintenanceRecordMapper;
import fr.driv.n.cook.service.truck.mapper.TruckMapper;
import fr.driv.n.cook.shared.TruckStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class TruckService {

    @Inject
    TruckRepository truckRepository;

    @Inject
    IncidentRepository incidentRepository;

    @Inject
    MaintenanceRecordRepository maintenanceRecordRepository;

    @Inject
    FranchiseeRepository franchiseeRepository;

    @Inject
    WarehouseRepository warehouseRepository;

    @Inject
    TruckMapper mapper;

    @Inject
    IncidentMapper incidentMapper;

    @Inject
    MaintenanceRecordMapper maintenanceRecordMapper;

    public Truck getTruck(Long franchiseeId) {
        TruckEntity entity = truckRepository.findByFranchiseeId(franchiseeId);
        if (entity == null) {
            throw new NotFoundException("Aucun camion assigné à ce franchisé");
        }
        return mapper.toDto(entity);
    }

    public List<Truck> listTrucks(TruckStatus status, Long warehouseId) {
        return truckRepository.listByFilters(status, warehouseId).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public Truck createTruck(Truck truck) {
        if (truck.currentWarehouseId() == null) {
            throw new BadRequestException("L'entrepôt courant est obligatoire");
        }
        TruckEntity entity = new TruckEntity();
        entity.setPlateNumber(truck.plateNumber());
        entity.setStatus(TruckStatus.IN_SERVICE);
        entity.setCurrentWarehouse(resolveWarehouse(truck.currentWarehouseId()));
        truckRepository.persist(entity);
        return mapper.toDto(entity);
    }

    @Transactional
    public Truck patchTruck(Long truckId, TruckPatch patch) {
        TruckEntity entity = fetchTruck(truckId);
        if (patch.assignedFranchiseeId() != null) {
            entity.setFranchisee(resolveFranchisee(patch.assignedFranchiseeId()));
        }
        if (patch.currentWarehouseId() != null) {
            entity.setCurrentWarehouse(resolveWarehouse(patch.currentWarehouseId()));
        }
        return mapper.toDto(entity);
    }

    public List<Incident> listIncidents(Long truckId) {
        fetchTruck(truckId); // ensure exists
        return incidentRepository.listByFilters(null, truckId, null).stream()
                .map(incidentMapper::toDto)
                .toList();
    }

    @Transactional
    public MaintenanceRecord addMaintenanceRecord(Long truckId, MaintenanceRecord record) {
        TruckEntity truck = fetchTruck(truckId);
        MaintenanceRecordEntity entity = new MaintenanceRecordEntity();
        entity.setTruck(truck);
        entity.setDate(record.date());
        entity.setDescription(record.description());
        maintenanceRecordRepository.persist(entity);
        return maintenanceRecordMapper.toDto(entity);
    }

    public List<MaintenanceRecord> listMaintenance(Long truckId) {
        fetchTruck(truckId);
        return maintenanceRecordRepository.listByTruck(truckId).stream()
                .map(maintenanceRecordMapper::toDto)
                .toList();
    }

    private TruckEntity fetchTruck(Long truckId) {
        return truckRepository.findByIdOptional(truckId)
                .orElseThrow(() -> new NotFoundException("Camion introuvable"));
    }

    private WarehouseEntity resolveWarehouse(Long warehouseId) {
        if (warehouseId == null) {
            return null;
        }
        return warehouseRepository.findByIdOptional(warehouseId)
                .orElseThrow(() -> new NotFoundException("Entrepôt introuvable"));
    }

    private FranchiseeEntity resolveFranchisee(Long franchiseeId) {
        if (franchiseeId == null) {
            return null;
        }
        return franchiseeRepository.findByIdOptional(franchiseeId)
                .orElseThrow(() -> new NotFoundException("Franchisé introuvable"));
    }
}
