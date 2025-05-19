package be_sitruck.backend_sitruck.restcontroller;

import be_sitruck.backend_sitruck.model.Truck;
import be_sitruck.backend_sitruck.restdto.request.CreateTruckRequestDTO;
import be_sitruck.backend_sitruck.restdto.request.UpdateTruckRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.BaseResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateTruckResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.UpdateTruckResponseDTO;
import be_sitruck.backend_sitruck.restservice.TruckRestService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/truck")
public class TruckRestController {

    @Autowired
    private TruckRestService truckRestService;

    // Handler khusus untuk validasi DTO gagal
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponseDTO<?>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        BaseResponseDTO<Object> resp = new BaseResponseDTO<>();
        String msg = ex.getBindingResult().getFieldError().getDefaultMessage();

        resp.setStatus(HttpStatus.BAD_REQUEST.value());
        resp.setMessage(msg);
        resp.setTimestamp(new Date());
        resp.setData(null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    // Create Truck
    @PostMapping("/add")
    public ResponseEntity<?> createTruck(@Valid @RequestBody CreateTruckRequestDTO request) {
        var baseResponseDTO = new BaseResponseDTO<CreateTruckResponseDTO>();
        try {
            CreateTruckResponseDTO response = truckRestService.createTruck(request);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Truck berhasil ditambahkan!");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(response);
            return ResponseEntity.ok(baseResponseDTO);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Terjadi kesalahan saat menambahkan truck: " + e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }

    // Get All Trucks
    @PreAuthorize("hasAnyAuthority('Admin', 'Supervisor', 'Manager', 'Operasional')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllTrucks() {
        var baseResponseDTO = new BaseResponseDTO<List<CreateTruckRequestDTO>>();
        try {
            List<CreateTruckRequestDTO> truckList = truckRestService.getAllTruck();
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage(String.format("Berhasil mendapatkan %d truck", truckList.size()));
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(truckList);
            return ResponseEntity.ok(baseResponseDTO);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Terjadi kesalahan saat mengambil daftar truck");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }

    //  Get Truck by ID
    @PreAuthorize("hasAnyAuthority('Admin', 'Supervisor', 'Manager', 'Operasional')")
    @GetMapping("/detail")
    public ResponseEntity<?> getTruckById(@RequestParam("id") String vehicleId) {
        var baseResponseDTO = new BaseResponseDTO<CreateTruckRequestDTO>();
        try {
            CreateTruckRequestDTO truck = truckRestService.getTruckById(vehicleId);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Truck berhasil ditemukan!");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(truck);
            return ResponseEntity.ok(baseResponseDTO);
        } catch (ValidationException e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage("Truck tidak ditemukan dengan ID: " + vehicleId);
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponseDTO);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Terjadi kesalahan saat mengambil truck: " + e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }

    //Update Truck
    @PutMapping("/update")
    public ResponseEntity<?> updateTruck(@RequestParam("id") String vehicleId,
                                         @Valid @RequestBody UpdateTruckRequestDTO request) {
        var baseResponseDTO = new BaseResponseDTO<UpdateTruckResponseDTO>();

        try {
            UpdateTruckResponseDTO response = truckRestService.updateTruck(vehicleId, request);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Truck berhasil diperbarui!");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(response);
            return ResponseEntity.ok(baseResponseDTO);
        } catch (ValidationException e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage("Validasi gagal: " + e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponseDTO);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }
}