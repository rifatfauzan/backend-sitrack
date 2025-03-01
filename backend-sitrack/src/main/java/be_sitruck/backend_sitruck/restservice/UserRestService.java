package be_sitruck.backend_sitruck.restservice;

import be_sitruck.backend_sitruck.model.UserModel;
import be_sitruck.backend_sitruck.restdto.request.CreateUserRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateUserResponseDTO;
import java.util.*;

public interface UserRestService {
    CreateUserResponseDTO addUser(CreateUserRequestDTO createUserRequestDTO);
    String hashPassword(String password);
    UserModel authenticateWithUsername(String username, String password) throws Exception;
    List<CreateUserResponseDTO> getAllUsers();
}
