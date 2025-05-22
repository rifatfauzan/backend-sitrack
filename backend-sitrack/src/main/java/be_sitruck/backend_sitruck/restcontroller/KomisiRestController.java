package be_sitruck.backend_sitruck.restcontroller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be_sitruck.backend_sitruck.restdto.request.CreateKomisiRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.BaseResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateKomisiResponseDTO;
import be_sitruck.backend_sitruck.restservice.KomisiRestService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/komisi")
public class KomisiRestController {

    @Autowired
    private KomisiRestService komisiRestService;

    @PostMapping("/add")
    public ResponseEntity<?> addKomisi(@Valid @RequestBody CreateKomisiRequestDTO requestDTO) {
        BaseResponseDTO<CreateKomisiResponseDTO> response = new BaseResponseDTO<>();
        try {
            CreateKomisiResponseDTO result = komisiRestService.addKomisi(requestDTO);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Komisi berhasil ditambahkan");
            response.setTimestamp(new Date());
            response.setData(result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllKomisi() {
        BaseResponseDTO<List<CreateKomisiResponseDTO>> response = new BaseResponseDTO<>();
        try {
            List<CreateKomisiResponseDTO> result = komisiRestService.getAllKomisi();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Berhasil mendapatkan semua komisi");
            response.setTimestamp(new Date());
            response.setData(result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @GetMapping("/detail/{komisiId}")
    public ResponseEntity<?> getKomisiById(@PathVariable("komisiId") String komisiId) {
        BaseResponseDTO<CreateKomisiResponseDTO> response = new BaseResponseDTO<>();
        try {
            CreateKomisiResponseDTO result = komisiRestService.getKomisiById(komisiId);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Berhasil mendapatkan detail komisi");
            response.setTimestamp(new Date());
            response.setData(result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PutMapping("/update/{komisiId}")
    public ResponseEntity<?> updateKomisi(@PathVariable("komisiId") String komisiId, @Valid @RequestBody CreateKomisiRequestDTO requestDTO) {
        BaseResponseDTO<CreateKomisiResponseDTO> response = new BaseResponseDTO<>();
        try {
            CreateKomisiResponseDTO result = komisiRestService.updateKomisi(komisiId, requestDTO);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Komisi berhasil diperbarui");
            response.setTimestamp(new Date());
            response.setData(result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
