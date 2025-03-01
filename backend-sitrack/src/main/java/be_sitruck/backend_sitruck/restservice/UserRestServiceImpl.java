package be_sitruck.backend_sitruck.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import be_sitruck.backend_sitruck.model.Role;
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
        try {
            if (userDb.existsByUsername(requestDTO.getUsername())) {
                throw new RuntimeException("Username " + requestDTO.getUsername() + " sudah terdaftar!");
            }
            
            Role role = roleService.getRoleByRoleName(requestDTO.getRole());
            if (role == null) {
                throw new RuntimeException("Role tidak ditemukan");
            }
            
            UserModel user = new UserModel();
            user.setUsername(requestDTO.getUsername());
            user.setPassword(hashPassword(requestDTO.getPassword()));
            user.setRole(role);
            
            user = userDb.save(user);
            return new CreateUserResponseDTO(user.getId(), user.getUsername(), user.getRole().getRole());
            

        } catch (Exception e) {
            throw new RuntimeException("Gagal membuat user: " + e.getMessage());
        }
    }


    @Override
    public String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public UserModel authenticateWithUsername(String username, String password) throws Exception {
        UserModel user = userDb.findByUsername(username);
        if (user == null) {
            throw new Exception("User tidak ditemukan!");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Password salah!");
        }
        return user;
    }
}

