package be_sitruck.backend_sitruck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import be_sitruck.backend_sitruck.model.Asset;

@Repository
public interface AssetDb extends JpaRepository<Asset, String> {
    Asset findByAssetId(String assetId);
    @Query(value = "SELECT asset_id FROM asset ORDER BY CAST(SUBSTRING(asset_id, LENGTH(asset_id) - 4, 5) AS INTEGER) DESC LIMIT 1", nativeQuery = true)
    String findMaxAssetId();
    @Query("SELECT a FROM Asset a ORDER BY CAST(SUBSTRING(a.assetId, 3) AS int) ASC")
    List<Asset> findAllOrdered();
}

