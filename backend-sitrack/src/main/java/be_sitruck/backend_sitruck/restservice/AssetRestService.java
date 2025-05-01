package be_sitruck.backend_sitruck.restservice;

import java.util.List;

import be_sitruck.backend_sitruck.restdto.request.CreateAssetRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateAssetResponseDTO;

public interface AssetRestService {
    CreateAssetResponseDTO createAsset(CreateAssetRequestDTO CreateAssetRequestDTO);
    List<CreateAssetRequestDTO> getAllAssets();
    CreateAssetRequestDTO getAssetById(String assetId);
    CreateAssetRequestDTO updateAsset(String assetId, CreateAssetRequestDTO assetDTO);
    
}
