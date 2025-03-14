package be_sitruck.backend_sitruck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import be_sitruck.backend_sitruck.model.Customer;

@Repository
public interface CustomerDb extends JpaRepository<Customer, String> {
    List<Customer> findAll();
}
