package be_sitruck.backend_sitruck.repository;

import be_sitruck.backend_sitruck.model.RequestAssetItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestAssetItemDb extends JpaRepository<RequestAssetItem, Long> {
    List<RequestAssetItem> findByRequestAssetRequestAssetId(String requestAssetId);
}
