package be_sitruck.backend_sitruck.restservice;

import be_sitruck.backend_sitruck.model.*;
import be_sitruck.backend_sitruck.repository.*;
import be_sitruck.backend_sitruck.restdto.request.ReportFilterRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class ReportRestServiceImpl implements ReportRestService {

    @Autowired
    private CustomerDb customerDb;

    @Autowired
    private TruckDb truckDb;

    @Autowired
    private ChassisDb chassisDb;

    @Autowired
    private SopirDb sopirDb;

    @Autowired
    private OrderDb orderDb;

    @Autowired
    private SpjDb spjDb;

    @Autowired
    private KomisiDb komisiDb;

    @Override
    public List<Customer> getAllCustomers() {
        return customerDb.findAll();
    }
    @Override
    public List<Truck> getAllVehicles() {
        return truckDb.findAll();
    }
    @Override
    public List<Chassis> getAllChassis() {
        return chassisDb.findAll();
    }
    @Override
    public List<SopirModel> getAllDrivers() {
        return sopirDb.findAll();
    }

    @Override
    public List<Komisi> getAllKomisi() {
        return komisiDb.findAll();
    }
    @Override
    public List<Order> getOrdersByDateRange(Date fromDate, Date endDate) {
        if (fromDate == null || endDate == null) {
            return orderDb.findAll();
        }
        return orderDb.findByOrderDateBetween(fromDate, endDate);
    }
    @Override
    public List<Spj> getSpjByDateRange(Date fromDate, Date endDate) {
        if (fromDate == null || endDate == null) {
            return spjDb.findAll();
        }
        return spjDb.findByDateOutBetween(fromDate, endDate);
    }
    @Override
    public Object generateReport(ReportFilterRequestDTO filter) {
        switch (filter.getReportType()) {
            case "ALL_CUSTOMERS":
                return getAllCustomers();
            case "ALL_VEHICLES":
                return getAllVehicles();
            case "ALL_CHASSIS":
                return getAllChassis();
            case "ALL_DRIVERS":
                return getAllDrivers();
            case "ALL_COMMISSIONS":
                return getAllKomisi();
            case "ALL_ORDERS":
                return getOrdersByDateRange(filter.getFromDate(), filter.getEndDate());
            case "ALL_SPJ":
                return getSpjByDateRange(filter.getFromDate(), filter.getEndDate());
            default:
                throw new IllegalArgumentException("Invalid report type");
        }
    }
} 