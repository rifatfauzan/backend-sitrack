package be_sitruck.backend_sitruck.restservice;

import be_sitruck.backend_sitruck.restdto.request.CreateUserRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateUserResponseDTO;

public interface UserRestService {
    CreateUserResponseDTO addUser(CreateUserRequestDTO createUserRequestDTO);
    String hashPassword(String password);
}
