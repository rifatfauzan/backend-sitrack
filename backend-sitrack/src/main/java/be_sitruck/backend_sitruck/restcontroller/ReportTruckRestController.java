package be_sitruck.backend_sitruck.restcontroller;

import be_sitruck.backend_sitruck.restdto.request.ReportTruckRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.BaseResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.ReportTruckResponseDTO;
import be_sitruck.backend_sitruck.restservice.ReportTruckRestService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/report-truck")
@RequiredArgsConstructor
public class ReportTruckRestController {

    private final ReportTruckRestService reportTruckRestService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponseDTO<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BaseResponseDTO<Object> resp = new BaseResponseDTO<>();
        String msg = ex.getBindingResult().getFieldError().getDefaultMessage();
        resp.setStatus(HttpStatus.BAD_REQUEST.value());
        resp.setMessage(msg);
        resp.setTimestamp(new Date());
        resp.setData(null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    // Create Report Truck
    @PostMapping("/add")
    public ResponseEntity<?> createReportTruck(@Valid @RequestBody ReportTruckRequestDTO request) {
        var baseResponseDTO = new BaseResponseDTO<ReportTruckResponseDTO>();
        try {
            ReportTruckResponseDTO response = reportTruckRestService.createReportTruck(request);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Laporan perawatan berhasil ditambahkan!");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(response);
            return ResponseEntity.ok(baseResponseDTO);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Terjadi kesalahan saat menambahkan laporan: " + e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }

    // Get All Report Trucks
    @GetMapping("/all")
    public ResponseEntity<?> getAllReportTrucks() {
        var baseResponseDTO = new BaseResponseDTO<List<ReportTruckRequestDTO>>();
        try {
            List<ReportTruckRequestDTO> reportList = reportTruckRestService.getAllReportTrucks();
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage(String.format("Berhasil mendapatkan %d laporan", reportList.size()));
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(reportList);
            return ResponseEntity.ok(baseResponseDTO);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Terjadi kesalahan saat mengambil daftar laporan");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }
}
