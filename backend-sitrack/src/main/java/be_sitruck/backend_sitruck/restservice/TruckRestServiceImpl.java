package be_sitruck.backend_sitruck.restservice;

import be_sitruck.backend_sitruck.model.NotificationCategory;
import be_sitruck.backend_sitruck.model.Truck;
import be_sitruck.backend_sitruck.repository.TruckDb;
import be_sitruck.backend_sitruck.restdto.request.CreateTruckRequestDTO;
import be_sitruck.backend_sitruck.restdto.request.UpdateTruckRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateTruckResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.UpdateTruckResponseDTO;
import be_sitruck.backend_sitruck.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TruckRestServiceImpl implements TruckRestService {

    @Autowired
    private TruckDb truckDb;

    @Autowired
    private NotificationRestService notificationRestService;

    @Autowired
    private JwtUtils jwtUtils;


    @Override
    public CreateTruckResponseDTO createTruck(CreateTruckRequestDTO request) {
        
        String vehiclePlateNo = request.getVehiclePlateNo().toUpperCase();
        String vehicleKIRNo = request.getVehicleKIRNo().toUpperCase();
        String vehicleBizLicenseNo = request.getVehicleBizLicenseNo().toUpperCase();
        String vehicleDispensationNo = request.getVehicleDispensationNo().toUpperCase();
        String vehicleNumber = request.getVehicleNumber().toUpperCase();
        
        if (truckDb.existsByVehiclePlateNo(vehiclePlateNo)) {
            throw new IllegalArgumentException("Plat Nomor terdaftar di truck lain!");
        }
        if (truckDb.existsByVehicleKIRNo(vehicleKIRNo)) {
            throw new IllegalArgumentException("KIR terdaftar di truck lain!");
        }

        if (!request.getVehicleBizLicenseNo().isEmpty() &&
            truckDb.existsByVehicleBizLicenseNo(vehicleBizLicenseNo)) {
            throw new IllegalArgumentException("Business License Number sudah terdaftar di truck lain!");
        }

        if (!request.getVehicleDispensationNo().isEmpty() &&
            truckDb.existsByVehicleDispensationNo(vehicleDispensationNo)) {
            throw new IllegalArgumentException("Dispensation Number sudah terdaftar di truck lain!");
        }

        if (!request.getVehicleNumber().isEmpty() &&
            truckDb.existsByVehicleNumber(vehicleNumber)) {
            throw new IllegalArgumentException("Vehicle Number sudah terdaftar di truck lain!");
        }

        String currentUser = jwtUtils.getCurrentUsername();

        String maxVehicleId = truckDb.findMaxVehicleId();

        int nextNumber = 1;
        if (maxVehicleId != null && maxVehicleId.length() > 2) {
            String numberPart = maxVehicleId.substring(maxVehicleId.length() - 5);
            try {
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                nextNumber = 1;
            }
        }
    
        String paddedNumber = String.format("%05d", nextNumber);
        String generatedVehicleId = "VH"+ paddedNumber;

        // Buat objek Truck baru
        Truck truck = new Truck();
        truck.setVehicleId(generatedVehicleId); 
        truck.setVehicleBrand(request.getVehicleBrand());
        truck.setVehicleYear(request.getVehicleYear());
        truck.setVehiclePlateNo(vehiclePlateNo);
        truck.setVehicleSTNKDate(request.getVehicleSTNKDate());
        truck.setVehicleKIRNo(vehicleKIRNo);
        truck.setVehicleKIRDate(request.getVehicleKIRDate());

        // Properti tambahan dari DTO yang bisa null
        truck.setVehicleCylinder(request.getVehicleCylinder());
        truck.setVehicleChassisNo(request.getVehicleChassisNo());
        truck.setVehicleEngineNo(request.getVehicleEngineNo());
        truck.setVehicleBizLicenseNo(request.getVehicleBizLicenseNo().toUpperCase());
        truck.setVehicleBizLicenseDate(request.getVehicleBizLicenseDate());
        truck.setVehicleDispensationNo(request.getVehicleDispensationNo().toUpperCase());
        truck.setVehicleDispensationDate(request.getVehicleDispensationDate());
        truck.setVehicleRemarks(request.getVehicleRemarks());
        truck.setSiteId(request.getSiteId() != null ? request.getSiteId() : "JKT"); // Default ke 'JKT'
        truck.setVehicleType(request.getVehicleType());
        truck.setDivision(request.getDivision());
        truck.setVehicleNumber(request.getVehicleNumber());
        truck.setRowStatus("A");
        truck.setRecordStatus(request.getRecordStatus());
        truck.setDept(request.getDept());
        truck.setVehicleFuelConsumption(request.getVehicleFuelConsumption() != null ? request.getVehicleFuelConsumption() : 0.0);
        truck.setVehicleGroup(request.getVehicleGroup());

        truck.setInsertedBy(currentUser);
        truck.setInsertedDate(new Date());


        // Simpan ke database
        truckDb.save(truck);

        // Buat response
        return new CreateTruckResponseDTO(
                truck.getVehicleId(),
                "Truck successfully added"
        );
    }

    @Override
    public List<CreateTruckRequestDTO> getAllTruck() {
        return truckDb.findAllOrdered().stream()
                .map(this::convertToTruckDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CreateTruckRequestDTO getTruckById(String vehicleId) {
        Truck truck = truckDb.findByVehicleId(vehicleId);

        if (truck == null) {
            throw new ValidationException("Truck dengan ID " + vehicleId + " tidak ditemukan!");
        }

        return convertToTruckDTO(truck);
    }

    private CreateTruckRequestDTO convertToTruckDTO(Truck truck) {
        return new CreateTruckRequestDTO(
            truck.getVehicleId(),
            truck.getVehicleBrand(),
            truck.getVehicleYear(),
            truck.getVehiclePlateNo(),
            truck.getVehicleSTNKDate(),
            truck.getVehicleKIRNo(),
            truck.getVehicleKIRDate(),
            truck.getDivision(), 
            truck.getVehicleCylinder(),
            truck.getVehicleChassisNo(),
            truck.getVehicleEngineNo(),
            truck.getVehicleBizLicenseNo(),
            truck.getVehicleBizLicenseDate(),
            truck.getVehicleDispensationNo(),
            truck.getVehicleDispensationDate(),
            truck.getVehicleRemarks(),
            truck.getSiteId(),
            truck.getVehicleType(),
            truck.getDept(), 
            truck.getRecordStatus(),
            truck.getRowStatus(), 
            truck.getVehicleNumber(),
            truck.getVehicleFuelConsumption(),
            truck.getVehicleGroup(),
            truck.getInsertedBy(),
            truck.getInsertedDate(),
            truck.getUpdatedBy(),
            truck.getUpdatedDate()
        );
    }
    

    @Transactional
    @Override
    public UpdateTruckResponseDTO updateTruck(String vehicleId, UpdateTruckRequestDTO request) {
        Truck truck = truckDb.findByVehicleId(vehicleId);
    
        if (truck == null) {
            throw new ValidationException("Truck dengan ID " + vehicleId + " tidak ditemukan!");
        }
    
        String vehiclePlateNo = request.getVehiclePlateNo().toUpperCase();
        String vehicleKIRNo = request.getVehicleKIRNo().toUpperCase();
        String vehicleBizLicenseNo = request.getVehicleBizLicenseNo().toUpperCase();
        String vehicleDispensationNo = request.getVehicleDispensationNo().toUpperCase();
        String vehicleNumber = request.getVehicleNumber().toUpperCase();
    
        // Validasi STNK & KIR
        if (!truck.getVehiclePlateNo().equalsIgnoreCase(vehiclePlateNo) &&
            truckDb.existsByVehiclePlateNo(vehiclePlateNo)) {
            throw new IllegalArgumentException("Plat Nomor terdaftar di truck lain!");
        }

        if (!truck.getVehicleKIRNo().equalsIgnoreCase(vehicleKIRNo) &&
            truckDb.existsByVehicleKIRNo(vehicleKIRNo)) {
            throw new IllegalArgumentException("KIR terdaftar di truck lain!");
        }

        if (vehicleBizLicenseNo != null && !vehicleBizLicenseNo.isEmpty()) {
            Truck existingTruck = truckDb.findByVehicleBizLicenseNo(vehicleBizLicenseNo);
            if (existingTruck != null && !existingTruck.getVehicleId().equals(vehicleId)) {
                throw new IllegalArgumentException("Business License Number sudah terdaftar di truck lain!");
            }
        }

        if (vehicleDispensationNo != null && !vehicleDispensationNo.isEmpty()) {
            Truck existingTruck = truckDb.findByVehicleDispensationNo(vehicleDispensationNo);
            if (existingTruck != null && !existingTruck.getVehicleId().equals(vehicleId)) {
                throw new IllegalArgumentException("Dispensation Number sudah terdaftar di truck lain!");
            }
        }

        if (vehicleNumber != null && !vehicleNumber.isEmpty()) {
            Truck existingTruck = truckDb.findByVehicleNumber(vehicleNumber);
            if (existingTruck != null && !existingTruck.getVehicleId().equals(vehicleId)) {
                throw new IllegalArgumentException("Vehicle Number sudah terdaftar di truck lain!");
            }
        }
    
        String currentUser = jwtUtils.getCurrentUsername();
    
        if (!truck.getVehicleSTNKDate().equals(request.getVehicleSTNKDate())) {
            notificationRestService.deactivateNotificationsByCategoryAndReference(
                NotificationCategory.VEHICLE_STNK_EXPIRY,
                "TRUCK",
                vehicleId
            );
        }
        
        if (!truck.getVehicleKIRDate().equals(request.getVehicleKIRDate())) {
            notificationRestService.deactivateNotificationsByCategoryAndReference(
                NotificationCategory.VEHICLE_KIR_EXPIRY,
                "TRUCK",
                vehicleId
            );
        }
    
        truck.setVehicleId(vehicleId);
        truck.setVehicleBrand(request.getVehicleBrand());
        truck.setVehicleYear(request.getVehicleYear());
        truck.setVehiclePlateNo(vehiclePlateNo);
        truck.setVehicleSTNKDate(request.getVehicleSTNKDate());
        truck.setVehicleKIRNo(vehicleKIRNo);
        truck.setVehicleKIRDate(request.getVehicleKIRDate());
        truck.setUpdatedBy(currentUser);
        truck.setUpdatedDate(new Date());
    
        truck.setVehicleCylinder(request.getVehicleCylinder());
        truck.setVehicleChassisNo(request.getVehicleChassisNo());
        truck.setVehicleEngineNo(request.getVehicleEngineNo());
        truck.setVehicleBizLicenseNo(request.getVehicleBizLicenseNo().toUpperCase());
        truck.setVehicleBizLicenseDate(request.getVehicleBizLicenseDate());
        truck.setVehicleDispensationNo(request.getVehicleDispensationNo().toUpperCase());
        truck.setVehicleDispensationDate(request.getVehicleDispensationDate());
        truck.setVehicleRemarks(request.getVehicleRemarks());
        truck.setSiteId(request.getSiteId() != null ? request.getSiteId(): "JKT");
        truck.setVehicleType(request.getVehicleType());
        truck.setDivision(request.getDivision());
        truck.setVehicleNumber(request.getVehicleNumber());
        truck.setRowStatus(request.getRowStatus());
        truck.setDept(request.getDept());
        truck.setRecordStatus(request.getRecordStatus());
        truck.setVehicleFuelConsumption(request.getVehicleFuelConsumption() != null ? request.getVehicleFuelConsumption() : 0.0);
        truck.setVehicleGroup(request.getVehicleGroup());
    
        truckDb.save(truck);
    
        return new UpdateTruckResponseDTO(
            truck.getVehicleId(),
            "Truck successfully updated"
        );
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // setiap tengah malam
    public void checkExpiringTrucks() {
        List<Truck> trucks = truckDb.findAll();
        Date today = new Date();
    
        for (Truck truck : trucks) {
            boolean updated = false;
    
            if (truck.getVehicleKIRDate() != null && truck.getVehicleKIRDate().before(today)) {
                truck.setRowStatus("I");
                updated = true;
            }
    
            if (truck.getVehicleSTNKDate() != null && truck.getVehicleSTNKDate().before(today)) {
                truck.setRowStatus("I");
                updated = true;
            }
    
            if (updated) truckDb.save(truck);
        }
    }
}
