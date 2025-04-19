package be_sitruck.backend_sitruck.restcontroller;

import be_sitruck.backend_sitruck.restdto.request.CreateChassisRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.BaseResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateChassisResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateUserResponseDTO;
import be_sitruck.backend_sitruck.restservice.ChassisRestService;
import be_sitruck.backend_sitruck.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chassis")
public class ChassisController {

    // @Autowired
    // private JwtUtils jwtUtils;

    @Autowired
    private ChassisRestService chassisRestService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponseDTO<?>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        BaseResponseDTO<Object> resp = new BaseResponseDTO<>();
        String msg = ex.getBindingResult()
                       .getFieldError()
                       .getDefaultMessage();  

        resp.setStatus(HttpStatus.BAD_REQUEST.value());
        resp.setMessage("Gagal menambah chassis: " + msg);
        resp.setTimestamp(new Date());
        resp.setData(null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(resp);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addChassis(@Valid @RequestBody CreateChassisRequestDTO chassisRequestDTO){
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
    public ResponseEntity<?> getAllChassis(){
        var baseResponseDTO = new BaseResponseDTO<List<CreateChassisRequestDTO>>();
        List<CreateChassisRequestDTO> listChassis = chassisRestService.getAllChassis();

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listChassis);
        baseResponseDTO.setMessage(String.format("List chassis berhasil ditemukan sebanyak %d chassis", listChassis.size()));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);

    }

    @GetMapping("/detail")
    public ResponseEntity<?> getChassisById(@RequestParam("id") String chassisId) {
        var baseResponseDTO = new BaseResponseDTO<CreateChassisRequestDTO>();
        try {
            CreateChassisRequestDTO chassisRequestDTO = chassisRestService.getChassisById(chassisId);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Chassis berhasil ditemukan!");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(chassisRequestDTO);
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
    public ResponseEntity<?> updateChassis(@Valid @RequestParam("id") String chassisId, 
                                        @Valid @RequestBody CreateChassisRequestDTO updateRequest) {
        var baseResponseDTO = new BaseResponseDTO<CreateChassisResponseDTO>();
        try {
            CreateChassisResponseDTO chassisResponseDTO = chassisRestService.updateChassis(chassisId, updateRequest);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Chassis berhasil di-update!");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(chassisResponseDTO);
            return ResponseEntity.ok(baseResponseDTO);  
        } catch (ValidationException e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponseDTO);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Terjadi kesalahan sistem!");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }

   
}

