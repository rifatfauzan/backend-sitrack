package be_sitruck.backend_sitruck.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be_sitruck.backend_sitruck.model.Komisi;

@Repository
public interface KomisiDb extends JpaRepository<Komisi, String> {
    Komisi findByKomisiId(String komisiId);

    Komisi findTopByKomisiIdStartingWithOrderByKomisiIdDesc(String prefix);
    boolean existsByTruck_VehicleIdAndLocation(String vehicleId, String location);
    Komisi findByTruck_VehicleIdAndLocation(String vehicleId, String location);

    
}
