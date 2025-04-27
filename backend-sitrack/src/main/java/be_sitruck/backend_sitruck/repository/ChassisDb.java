package be_sitruck.backend_sitruck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import be_sitruck.backend_sitruck.model.Chassis;

@Repository
public interface ChassisDb extends JpaRepository<Chassis, String> {
        Chassis findByChassisKIRNo(String chassisKIRNo);
        Chassis findByChassisId(String chassisId);
        
        @Query("SELECT c FROM Chassis c WHERE c.chassisId LIKE CONCAT(:siteId, '%') ORDER BY c.chassisId DESC LIMIT 1")
        Chassis findTopByChassisIdStartingWithOrderByChassisIdDesc(@Param("siteId") String siteId);

}
