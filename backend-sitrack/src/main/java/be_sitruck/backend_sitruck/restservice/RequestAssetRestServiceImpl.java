package be_sitruck.backend_sitruck.restservice;

import be_sitruck.backend_sitruck.model.Asset;
import be_sitruck.backend_sitruck.model.RequestAsset;
import be_sitruck.backend_sitruck.model.RequestAssetItem;
import be_sitruck.backend_sitruck.repository.AssetDb;
import be_sitruck.backend_sitruck.repository.RequestAssetDb;
import be_sitruck.backend_sitruck.repository.RequestAssetItemDb;
import be_sitruck.backend_sitruck.restdto.request.CreateRequestAssetRequestDTO;
import be_sitruck.backend_sitruck.restdto.request.RequestAssetItemDTO;
import be_sitruck.backend_sitruck.restdto.request.UpdateRequestAssetStatusDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateRequestAssetResponseDTO;
import be_sitruck.backend_sitruck.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RequestAssetRestServiceImpl implements RequestAssetRestService {

    @Autowired
    private RequestAssetDb requestAssetDb;

    @Autowired
    private AssetDb assetDb;

    @Autowired
    private RequestAssetItemDb requestAssetItemDb;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public CreateRequestAssetResponseDTO createRequestAsset(CreateRequestAssetRequestDTO requestDTO) {
        String currentUser = jwtUtils.getCurrentUsername();

        String maxId = requestAssetDb.findMaxRequestAssetId();
        int nextNumber = 1;
        if (maxId != null && maxId.length() > 2) {
            String numberPart = maxId.substring(2);
            try {
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                nextNumber = 1;
            }
        }
        String paddedNumber = String.format("%05d", nextNumber);
        String generatedRequestAssetId = "RA" + paddedNumber;

        RequestAsset requestAsset = new RequestAsset();
        requestAsset.setRequestAssetId(generatedRequestAssetId);
        requestAsset.setStatus(0);
        requestAsset.setCreatedBy(currentUser);
        requestAsset.setCreatedDate(new Date());
        requestAsset.setRequestRemark(requestDTO.getRequestRemark());

        requestAssetDb.save(requestAsset);

        List<RequestAssetItem> items = requestDTO.getAssets().stream().map(itemDTO -> {
            RequestAssetItem item = new RequestAssetItem();
            item.setRequestAsset(requestAsset);
            item.setAssetId(itemDTO.getAssetId());
            item.setRequestedQuantity(itemDTO.getRequestedQuantity());
            return item;
        }).collect(Collectors.toList());

        requestAssetItemDb.saveAll(items);

        return new CreateRequestAssetResponseDTO(
                requestAsset.getRequestAssetId(),
                "Request Asset berhasil dibuat!"
        );
    }

    @Override
    public List<CreateRequestAssetRequestDTO> getAllRequestAssets() {
        return requestAssetDb.findAll().stream()
                .map(this::convertToRequestAssetDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateRequestAssetStatus(String requestAssetId, UpdateRequestAssetStatusDTO request) {
        RequestAsset requestAsset = requestAssetDb.findById(requestAssetId)
            .orElseThrow(() -> new ValidationException("Request Asset tidak ditemukan"));
        
        String currentUser = jwtUtils.getCurrentUsername();
    
        requestAsset.setStatus(request.getStatus());
        requestAsset.setRequestRemark(request.getRequestRemark());
        requestAsset.setUpdatedBy(currentUser);
        requestAsset.setUpdatedDate(new Date());
    
        //Jika status APPROVED (1), tambah requestedstok dari asset terkait
        if (request.getStatus() == 1) {
            requestAsset.setApprovalBy(currentUser);
            requestAsset.setApprovalDate(new Date());
            List<RequestAssetItem> requestItems = requestAssetItemDb.findByRequestAssetRequestAssetId(requestAsset.getRequestAssetId());
            for (RequestAssetItem item : requestItems) {
                Asset asset = assetDb.findByAssetId(item.getAssetId());
                if (asset != null) {
                    int currentStok = asset.getRequestedStok() != null ? asset.getRequestedStok() : 0;
                    asset.setRequestedStok(currentStok + item.getRequestedQuantity());
                    assetDb.save(asset);
                }
            }
        }
    
        requestAssetDb.save(requestAsset);
    }
    

    @Override
    public CreateRequestAssetRequestDTO getRequestAssetById(String requestAssetId) {
        RequestAsset requestAsset = requestAssetDb.findById(requestAssetId)
            .orElseThrow(() -> new ValidationException("Request Asset tidak ditemukan"));

        return convertToRequestAssetDTO(requestAsset);
    }

    private CreateRequestAssetRequestDTO convertToRequestAssetDTO(RequestAsset requestAsset) {
        CreateRequestAssetRequestDTO dto = new CreateRequestAssetRequestDTO();
        dto.setRequestAssetId(requestAsset.getRequestAssetId());
        dto.setRequestRemark(requestAsset.getRequestRemark());
        dto.setStatus(requestAsset.getStatus());
        dto.setCreatedBy(requestAsset.getCreatedBy());
        dto.setCreatedDate(requestAsset.getCreatedDate());
        dto.setUpdatedBy(requestAsset.getUpdatedBy());
        dto.setUpdatedDate(requestAsset.getUpdatedDate());
        dto.setApprovalBy(requestAsset.getApprovalBy());
        dto.setApprovalDate(requestAsset.getApprovalDate());

        List<RequestAssetItemDTO> itemDTOs = requestAsset.getItems().stream()
            .map(this::convertToRequestAssetItemDTO)
            .collect(Collectors.toList());

        dto.setAssets(itemDTOs);
        return dto;
    }

    private RequestAssetItemDTO convertToRequestAssetItemDTO(RequestAssetItem item) {
    RequestAssetItemDTO dto = new RequestAssetItemDTO();
    dto.setAssetId(item.getAssetId());
    dto.setRequestedQuantity(item.getRequestedQuantity());

    Asset asset = assetDb.findByAssetId(item.getAssetId());
    if (asset != null) {
        dto.setJenisAsset(asset.getJenisAsset());
        dto.setBrand(asset.getBrand());
    }

    return dto;
}
}
