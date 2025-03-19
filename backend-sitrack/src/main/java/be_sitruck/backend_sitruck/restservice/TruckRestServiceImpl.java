package be_sitruck.backend_sitruck.restservice;


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
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TruckRestServiceImpl implements TruckRestService {

    @Autowired
    private TruckDb truckDb;

    @Autowired
    private JwtUtils jwtUtils;


    @Override
    public CreateTruckResponseDTO createTruck(CreateTruckRequestDTO request) {
        // Validasi STNK dan KIR tidak boleh duplikat
        if (truckDb.existsByVehiclePlateNo(request.getVehiclePlateNo())) {
            throw new IllegalArgumentException("Plat Nomor terdaftar di truck lain!");
        }

        if (truckDb.existsByVehicleKIRNo(request.getVehicleKIRNo())) {
            throw new IllegalArgumentException("KIR terdaftar di truck lain!");
        }

        String currentUser = jwtUtils.getCurrentUsername();

        String maxVehicleId = truckDb.findMaxVehicleId();

        int nextNumber = 1;
        if (maxVehicleId != null && maxVehicleId.length() > 3) {
            String numberPart = maxVehicleId.substring(maxVehicleId.length() - 5);
            try {
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                nextNumber = 1;
            }
        }
    
        String paddedNumber = String.format("%05d", nextNumber);
        String generatedVehicleId = request.getSiteId() + paddedNumber;

        // Buat objek Truck baru
        Truck truck = new Truck();
        truck.setVehicleId(generatedVehicleId); 
        truck.setVehicleBrand(request.getVehicleBrand());
        truck.setVehicleYear(request.getVehicleYear());
        truck.setVehiclePlateNo(request.getVehiclePlateNo());
        truck.setVehicleSTNKDate(request.getVehicleSTNKDate());
        truck.setVehicleKIRNo(request.getVehicleKIRNo());
        truck.setVehicleKIRDate(request.getVehicleKIRDate());

        // Properti tambahan dari DTO yang bisa null
        truck.setVehicleCylinder(request.getVehicleCylinder());
        truck.setVehicleChassisNo(request.getVehicleChassisNo());
        truck.setVehicleEngineNo(request.getVehicleEngineNo());
        truck.setVehicleBizLicenseNo(request.getVehicleBizLicenseNo());
        truck.setVehicleBizLicenseDate(request.getVehicleBizLicenseDate());
        truck.setVehicleDispensationNo(request.getVehicleDispensationNo());
        truck.setVehicleDispensationDate(request.getVehicleDispensationDate());
        truck.setVehicleRemarks(request.getVehicleRemarks());
        truck.setSiteId(request.getSiteId() != null ? request.getSiteId() : "JKT"); // Default ke 'JKT'
        truck.setVehicleType(request.getVehicleType());
        truck.setDivision(request.getDivision());
        truck.setVehicleNumber(request.getVehicleNumber());
        truck.setRowStatus(request.getRowStatus());
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
        return truckDb.findAll().stream()
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

        // Validasi STNK dan KIR tidak boleh digunakan oleh truk lain
        if (!truck.getVehiclePlateNo().equals(request.getVehiclePlateNo()) &&
            truckDb.existsByVehiclePlateNo(request.getVehiclePlateNo())) {
            throw new IllegalArgumentException("Vehicle plate number already exists!");
        }

        if (!truck.getVehicleKIRNo().equals(request.getVehicleKIRNo()) &&
            truckDb.existsByVehicleKIRNo(request.getVehicleKIRNo())) {
            throw new IllegalArgumentException("Vehicle KIR number already exists!");
        }

        String currentUser = jwtUtils.getCurrentUsername();

        // Update data kendaraan
        truck.setVehicleId(vehicleId);
        truck.setVehicleBrand(request.getVehicleBrand());
        truck.setVehicleYear(request.getVehicleYear());
        truck.setVehiclePlateNo(request.getVehiclePlateNo());
        truck.setVehicleSTNKDate(request.getVehicleSTNKDate());
        truck.setVehicleKIRNo(request.getVehicleKIRNo());
        truck.setVehicleKIRDate(request.getVehicleKIRDate());
        truck.setUpdatedBy(currentUser);
        truck.setUpdatedDate(new Date());

        // Properti tambahan dari DTO yang bisa null
        truck.setVehicleCylinder(request.getVehicleCylinder());
        truck.setVehicleChassisNo(request.getVehicleChassisNo());
        truck.setVehicleEngineNo(request.getVehicleEngineNo());
        truck.setVehicleBizLicenseNo(request.getVehicleBizLicenseNo());
        truck.setVehicleBizLicenseDate(request.getVehicleBizLicenseDate());
        truck.setVehicleDispensationNo(request.getVehicleDispensationNo());
        truck.setVehicleDispensationDate(request.getVehicleDispensationDate());
        truck.setVehicleRemarks(request.getVehicleRemarks());
        truck.setSiteId(request.getSiteId() != null ? request.getSiteId() : "JKT"); // Default ke 'JKT'
        truck.setVehicleType(request.getVehicleType());
        truck.setDivision(request.getDivision());
        truck.setVehicleNumber(request.getVehicleNumber());
        truck.setRowStatus(request.getRowStatus());
        truck.setDept(request.getDept());
        truck.setRecordStatus(request.getRecordStatus());
        truck.setVehicleFuelConsumption(request.getVehicleFuelConsumption() != null ? request.getVehicleFuelConsumption() : 0.0);
        truck.setVehicleGroup(request.getVehicleGroup());

        // Simpan perubahan
        truckDb.save(truck);

        return new UpdateTruckResponseDTO(
                truck.getVehicleId(),
                "Truck successfully updated"
        );
    }
}
