package be_sitruck.backend_sitruck.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import be_sitruck.backend_sitruck.model.UserModel;
import be_sitruck.backend_sitruck.repository.UserDb;
import be_sitruck.backend_sitruck.restdto.request.CreateUserRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateUserResponseDTO;

@Service
public class UserRestServiceImpl implements UserRestService {
    @Autowired
    private UserDb userDb;

    @Autowired
    private RoleRestService roleService;

    @Override
    public CreateUserResponseDTO addUser(CreateUserRequestDTO requestDTO) {
        UserModel user = new UserModel();
        user.setUsername(requestDTO.getUsername());
        user.setRole(roleService.getRoleByRoleName(requestDTO.getRole()));
        user.setPassword(hashPassword(requestDTO.getPassword()));
        userDb.save(user);

        CreateUserResponseDTO responseDTO = new CreateUserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setRole(user.getRole().getRole());
        responseDTO.setUsername(user.getUsername());
        return responseDTO;
    }

    @Override
    public String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }   
}

