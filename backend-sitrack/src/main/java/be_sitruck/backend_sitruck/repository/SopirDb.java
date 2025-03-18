package be_sitruck.backend_sitruck.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import be_sitruck.backend_sitruck.model.SopirModel;

public interface SopirDb  extends JpaRepository<SopirModel, String>{
    
}
