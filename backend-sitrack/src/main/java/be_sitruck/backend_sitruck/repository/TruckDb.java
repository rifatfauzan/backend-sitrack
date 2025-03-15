package be_sitruck.backend_sitruck.repository;

import be_sitruck.backend_sitruck.model.Truck;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TruckDb extends JpaRepository<Truck, String> {
    Truck findByVehicleId(String vechicleId);

    boolean existsByVehiclePlateNo(String vehiclePlateNo);
    boolean existsByVehicleKIRNo(String vehicleKIRNo);
    
}
