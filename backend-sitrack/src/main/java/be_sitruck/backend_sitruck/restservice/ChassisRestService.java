package be_sitruck.backend_sitruck.restservice;

import java.util.List;

import be_sitruck.backend_sitruck.restdto.request.CreateChassisRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateChassisResponseDTO;


public interface ChassisRestService {
    CreateChassisResponseDTO addChassis(CreateChassisRequestDTO createChassisRequestDTO);
    List<CreateChassisRequestDTO> getAllChassis();
    CreateChassisRequestDTO getChassisById(String chassisId);
    CreateChassisResponseDTO updateChassis(String chassisId, CreateChassisRequestDTO updateRequest);
}
