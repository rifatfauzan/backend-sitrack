package be_sitruck.backend_sitruck.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be_sitruck.backend_sitruck.model.Customer;
import be_sitruck.backend_sitruck.model.Order;
import be_sitruck.backend_sitruck.model.Spj;
import be_sitruck.backend_sitruck.model.Truck;

@Repository
public interface SpjDb extends JpaRepository<Spj, String> {
    List<Spj> findAll();
    List<Spj> findByStatusIn(List<Integer> status);
    Optional<Spj> findFirstByIdStartingWithOrderByIdDesc(String prefix);
    List<Spj> findByOrderAndStatusIn(Order order, List<Integer> status);
    List<Spj> findByDateOutBetween(Date fromDate, Date endDate);
    List<Spj> findByCustomer(Customer customer);
    List<Spj> findByVehicleAndCustomer_CityDestination(Truck vehicle, String cityDestination);
}
