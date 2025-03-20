package be_sitruck.backend_sitruck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import be_sitruck.backend_sitruck.model.Customer;

@Repository
public interface CustomerDb extends JpaRepository<Customer, String> {
    List<Customer> findAll();

    @Query("SELECT c FROM Customer c WHERE c.siteId = ?1 ORDER BY c.id DESC")
    List<Customer> findBySiteIdOrderByIdDesc(String siteId);    
}
