package be_sitruck.backend_sitruck.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import be_sitruck.backend_sitruck.model.Role;
import be_sitruck.backend_sitruck.model.UserModel;
import be_sitruck.backend_sitruck.repository.UserDb;
import be_sitruck.backend_sitruck.restdto.request.CreateUserRequestDTO;
import be_sitruck.backend_sitruck.restdto.request.UpdateUserRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateUserResponseDTO;

import java.util.*;

@Service
public class UserRestServiceImpl implements UserRestService {
    @Autowired
    private UserDb userDb;

    @Autowired
    private RoleRestService roleService;

    @Override
    public CreateUserResponseDTO addUser(CreateUserRequestDTO requestDTO) {
        try {
            if (userDb.existsByUsernameIgnoreCase(requestDTO.getUsername())) {
                throw new RuntimeException("Username " + requestDTO.getUsername() + " sudah terdaftar!");
            }
            
            Role role = roleService.getRoleByRoleName(requestDTO.getRole());
            if (role == null) {
                throw new RuntimeException("Role tidak ditemukan");
            }
            
            UserModel user = new UserModel();
            user.setUsername(requestDTO.getUsername().toLowerCase());
            user.setPassword(hashPassword(requestDTO.getPassword()));
            user.setRole(role);
            
            user = userDb.save(user);
            return new CreateUserResponseDTO(user.getId(), user.getUsername(), user.getRole().getRole());
            

        } catch (Exception e) {
            throw new RuntimeException("Gagal membuat user: " + e.getMessage());
        }
    }

    @Override
    public CreateUserResponseDTO getUserById(Long id) {
        UserModel user = userDb.findById(id).orElse(null);
        if (user == null) {
            throw new RuntimeException("User tidak ditemukan!");
        }
        return new CreateUserResponseDTO(user.getId(), user.getUsername(), user.getRole().getRole());
    }

    @Override
    public CreateUserResponseDTO updateUser(Long id, UpdateUserRequestDTO requestDTO) {
        try {
            UserModel existingUser = userDb.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User dengan ID " + id + " tidak ditemukan!"));
            
            if (!existingUser.getUsername().equalsIgnoreCase(requestDTO.getUsername()) && 
                userDb.existsByUsernameIgnoreCase(requestDTO.getUsername())) {
                throw new IllegalArgumentException("Username " + requestDTO.getUsername() + " sudah terdaftar!");
            }
            
            existingUser.setUsername(requestDTO.getUsername());
            
            if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
                existingUser.setPassword(hashPassword(requestDTO.getPassword()));
            }
            
            if (requestDTO.getRole() != null && !requestDTO.getRole().isEmpty()) {
                Role role = roleService.getRoleByRoleName(requestDTO.getRole());
                if (role == null) {
                    throw new IllegalArgumentException("Role tidak ditemukan");
                }
                existingUser.setRole(role);
            }
            
            UserModel updatedUser = userDb.save(existingUser);
            
            return new CreateUserResponseDTO(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getRole().getRole()
            );
        } catch (NoSuchElementException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Gagal memperbarui user: " + e.getMessage());
        }
    }

    @Override
    public void deleteUserById(Long id){
        try{
            UserModel user = userDb.findById(id)
            .orElseThrow(() -> new NoSuchElementException("User dengan ID " + id + " tidak ditemukan!"));

            if (user.getRole().getRole().equals("Admin")) {
                throw new IllegalArgumentException("User dengan role Admin tidak dapat dihapus!");
            }

            userDb.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Gagal menghapus user: " + e.getMessage());
        }
    }

    @Override
    public List<CreateUserResponseDTO> getAllUsers(){
        List<UserModel> users = userDb.findAll();
        List<CreateUserResponseDTO> responseDTOs = new ArrayList<>();
        for (UserModel user : users) {
            responseDTOs.add(new CreateUserResponseDTO(user.getId(), user.getUsername(), user.getRole().getRole()));
        }
        return responseDTOs;
    }


    @Override
    public String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public UserModel authenticateWithUsername(String username, String password) throws Exception {
        UserModel user = userDb.findByUsernameIgnoreCase(username);
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

