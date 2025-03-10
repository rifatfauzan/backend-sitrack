package be_sitruck.backend_sitruck.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be_sitruck.backend_sitruck.model.Chassis;
import be_sitruck.backend_sitruck.model.UserModel;

@Repository
public interface ChassisDb extends JpaRepository<Chassis, String> {
        Chassis findByChassisKIRNo(String chassisKIRNo);
}
