package be_sitruck.backend_sitruck.restservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be_sitruck.backend_sitruck.model.Chassis;
import be_sitruck.backend_sitruck.model.UserModel;
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
    private JwtUtils jwtUtils;
    
    @Transactional
    @Override
    public CreateChassisResponseDTO addChassis(CreateChassisRequestDTO createChassisRequestDTO) {

        // ID creation not handled

        Chassis existingChassis = chassisDb.findByChassisKIRNo(createChassisRequestDTO.getChassisKIRNo());
        if (existingChassis != null) {
            throw new ValidationException("Nomor KIR sudah terdaftar dalam sistem!");
        }

        if (createChassisRequestDTO.getChassisId() == null || createChassisRequestDTO.getChassisId().isEmpty()) {
            throw new ValidationException("ID tidak boleh kosong!");
        }

        if (createChassisRequestDTO.getChassisSize() == null || createChassisRequestDTO.getChassisSize().isEmpty()) {
            throw new ValidationException("Size tidak boleh kosong!");
        }

        if (createChassisRequestDTO.getChassisKIRNo() == null || createChassisRequestDTO.getChassisKIRNo().isEmpty()) {
            throw new ValidationException("Nomor KIR tidak boleh kosong!");
        }

        String currentUser = jwtUtils.getCurrentUsername();

        Chassis chassis = new Chassis();
        chassis.setChassisId(createChassisRequestDTO.getChassisId());
        chassis.setChassisSize(createChassisRequestDTO.getChassisSize());
        chassis.setChassisYear(createChassisRequestDTO.getChassisYear());
        chassis.setChassisNumber(createChassisRequestDTO.getChassisNumber());
        chassis.setChassisAxle(createChassisRequestDTO.getChassisAxle());
        chassis.setChassisKIRNo(createChassisRequestDTO.getChassisKIRNo());
        chassis.setChassisKIRDate(createChassisRequestDTO.getChassisKIRDate());
        chassis.setChassisType(createChassisRequestDTO.getChassisType());
        chassis.setInsertedBy(currentUser);
        chassis.setInsertedDate(new Date());
        chassis.setSiteId(createChassisRequestDTO.getSiteId());

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
                chassis.getInsertedBy(),
                chassis.getInsertedDate(),
                chassis.getUpdatedBy(),
                chassis.getUpdatedDate(),
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

        if (updateRequest.getChassisSize().length() > 2) {
            throw new ValidationException("Ukuran chassis tidak boleh lebih dari 2 karakter!");
        }
        if (updateRequest.getChassisNumber().length() > 6) {
            throw new ValidationException("Nomor chassis tidak boleh lebih dari 6 karakter!");
        }
        
        existingChassis.setChassisSize(updateRequest.getChassisSize());
        existingChassis.setChassisYear(updateRequest.getChassisYear());
        existingChassis.setChassisNumber(updateRequest.getChassisNumber());
        existingChassis.setChassisAxle(updateRequest.getChassisAxle());
        existingChassis.setChassisKIRNo(updateRequest.getChassisKIRNo());
        existingChassis.setChassisKIRDate(updateRequest.getChassisKIRDate());
        existingChassis.setChassisType(updateRequest.getChassisType());

        existingChassis.setUpdatedBy(jwtUtils.getCurrentUsername());
        existingChassis.setUpdatedDate(new Date());
        
        existingChassis.setSiteId(updateRequest.getSiteId());

        chassisDb.save(existingChassis);

        return new CreateChassisResponseDTO("Chassis berhasil diperbarui!", existingChassis.getChassisId());
    }



}
