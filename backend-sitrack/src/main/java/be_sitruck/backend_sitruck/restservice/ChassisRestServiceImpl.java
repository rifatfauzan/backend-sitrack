package be_sitruck.backend_sitruck.restservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be_sitruck.backend_sitruck.model.Chassis;
import be_sitruck.backend_sitruck.model.Truck;
import be_sitruck.backend_sitruck.model.UserModel;
import be_sitruck.backend_sitruck.model.NotificationCategory;
import be_sitruck.backend_sitruck.repository.ChassisDb;
import be_sitruck.backend_sitruck.restdto.request.CreateChassisRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateChassisResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateUserResponseDTO;
import be_sitruck.backend_sitruck.security.jwt.JwtUtils;
import jakarta.validation.ValidationException;

@Service
public class ChassisRestServiceImpl implements ChassisRestService {
    
    @Autowired
    private ChassisDb chassisDb;

    @Autowired
    private NotificationRestService notificationRestService;

    @Autowired
    private JwtUtils jwtUtils;
    
    @Transactional
    @Override
    public CreateChassisResponseDTO addChassis(CreateChassisRequestDTO createChassisRequestDTO) {
        Chassis existingChassis = chassisDb.findByChassisKIRNo(createChassisRequestDTO.getChassisKIRNo());
        if (existingChassis != null) {
            throw new ValidationException("Nomor KIR sudah terdaftar dalam sistem!");
        }
    
        String currentUser = jwtUtils.getCurrentUsername();
        String chassisId = generateChassisId();
    
        Chassis chassis = new Chassis();
        chassis.setChassisId(chassisId);
        chassis.setChassisSize(createChassisRequestDTO.getChassisSize());
        chassis.setChassisYear(createChassisRequestDTO.getChassisYear());
        chassis.setChassisNumber(createChassisRequestDTO.getChassisNumber());
        chassis.setChassisAxle(createChassisRequestDTO.getChassisAxle());
        chassis.setChassisKIRNo(createChassisRequestDTO.getChassisKIRNo());
        chassis.setChassisKIRDate(createChassisRequestDTO.getChassisKIRDate());
        chassis.setChassisType(createChassisRequestDTO.getChassisType().toUpperCase());
        chassis.setChassisRemarks(createChassisRequestDTO.getChassisRemarks());
        chassis.setInsertedBy(currentUser);
        chassis.setInsertedDate(new Date());
        chassis.setDivision(createChassisRequestDTO.getDivision().toUpperCase());
        chassis.setDept(createChassisRequestDTO.getDept().toUpperCase().toUpperCase());
        chassis.setRowStatus("A");

        chassis.setSiteId(createChassisRequestDTO.getSiteId().toUpperCase());
    
        chassisDb.save(chassis);
    
        return new CreateChassisResponseDTO("Chassis berhasil ditambahkan!", chassis.getChassisId());
    }

    @Override
    public List<CreateChassisRequestDTO> getAllChassis() {
        return chassisDb.findAll().stream()
                .map(this::convertToChassisDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CreateChassisRequestDTO getChassisById(String chassisId) {
        Chassis chassis = chassisDb.findByChassisId(chassisId);

        if (chassis == null) {
            throw new ValidationException("Chassis dengan ID " + chassisId + " tidak ditemukan!");
        }

        return convertToChassisDTO(chassis);
    }

    private CreateChassisRequestDTO convertToChassisDTO(Chassis chassis) {
        return new CreateChassisRequestDTO(
                chassis.getChassisId(),
                chassis.getChassisSize(),
                chassis.getChassisYear(),
                chassis.getChassisNumber(),
                chassis.getChassisAxle(),
                chassis.getChassisKIRNo(),
                chassis.getChassisKIRDate(),
                chassis.getChassisType(),
                chassis.getChassisRemarks(),
                chassis.getInsertedBy(),
                chassis.getInsertedDate(),
                chassis.getUpdatedBy(),
                chassis.getUpdatedDate(),
                chassis.getDivision(),
                chassis.getDept(),
                chassis.getRowStatus(),
                chassis.getSiteId()
        );
    }

    @Transactional
    @Override
    public CreateChassisResponseDTO updateChassis(String chassisId, CreateChassisRequestDTO updateRequest) {
        Chassis existingChassis = chassisDb.findByChassisId(chassisId);

        if (existingChassis == null) {
            throw new ValidationException("Chassis dengan ID " + chassisId + " tidak ditemukan!");
        }

        Chassis duplicateChassis = chassisDb.findByChassisKIRNo(updateRequest.getChassisKIRNo());
        if (duplicateChassis != null && !duplicateChassis.getChassisId().equals(chassisId)) {
            throw new ValidationException("Nomor KIR sudah terdaftar dalam sistem!");
        }

        if (!existingChassis.getChassisKIRDate().equals(updateRequest.getChassisKIRDate())) {
            notificationRestService.deactivateNotificationsByCategoryAndReference(
                NotificationCategory.CHASSIS_KIR_EXPIRY,
                "CHASSIS",
                chassisId
            );
        }

        existingChassis.setChassisSize(updateRequest.getChassisSize());
        existingChassis.setChassisYear(updateRequest.getChassisYear());
        existingChassis.setChassisNumber(updateRequest.getChassisNumber());
        existingChassis.setChassisAxle(updateRequest.getChassisAxle());
        existingChassis.setChassisKIRNo(updateRequest.getChassisKIRNo());
        existingChassis.setChassisKIRDate(updateRequest.getChassisKIRDate());
        existingChassis.setChassisRemarks(updateRequest.getChassisRemarks());
        existingChassis.setDivision(updateRequest.getDivision());
        existingChassis.setDept(updateRequest.getDept());
        existingChassis.setUpdatedBy(jwtUtils.getCurrentUsername());
        existingChassis.setUpdatedDate(new Date());

        chassisDb.save(existingChassis);

        return new CreateChassisResponseDTO("Chassis berhasil diperbarui!", existingChassis.getChassisId());
    }


    private String generateChassisId() {
        Chassis lastChassis = chassisDb.findTopByChassisIdStartingWithOrderByChassisIdDesc("CH");
        
        int nextNumber = 1;
        if (lastChassis != null) {
            String lastChassisId = lastChassis.getChassisId();
            String numberPart = lastChassisId.substring("CH".length());
            
            try {
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                nextNumber = 1;
            }
        }
        
        return String.format("CH%05d", nextNumber);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // setiap tengah malam
    public void checkExpiringChassis() {
        List<Chassis> allChassis = chassisDb.findAll();
        Date today = new Date();
    
        for (Chassis chassis : allChassis) {
            boolean updated = false;
    
            if (chassis.getChassisKIRDate() != null && chassis.getChassisKIRDate().before(today)) {
                chassis.setRowStatus("I");
                updated = true;
            }
    
            if (updated) chassisDb.save(chassis);
        }
    }
    
}
