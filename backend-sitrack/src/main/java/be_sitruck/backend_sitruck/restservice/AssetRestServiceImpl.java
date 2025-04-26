package be_sitruck.backend_sitruck.restservice;

import be_sitruck.backend_sitruck.model.Asset;
import be_sitruck.backend_sitruck.repository.AssetDb;
import be_sitruck.backend_sitruck.restdto.request.CreateAssetRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateAssetResponseDTO;
import be_sitruck.backend_sitruck.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetRestServiceImpl implements AssetRestService {

    @Autowired
    private AssetDb assetDb;

    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    @Override
    public CreateAssetResponseDTO createAsset(CreateAssetRequestDTO request) {
        if (request.getJenisAsset() == null || request.getJenisAsset().isBlank()) {
            throw new ValidationException("Jenis Asset harus diisi");
        }

        if (request.getJumlahStok() == null || request.getJumlahStok() < 0) {
            throw new ValidationException("Jumlah Stok harus diisi dan tidak boleh negatif");
        }

        if (request.getBrand() == null || request.getBrand().isBlank()) {
            throw new ValidationException("Brand harus diisi");
        }

        String currentUser = jwtUtils.getCurrentUsername();
        String maxAssetId = assetDb.findMaxAssetId(); 

        int nextNumber = 1;
        if (maxAssetId != null && maxAssetId.length() > 2) {
            String numberPart = maxAssetId.substring(2); // Ambil 5 digit terakhir
            try {
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                nextNumber = 1;
            }
        }

        String paddedNumber = String.format("%05d", nextNumber);
        String generatedAssetId = "AS" + paddedNumber;

        Asset asset = new Asset();
        asset.setAssetId(generatedAssetId);
        asset.setJenisAsset(request.getJenisAsset());
        asset.setJumlahStok(request.getJumlahStok());
        asset.setBrand(request.getBrand());
        asset.setAssetRemark(request.getAssetRemark());
        asset.setRequestedStok(0); // Requested stok default 0 saat create
        asset.setCreatedBy(currentUser);
        asset.setCreatedDate(new Date());

        assetDb.save(asset);

        return new CreateAssetResponseDTO(
                asset.getAssetId(),
                "Asset berhasil ditambahkan"
        );
    }

    @Override
    public List<CreateAssetRequestDTO> getAllAssets() {
        return assetDb.findAllOrdered().stream()
                .map(this::convertToAssetDTO)
                .collect(Collectors.toList());
    }

    private CreateAssetRequestDTO convertToAssetDTO(Asset asset) {
        CreateAssetRequestDTO dto = new CreateAssetRequestDTO();
        dto.setAssetId(asset.getAssetId());
        dto.setJenisAsset(asset.getJenisAsset());
        dto.setJumlahStok(asset.getJumlahStok());
        dto.setBrand(asset.getBrand());
        dto.setAssetRemark(asset.getAssetRemark());
        dto.setRequestedStok(asset.getRequestedStok());
        dto.setCreatedBy(asset.getCreatedBy());
        dto.setCreatedDate(asset.getCreatedDate());
        dto.setUpdatedBy(asset.getUpdatedBy());
        dto.setUpdatedDate(asset.getUpdatedDate());
        return dto;
    }

    @Override
    public CreateAssetRequestDTO getAssetById(String assetId) {
        Asset asset = assetDb.findById(assetId).orElse(null);
        if (asset == null) {
            return null; 
        }
        return convertToAssetDTO(asset);
    }

    @Override
    public CreateAssetRequestDTO updateAsset(String assetId, CreateAssetRequestDTO assetDTO) {
        Asset asset = assetDb.findById(assetId).orElse(null);
        if (asset == null) {
            throw new IllegalArgumentException("Asset dengan ID: " + assetId + " tidak ditemukan"); 
        }

            asset.setJenisAsset(assetDTO.getJenisAsset());
            asset.setJumlahStok(assetDTO.getJumlahStok());
            asset.setBrand(assetDTO.getBrand());
            asset.setAssetRemark(assetDTO.getAssetRemark());

        asset.setUpdatedBy(jwtUtils.getCurrentUsername());
        asset.setUpdatedDate(new Date());

        assetDb.save(asset);

        return convertToAssetDTO(asset);
    }
}