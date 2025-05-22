package be_sitruck.backend_sitruck.repository;

import be_sitruck.backend_sitruck.model.ReportTruck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportTruckDb extends JpaRepository<ReportTruck, String> {

    @Query(value = "SELECT report_truck_id FROM report_truck ORDER BY CAST(SUBSTRING(report_truck_id, 4) AS INTEGER) DESC LIMIT 1", nativeQuery = true)
    String findMaxReportTruckId();

    ReportTruck findByReportTruckId(String reportTruckId);

    @Query("SELECT rt FROM ReportTruck rt ORDER BY CAST(SUBSTRING(rt.reportTruckId, 3) AS int) ASC")
    List<ReportTruck> findAllOrdered();
}