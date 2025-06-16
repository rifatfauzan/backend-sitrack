package be_sitruck.backend_sitruck.restcontroller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be_sitruck.backend_sitruck.model.Spj;
import be_sitruck.backend_sitruck.restdto.request.ApproveSpjRequestDTO;
import be_sitruck.backend_sitruck.restdto.request.CreateSpjRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.BaseResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.SpjResponseDTO;
import be_sitruck.backend_sitruck.restservice.SpjRestService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import be_sitruck.backend_sitruck.util.ExcelExporter;
import be_sitruck.backend_sitruck.util.PDFExporter;


@RestController
@RequestMapping("/api/spj")
public class SpjRestController {
    
    @Autowired
    private SpjRestService spjRestService;

    @Autowired
    private ExcelExporter excelExporter;

    @Autowired
    private PDFExporter pdfExporter;

    @PostMapping("/add")
    public ResponseEntity<?> addSpj(@RequestBody CreateSpjRequestDTO spjRequestDTO) {
        var response = new BaseResponseDTO<SpjResponseDTO>();
        try {
            SpjResponseDTO result = spjRestService.addSpj(spjRequestDTO);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("SPJ berhasil ditambahkan!");
            response.setTimestamp(new Date());
            response.setData(result);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            response.setData(null);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/vehicle-out")
    public ResponseEntity<?> getAllSpjVehicleOut() {
        var response = new BaseResponseDTO<List<SpjResponseDTO>>();
        try {
            List<SpjResponseDTO> listSpj = spjRestService.getAllVehicleOut();
            response.setStatus(HttpStatus.OK.value());
            response.setData(listSpj);
            response.setMessage(String.format("List SPJ berhasil ditemukan sebanyak %d user", listSpj.size()));
            response.setTimestamp(new Date());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            response.setData(null);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/vehicle-in")
    public ResponseEntity<?> getAllSpjVehicleIn() {
        var response = new BaseResponseDTO<List<SpjResponseDTO>>();
        try {
            List<SpjResponseDTO> listSpj = spjRestService.getAllVehicleIn();
            response.setStatus(HttpStatus.OK.value());
            response.setData(listSpj);
            response.setMessage(String.format("List SPJ berhasil ditemukan sebanyak %d user", listSpj.size()));
            response.setTimestamp(new Date());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            response.setData(null);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDetailSpj(@PathVariable("id") String id) {
        var response = new BaseResponseDTO<SpjResponseDTO>();
        try {
            SpjResponseDTO spjResponseDTO = spjRestService.getSpjById(id);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("SPJ berhasil ditemukan!");
            response.setTimestamp(new Date());
            response.setData(spjResponseDTO);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            response.setData(null);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/available/{orderId}")
    public ResponseEntity<?> getAvailableChassisAndContainers(@PathVariable("orderId") String orderId) {
        var response = new BaseResponseDTO<Map<String, Integer>>();
        try {
            Map<String, Integer> availableData = spjRestService.getAvailableChassisAndContainers(orderId);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Available chassis dan container berhasil ditemukan");
            response.setTimestamp(new Date());
            response.setData(availableData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            response.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/approve")
    public ResponseEntity<?> approveSpj(@RequestBody ApproveSpjRequestDTO request) {
        var response = new BaseResponseDTO<SpjResponseDTO>();
        try {
            SpjResponseDTO updatedSpj = spjRestService.approveSpj(request);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Approval SPJ berhasil diubah!");
            response.setTimestamp(new Date());
            response.setData(updatedSpj);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            response.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/done/{id}")
    public ResponseEntity<?> markSpjAsDone(@PathVariable("id") String id) {
        var response = new BaseResponseDTO<String>();
        try {
            spjRestService.markSpjAsDone(id);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Aktivitas SPJ berhasil diselesaikan!");
            response.setTimestamp(new Date());
            response.setData("Success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            response.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update/{spjId}")
    public ResponseEntity<?> updateSpj(@PathVariable("spjId") String spjId, @RequestBody CreateSpjRequestDTO requestDTO) {
        try {
            var response = new BaseResponseDTO<>();
            response.setData(spjRestService.updateSPJ(spjId, requestDTO));
            response.setMessage("Data SPJ berhasil diperbarui");
            response.setTimestamp(new Date());
            response.setStatus(200);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            var response = new BaseResponseDTO<>();
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            response.setStatus(400);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/export/excel/{id}")
    public ResponseEntity<?> exportSpjToExcel(@PathVariable("id") String id) {
        try {
            SpjResponseDTO spjDto = spjRestService.getSpjById(id);

            if (spjDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SPJ tidak ditemukan");
            }

            Spj spjEntity = spjRestService.getSpjEntityById(id);

            if (spjEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SPJ tidak ditemukan");
            }

            byte[] excelBytes = excelExporter.exportSpjToExcel(List.of(spjEntity));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=spj-" + id + ".xlsx");

            return ResponseEntity.ok().headers(headers).body(excelBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/export/pdf/{id}")
    public ResponseEntity<?> exportSpjToPdf(@PathVariable("id") String id) {
        try {
            SpjResponseDTO spjDto = spjRestService.getSpjById(id);

            if (spjDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SPJ tidak ditemukan");
            }

            Spj spjEntity = spjRestService.getSpjEntityById(id);

            if (spjEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SPJ tidak ditemukan");
            }

            byte[] pdfBytes = pdfExporter.exportSpjToPDF(List.of(spjEntity), null, null);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=spj-" + id + ".pdf");
            
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
