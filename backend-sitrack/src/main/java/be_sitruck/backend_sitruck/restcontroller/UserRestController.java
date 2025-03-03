package be_sitruck.backend_sitruck.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import be_sitruck.backend_sitruck.repository.UserDb;
import be_sitruck.backend_sitruck.restdto.request.CreateUserRequestDTO;
import be_sitruck.backend_sitruck.restdto.request.UpdateUserRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.BaseResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateUserResponseDTO;
import be_sitruck.backend_sitruck.restservice.UserRestService;

import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserRestController {
    @Autowired
    UserRestService userRestService;

    @Autowired
    UserDb userDb;

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody CreateUserRequestDTO userRequestDTO){
        var baseResponseDTO = new BaseResponseDTO<CreateUserResponseDTO>();
        try {
            CreateUserResponseDTO userResponseDTO = userRestService.addUser(userRequestDTO);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("User berhasil ditambahkan!");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(userResponseDTO);
            return ResponseEntity.ok(baseResponseDTO);

        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body(baseResponseDTO);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(){
        var baseResponseDTO = new BaseResponseDTO<List<CreateUserResponseDTO>>();
        List<CreateUserResponseDTO> listUser = userRestService.getAllUsers();

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listUser);
        baseResponseDTO.setMessage(String.format("List user berhasil ditemukan sebanyak %d user", listUser.size()));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);

    }

    @GetMapping("/detail")
    public ResponseEntity<?> getUserById(@RequestParam("id") Long id) {
        var baseResponseDTO = new BaseResponseDTO<CreateUserResponseDTO>();
        try {
            CreateUserResponseDTO userResponseDTO = userRestService.getUserById(id);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("User berhasil ditemukan!");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(userResponseDTO);
            return ResponseEntity.ok(baseResponseDTO);

        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body(baseResponseDTO);
        }   
    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponseDTO<CreateUserResponseDTO>> updateUser(
            @RequestParam("id") Long id,
            @RequestBody UpdateUserRequestDTO requestDTO) {
        try {
            CreateUserResponseDTO updatedUser = userRestService.updateUser(id, requestDTO);
            
            var baseResponseDTO = new BaseResponseDTO<CreateUserResponseDTO>();
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(updatedUser);
            baseResponseDTO.setMessage("User dengan ID " + id + " berhasil diperbarui");
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.ok(baseResponseDTO);
        } catch (NoSuchElementException e) {
            var baseResponseDTO = new BaseResponseDTO<CreateUserResponseDTO>();
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponseDTO);
        } catch (IllegalArgumentException e) {
            var baseResponseDTO = new BaseResponseDTO<CreateUserResponseDTO>();
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponseDTO);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("id") Long id) {
        var baseResponseDTO = new BaseResponseDTO<String>();
        try {
            userRestService.deleteUserById(id);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("User berhasil dihapus!");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            return ResponseEntity.ok(baseResponseDTO);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }
}
