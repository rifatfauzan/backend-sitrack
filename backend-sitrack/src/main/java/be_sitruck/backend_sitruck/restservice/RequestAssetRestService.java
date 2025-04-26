package be_sitruck.backend_sitruck.restservice;

import java.util.List;

import be_sitruck.backend_sitruck.restdto.request.CreateRequestAssetRequestDTO;
import be_sitruck.backend_sitruck.restdto.request.UpdateRequestAssetStatusDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateRequestAssetResponseDTO;

public interface RequestAssetRestService {
    CreateRequestAssetResponseDTO createRequestAsset(CreateRequestAssetRequestDTO requestDTO);
    List<CreateRequestAssetRequestDTO> getAllRequestAssets();
    void updateRequestAssetStatus(String requestAssetId, UpdateRequestAssetStatusDTO request);
    CreateRequestAssetRequestDTO getRequestAssetById(String requestAssetId);
}
