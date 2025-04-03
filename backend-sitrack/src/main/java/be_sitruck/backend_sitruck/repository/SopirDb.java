package be_sitruck.backend_sitruck.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import be_sitruck.backend_sitruck.model.SopirModel;
import java.util.List;


@Repository
public interface SopirDb  extends JpaRepository<SopirModel, String>{

    @Query("SELECT s FROM SopirModel s WHERE s.driver_KTP_No = :driver_KTP_No")
    SopirModel findByDriverKTPNo(@Param("driver_KTP_No") String driver_KTP_No);

    
}
