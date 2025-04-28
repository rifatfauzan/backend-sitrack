package be_sitruck.backend_sitruck.repository;

import be_sitruck.backend_sitruck.model.RequestAsset;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestAssetDb extends JpaRepository<RequestAsset, String> {

    @Query(value = "SELECT request_asset_id FROM request_asset ORDER BY CAST(SUBSTRING(request_asset_id, 4) AS INTEGER) DESC LIMIT 1", nativeQuery = true)
    String findMaxRequestAssetId();

    RequestAsset findByRequestAssetId(String requestAssetId);
    
    @Query("SELECT ra FROM RequestAsset ra ORDER BY CAST(SUBSTRING(ra.requestAssetId, 3) AS int) ASC")
    List<RequestAsset> findAllOrdered();
}