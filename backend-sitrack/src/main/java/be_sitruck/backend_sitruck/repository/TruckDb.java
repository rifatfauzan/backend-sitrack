package be_sitruck.backend_sitruck.repository;

import be_sitruck.backend_sitruck.model.Truck;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TruckDb extends JpaRepository<Truck, String> {
    Truck findByVehicleId(String vechicleId);

    boolean existsByVehiclePlateNo(String vehiclePlateNo);
    boolean existsByVehicleKIRNo(String vehicleKIRNo);
    @Query(value = "SELECT vehicle_id FROM truck ORDER BY CAST(SUBSTRING(vehicle_id, LENGTH(vehicle_id) - 4, 5) AS INTEGER) DESC LIMIT 1", nativeQuery = true)
    String findMaxVehicleId();
    @Query("SELECT t FROM Truck t ORDER BY CAST(SUBSTRING(t.vehicleId, 3) AS int) ASC")
    List<Truck> findAllOrdered();
    Truck findByVehicleBizLicenseNo(String vehicleBizLicenseNo);
    Truck findByVehicleDispensationNo(String vehicleDispensationNo);
    Truck findByVehicleNumber(String vehicleNumber);
}
