package be_sitruck.backend_sitruck.restcontroller;

import be_sitruck.backend_sitruck.restdto.request.CreateRequestAssetRequestDTO;
import be_sitruck.backend_sitruck.restdto.request.UpdateRequestAssetStatusDTO;
import be_sitruck.backend_sitruck.restdto.response.BaseResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateRequestAssetResponseDTO;
import be_sitruck.backend_sitruck.restservice.RequestAssetRestService;
import be_sitruck.backend_sitruck.restdto.response.CreateRequestAssetResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/request-assets")
public class RequestAssetRestController {

    @Autowired
    private RequestAssetRestService requestAssetRestService;

    // CREATE Request Asset
    @PostMapping("/add")
    public ResponseEntity<?> createRequestAsset(@Valid @RequestBody CreateRequestAssetRequestDTO requestDTO) {
        BaseResponseDTO<CreateRequestAssetResponseDTO> baseResponse = new BaseResponseDTO<>();
        try {
            CreateRequestAssetResponseDTO response = requestAssetRestService.createRequestAsset(requestDTO);
            baseResponse.setStatus(HttpStatus.OK.value());
            baseResponse.setMessage("Request Asset berhasil dibuat!");
            baseResponse.setTimestamp(new Date());
            baseResponse.setData(response);
            return ResponseEntity.ok(baseResponse);
        } catch (ValidationException e) {
            baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponse.setMessage("Validasi gagal: " + e.getMessage());
            baseResponse.setTimestamp(new Date());
            baseResponse.setData(null);
            return ResponseEntity.badRequest().body(baseResponse);
        } catch (Exception e) {
            baseResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponse.setMessage("Terjadi kesalahan: " + e.getMessage());
            baseResponse.setTimestamp(new Date());
            baseResponse.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // VIEW ALL Request Assets
    @GetMapping("/all")
    public ResponseEntity<?> getAllRequestAssets() {
        BaseResponseDTO<List<CreateRequestAssetRequestDTO>> baseResponse = new BaseResponseDTO<>();
        try {
            List<CreateRequestAssetRequestDTO> responseList = requestAssetRestService.getAllRequestAssets();
            baseResponse.setStatus(HttpStatus.OK.value());
            baseResponse.setMessage(String.format("Berhasil mendapatkan %d Request Assets", responseList.size()));
            baseResponse.setTimestamp(new Date());
            baseResponse.setData(responseList);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            baseResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponse.setMessage("Terjadi kesalahan: " + e.getMessage());
            baseResponse.setTimestamp(new Date());
            baseResponse.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // GET Detail Request Asset by ID
    @GetMapping("/detail")
    public ResponseEntity<?> getRequestAssetDetail(@RequestParam("id") String requestAssetId) {
        BaseResponseDTO<CreateRequestAssetRequestDTO> baseResponse = new BaseResponseDTO<>();
        try {
            CreateRequestAssetRequestDTO detail = requestAssetRestService.getRequestAssetById(requestAssetId);
            baseResponse.setStatus(HttpStatus.OK.value());
            baseResponse.setMessage("Berhasil mendapatkan detail Request Asset");
            baseResponse.setTimestamp(new Date());
            baseResponse.setData(detail);
            return ResponseEntity.ok(baseResponse);
        } catch (ValidationException e) {
            baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponse.setMessage("Validasi gagal: " + e.getMessage());
            baseResponse.setTimestamp(new Date());
            baseResponse.setData(null);
            return ResponseEntity.badRequest().body(baseResponse);
        } catch (Exception e) {
            baseResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponse.setMessage("Terjadi kesalahan: " + e.getMessage());
            baseResponse.setTimestamp(new Date());
            baseResponse.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    // Update Approval
    @PutMapping("/approve")
    public ResponseEntity<BaseResponseDTO<String>> approveRequestAsset(
        @RequestParam("id") String requestAssetId,
        @RequestBody UpdateRequestAssetStatusDTO request) {
        
        requestAssetRestService.updateRequestAssetStatus(requestAssetId, request);
        
        BaseResponseDTO<String> response = new BaseResponseDTO<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Status berhasil diperbarui");
        response.setTimestamp(new Date());
        response.setData("Success");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/edit")
    public ResponseEntity<BaseResponseDTO<String>> editRequestAsset(
        @RequestParam("id") String requestAssetId,
        @Valid @RequestBody CreateRequestAssetRequestDTO requestDTO) {
        
        requestAssetRestService.editRequestAsset(requestAssetId, requestDTO);

        BaseResponseDTO<String> response = new BaseResponseDTO<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Request Asset berhasil diupdate");
        response.setTimestamp(new Date());
        response.setData("Success");

        return ResponseEntity.ok(response);
    }

}
