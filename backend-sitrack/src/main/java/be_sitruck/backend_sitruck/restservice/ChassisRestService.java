package be_sitruck.backend_sitruck.restservice;

import be_sitruck.backend_sitruck.restdto.request.CreateChassisRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateChassisResponseDTO;


public interface ChassisRestService {
    CreateChassisResponseDTO addChassis(CreateChassisRequestDTO createChassisRequestDTO);   
}
