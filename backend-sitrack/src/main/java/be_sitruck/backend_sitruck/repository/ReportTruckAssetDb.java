package be_sitruck.backend_sitruck.repository;

import be_sitruck.backend_sitruck.model.ReportTruckAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportTruckAssetDb extends JpaRepository<ReportTruckAsset, Long> {
    List<ReportTruckAsset> findByReportTruckReportTruckId(String reportTruckId);
}