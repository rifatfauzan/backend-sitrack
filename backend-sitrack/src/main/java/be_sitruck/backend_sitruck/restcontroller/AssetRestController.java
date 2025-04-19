// File: be_sitruck.backend_sitruck.restcontroller.AssetRestController.java
package be_sitruck.backend_sitruck.restcontroller;

import be_sitruck.backend_sitruck.restdto.request.CreateAssetRequestDTO;
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

    // //Get Asset by ID
    // @GetMapping("/detail")
    // public ResponseEntity<?> getAssetById(@RequestParam("id") String assetId) {
    //     var baseResponseDTO = new BaseResponseDTO<CreateAssetRequestDTO>();
    //     try {
    //         CreateAssetRequestDTO asset = assetRestService.getAssetById(assetId);
    //         baseResponseDTO.setStatus(HttpStatus.OK.value());
    //         baseResponseDTO.setMessage("Asset berhasil ditemukan!");
    //         baseResponseDTO.setTimestamp(new Date());
    //         baseResponseDTO.setData(asset);
    //         return ResponseEntity.ok(baseResponseDTO);
    //     } catch (ValidationException e) {
    //         baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
    //         baseResponseDTO.setMessage("Asset tidak ditemukan dengan ID: " + assetId);
    //         baseResponseDTO.setTimestamp(new Date());
    //         baseResponseDTO.setData(null);
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponseDTO);
    //     } catch (Exception e) {
    //         baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    //         baseResponseDTO.setMessage("Terjadi kesalahan saat mengambil asset: " + e.getMessage());
    //         baseResponseDTO.setTimestamp(new Date());
    //         baseResponseDTO.setData(null);
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
    //     }
    // }
}
