package be_sitruck.backend_sitruck.restservice;

import be_sitruck.backend_sitruck.model.*;
import be_sitruck.backend_sitruck.restdto.request.ReportFilterRequestDTO;
import java.util.Date;
import java.util.List;

public interface ReportRestService {
    List<Customer> getAllCustomers();
    List<Truck> getAllVehicles();
    List<Chassis> getAllChassis();
    List<SopirModel> getAllDrivers();
    List<Order> getOrdersByDateRange(Date fromDate, Date endDate);
    List<Spj> getSpjByDateRange(Date fromDate, Date endDate);
    Object generateReport(ReportFilterRequestDTO filter);
    List<Komisi> getAllKomisi();
} 