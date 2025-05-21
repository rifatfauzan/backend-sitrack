package be_sitruck.backend_sitruck.restservice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be_sitruck.backend_sitruck.model.Customer;
import be_sitruck.backend_sitruck.model.Order;
import be_sitruck.backend_sitruck.model.Tariff;
import be_sitruck.backend_sitruck.repository.CustomerDb;
import be_sitruck.backend_sitruck.repository.OrderDb;
import be_sitruck.backend_sitruck.repository.TariffDb;
import be_sitruck.backend_sitruck.restdto.request.CreateCustomerRequestDTO;
import be_sitruck.backend_sitruck.restdto.request.TariffRequestDTO;
import be_sitruck.backend_sitruck.restdto.request.UpdateCustomerRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.CustomerResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.TariffResponseDTO;
import be_sitruck.backend_sitruck.security.jwt.JwtUtils;

@Service
public class CustomerRestServiceImpl implements CustomerRestService{
    
    @Autowired
    private CustomerDb customerDb;

    @Autowired
    private TariffDb tariffDb;

    @Autowired
    private OrderDb orderDb;

    @Autowired
    private JwtUtils jwtUtils;

    private String generateCustomerId() {
        List<Customer> allCustomers = customerDb.findAll();
        if (!allCustomers.isEmpty()) {
            allCustomers.sort((a, b) -> b.getId().compareTo(a.getId()));
            String lastCustomerId = allCustomers.get(0).getId();
            int lastNumber = Integer.parseInt(lastCustomerId.substring(2));
            return "CU" + String.format("%05d", lastNumber + 1);
        } else {
            return "CU00001";
        }
    }    

    private String generateTariffId(String customerId, int chassisSize, String moveType) {
        return customerId + "-" + chassisSize + "-" + moveType;
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomer() {
        var customers = customerDb.findAll();
        var customerResponseDTO = new ArrayList<CustomerResponseDTO>();
        customers.forEach(customer -> {
            customerResponseDTO.add(CustomerToCustomerResponseDTO(customer));
        });

        return customerResponseDTO;
    }

    @Override
    public CustomerResponseDTO getCustomerById(String id) {
        var customer = customerDb.findById(id).orElse(null);

        if (customer == null) {
            return null;
        }

        return CustomerToCustomerResponseDTO(customer);
    }

    @Override
    public CustomerResponseDTO addCustomer(CreateCustomerRequestDTO customerDTO) {
        String currentUser = jwtUtils.getCurrentUsername();

        Optional<Customer> existingCustomer = customerDb.findAll().stream()
            .filter(c -> c.getName().equalsIgnoreCase(customerDTO.getName()) &&
                         c.getCityDestination().equalsIgnoreCase(customerDTO.getCityDestination()))
            .findFirst();

        if (existingCustomer.isPresent()) {
            throw new RuntimeException("Customer dengan nama '" + customerDTO.getName().toUpperCase() + "' dan destinasi '" 
                                       + customerDTO.getCityDestination().toUpperCase() + "' sudah ada!");
        }

        if (customerDTO.getName().isEmpty() || customerDTO.getCityDestination().isEmpty() || customerDTO.getSiteId().isEmpty()) {
            throw new RuntimeException("Nama customer, kota tujuan, dan Site ID wajib diisi.");
        }
        
        var customer = new Customer();
        customer.setId(generateCustomerId());
        customer.setSiteId(customerDTO.getSiteId().toUpperCase());
        customer.setName(customerDTO.getName().toUpperCase());
        customer.setAddress(customerDTO.getAddress().toUpperCase());
        customer.setContractNo(customerDTO.getContractNo());
        customer.setCityOrigin(customerDTO.getCityOrigin().toUpperCase());
        customer.setCityDestination(customerDTO.getCityDestination().toUpperCase());
        customer.setCommodity(customerDTO.getCommodity().toUpperCase());
        customer.setCommission(customerDTO.getCommission());
        customer.setTariffs(new ArrayList<>());
        customer.setInsertedBy(currentUser);
        customer.setInsertedDate(new Date());
        customerDb.save(customer);

        return CustomerToCustomerResponseDTO(customer);
    }

    @Override
    public CustomerResponseDTO updateCustomer(String id, UpdateCustomerRequestDTO customerDTO) {
        String currentUser = jwtUtils.getCurrentUsername();        
        var customer = customerDb.findById(id).orElseThrow(() -> new RuntimeException("Customer tidak ditemukan"));

        Optional<Customer> existingCustomer = customerDb.findAll().stream()
            .filter(c -> !c.getId().equals(id))
            .filter(c -> c.getName().equalsIgnoreCase(customerDTO.getName()) &&
                         c.getCityDestination().equalsIgnoreCase(customerDTO.getCityDestination()))
            .findFirst();

        if (existingCustomer.isPresent()) {
            throw new RuntimeException("Customer dengan nama '" + customerDTO.getName().toUpperCase() + "' dan destinasi '" 
                                    + customerDTO.getCityDestination().toUpperCase() + "' sudah ada!");
        }

        if (customerDTO.getName().isEmpty() || customerDTO.getCityDestination().isEmpty() || customerDTO.getSiteId().isEmpty()) {
            throw new RuntimeException("Nama customer, kota tujuan, dan Site ID wajib diisi.");
        }    

        customer.setSiteId(customerDTO.getSiteId().toUpperCase());
        customer.setName(customerDTO.getName().toUpperCase());
        customer.setAddress(customerDTO.getAddress().toUpperCase());
        customer.setContractNo(customerDTO.getContractNo());
        customer.setCityOrigin(customerDTO.getCityOrigin().toUpperCase());
        customer.setCityDestination(customerDTO.getCityDestination().toUpperCase());
        customer.setCommodity(customerDTO.getCommodity().toUpperCase());
        customer.setCommission(customerDTO.getCommission());
        customer.setUpdatedBy(currentUser);
        customer.setUpdatedDate(new Date());

        List<Tariff> existingTariffs = tariffDb.findByCustomerId(id);

        if (customerDTO.getTariffs().isEmpty()) {
            tariffDb.deleteAll(existingTariffs);
            customer.setTariffs(new ArrayList<>());
        } else {
            Set<String> seenCombinations = new HashSet<>();
            for (TariffRequestDTO tariffDTO : customerDTO.getTariffs()) {
                int chassisSize = tariffDTO.getChassisSize();
                String moveTypeUpper = tariffDTO.getMoveType().toUpperCase();
                String combinationKey = chassisSize + "-" + moveTypeUpper;

                if (!seenCombinations.add(combinationKey)) {
                    throw new RuntimeException("Kombinasi chasis size '" + chassisSize + "' dan move type '" + moveTypeUpper + "' tidak boleh duplikat.");
                }
            }
    
            tariffDb.deleteAll(existingTariffs);
            List<Tariff> newTariffs = new ArrayList<>();
            for (TariffRequestDTO tariffDTO : customerDTO.getTariffs()) {
                Tariff newTariff = new Tariff();
                newTariff.setTariffId(generateTariffId(customer.getId(), tariffDTO.getChassisSize(), tariffDTO.getMoveType().toUpperCase()));
                newTariff.setCustomer(customer);
                newTariff.setChassisSize(tariffDTO.getChassisSize());
                newTariff.setMoveType(tariffDTO.getMoveType().toUpperCase());
                newTariff.setStdTariff(tariffDTO.getStdTariff());
                newTariff.setInsurance(tariffDTO.getInsurance());
                newTariff.setTips(tariffDTO.getTips());
                newTariff.setPolice(tariffDTO.getPolice());
                newTariff.setLolo(tariffDTO.getLolo());
                newTariff.setOthers(tariffDTO.getOthers());
                newTariff.setTotalTariff(
                    tariffDTO.getStdTariff() + tariffDTO.getInsurance() + tariffDTO.getTips() +
                    tariffDTO.getPolice() + tariffDTO.getLolo() + tariffDTO.getOthers()
                );
                newTariffs.add(newTariff);
            }
        
            tariffDb.saveAll(newTariffs);
            customer.setTariffs(newTariffs);
        }
        customerDb.save(customer);
        return CustomerToCustomerResponseDTO(customer);
    }

    private CustomerResponseDTO CustomerToCustomerResponseDTO(Customer customer) {
        var customerDTO = new CustomerResponseDTO();
        
        customerDTO.setId(customer.getId());
        customerDTO.setSiteId(customer.getSiteId());
        customerDTO.setName(customer.getName());
        customerDTO.setAddress(customer.getAddress());
        customerDTO.setContractNo(customer.getContractNo());
        customerDTO.setCityOrigin(customer.getCityOrigin());
        customerDTO.setCityDestination(customer.getCityDestination());
        customerDTO.setCommodity(customer.getCommodity());
        customerDTO.setCommission(customer.getCommission());

        List<TariffResponseDTO> listTariffResponse = new ArrayList<>();
        for (Tariff tariff : customer.getTariffs()) {
            var tariffResponse = new TariffResponseDTO();
            tariffResponse.setTariffId(tariff.getTariffId());
            tariffResponse.setCustomerId(tariff.getCustomer().getId());
            tariffResponse.setChassisSize(tariff.getChassisSize());
            tariffResponse.setMoveType(tariff.getMoveType());
            tariffResponse.setStdTariff(tariff.getStdTariff());
            tariffResponse.setInsurance(tariff.getInsurance());
            tariffResponse.setTips(tariff.getTips());
            tariffResponse.setPolice(tariff.getPolice());
            tariffResponse.setLolo(tariff.getLolo());
            tariffResponse.setOthers(tariff.getOthers());
            tariffResponse.setTotalTariff(tariff.getTotalTariff());
            listTariffResponse.add(tariffResponse);
        }
        customerDTO.setTariffs(listTariffResponse);
        customerDTO.setInsertedBy(customer.getInsertedBy());
        customerDTO.setUpdatedBy(customer.getUpdatedBy());
        customerDTO.setInsertedDate(customer.getInsertedDate());
        customerDTO.setUpdatedDate(customer.getUpdatedDate());

        return customerDTO;
    }

    // Tambahkan di CustomerRestServiceImpl.java

@Override
public List<Map<String, Object>> getCustomerTransactionStats(int year) {
    List<Order> doneOrders = orderDb.findByOrderStatus(4); // 4 = Done

    Map<String, Integer> customerCountMap = new HashMap<>();

    for (Order order : doneOrders) {
        if (order.getOrderDate() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(order.getOrderDate());
            if (cal.get(Calendar.YEAR) == year) {
                String customerName = order.getCustomer().getName();
                customerCountMap.put(customerName, customerCountMap.getOrDefault(customerName, 0) + 1);
            }
        }
    }

    List<Map<String, Object>> result = new ArrayList<>();
    for (var entry : customerCountMap.entrySet()) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", entry.getKey());
        map.put("value", entry.getValue());
        result.add(map);
    }

    return result;
}

}
