// File: be_sitruck.backend_sitruck.restcontroller.AssetRestController.java
package be_sitruck.backend_sitruck.restcontroller;

import be_sitruck.backend_sitruck.restdto.request.CreateAssetRequestDTO;
import be_sitruck.backend_sitruck.restdto.request.CreateSopirRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateAssetResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.BaseResponseDTO;
import be_sitruck.backend_sitruck.restservice.AssetRestService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/asset")
public class AssetRestController {
    
    @Autowired
    private AssetRestService assetRestService;

    // Create Asset
    @PostMapping("/add")
    public ResponseEntity<?> createAsset(@Valid @RequestBody CreateAssetRequestDTO request) {
        var baseResponseDTO = new BaseResponseDTO<CreateAssetResponseDTO>();
        try {
            CreateAssetResponseDTO response = assetRestService.createAsset(request);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Asset berhasil ditambahkan!");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(response);
            return ResponseEntity.ok(baseResponseDTO);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Terjadi kesalahan saat menambahkan asset: " + e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }

    // Get All Assets
    @GetMapping("/all")
    public ResponseEntity<?> getAllAssets() {
        var baseResponseDTO = new BaseResponseDTO<List<CreateAssetRequestDTO>>();
        try {
            List<CreateAssetRequestDTO> assetList = assetRestService.getAllAssets();
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage(String.format("Berhasil mendapatkan %d asset", assetList.size()));
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(assetList);
            return ResponseEntity.ok(baseResponseDTO);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Terjadi kesalahan saat mengambil daftar asset");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }

    @GetMapping("/{assetId}")
    public ResponseEntity<?> viewAssetById(@PathVariable("assetId") String assetId){
        try{
            var response = new BaseResponseDTO<>();
            response.setData(assetRestService.getAssetById(assetId));
            response.setMessage("Data asset berhasil ditampilkan");
            response.setTimestamp(new Date());
            response.setStatus(200);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            var response = new BaseResponseDTO<>();
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            response.setStatus(400);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{assetId}")
    public ResponseEntity<?> updateAsset(@PathVariable("assetId") String assetId, @RequestBody CreateAssetRequestDTO requestDTO){
        try{
            var response = new BaseResponseDTO<>();
            response.setData(assetRestService.updateAsset(assetId, requestDTO));
            response.setMessage("Data Asset berhasil diupdate");
            response.setTimestamp(new Date());
            response.setStatus(200);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            var response = new BaseResponseDTO<>();
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            response.setStatus(400);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
