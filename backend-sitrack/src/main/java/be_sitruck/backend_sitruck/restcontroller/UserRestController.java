package be_sitruck.backend_sitruck.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be_sitruck.backend_sitruck.repository.UserDb;
import be_sitruck.backend_sitruck.restdto.request.CreateUserRequestDTO;
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
}
