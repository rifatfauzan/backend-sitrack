package be_sitruck.backend_sitruck.restcontroller;

import be_sitruck.backend_sitruck.restdto.request.CreateChassisRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.BaseResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateChassisResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateUserResponseDTO;
import be_sitruck.backend_sitruck.restservice.ChassisRestService;
import be_sitruck.backend_sitruck.security.jwt.JwtUtils;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chassis")
public class ChassisController {

    // @Autowired
    // private JwtUtils jwtUtils;

    @Autowired
    private ChassisRestService chassisRestService;

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody CreateChassisRequestDTO chassisRequestDTO){
        var baseResponseDTO = new BaseResponseDTO<CreateChassisResponseDTO>();
        try {
            CreateChassisResponseDTO chassisResponseDTO = chassisRestService.addChassis(chassisRequestDTO);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Chassis berhasil ditambahkan!");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(chassisResponseDTO);
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
        var baseResponseDTO = new BaseResponseDTO<List<CreateChassisRequestDTO>>();
        List<CreateChassisRequestDTO> listChassis = chassisRestService.getAllChassis();

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listChassis);
        baseResponseDTO.setMessage(String.format("List chassis berhasil ditemukan sebanyak %d chassis", listChassis.size()));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);

    }
   
}

