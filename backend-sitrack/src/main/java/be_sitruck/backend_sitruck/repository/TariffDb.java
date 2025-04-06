package be_sitruck.backend_sitruck.repository;

import be_sitruck.backend_sitruck.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TariffDb extends JpaRepository<Tariff, String> {
    List<Tariff> findByCustomerId(String customerId);
}
