package be_sitruck.backend_sitruck.restservice;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be_sitruck.backend_sitruck.model.Chassis;
import be_sitruck.backend_sitruck.repository.ChassisDb;
import be_sitruck.backend_sitruck.restdto.request.CreateChassisRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateChassisResponseDTO;
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
}
