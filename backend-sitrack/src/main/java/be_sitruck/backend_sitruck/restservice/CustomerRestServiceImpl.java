package be_sitruck.backend_sitruck.restservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be_sitruck.backend_sitruck.model.Customer;
import be_sitruck.backend_sitruck.model.Tariff;
import be_sitruck.backend_sitruck.repository.CustomerDb;
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
    private JwtUtils jwtUtils;

    private String generateCustomerId(String siteId) {
        List<Customer> customers = customerDb.findBySiteIdOrderByIdDesc(siteId);
    
        if (!customers.isEmpty()) {
            String lastCustomerId = customers.get(0).getId();
            String lastNumberPart = lastCustomerId.substring(siteId.length());
            int lastNumber = Integer.parseInt(lastNumberPart);
            lastNumber++;
    
            return siteId + String.format("%05d", lastNumber);
        } else {
            return siteId + "00001";
        }
    }
    

    private String generateTariffId(String customerId, String type) {
        return customerId + "-" + type;
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
            throw new RuntimeException("Customer dengan nama '" + customerDTO.getName() + "' dan destinasi '" 
                                       + customerDTO.getCityDestination() + "' sudah ada!");
        }

        if (customerDTO.getName().isEmpty() || customerDTO.getCityDestination().isEmpty() || customerDTO.getAddress().isEmpty() || customerDTO.getSiteId().isEmpty()) {
            throw new RuntimeException("Nama customer, kota tujuan, alamat, dan Site ID wajib diisi.");
        }        
        
        var customer = new Customer();
        customer.setId(generateCustomerId(customerDTO.getSiteId()));
        customer.setSiteId(customerDTO.getSiteId());
        customer.setName(customerDTO.getName());
        customer.setAddress(customerDTO.getAddress());
        customer.setContractNo(customerDTO.getContractNo());
        customer.setCityOrigin(customerDTO.getCityOrigin());
        customer.setCityDestination(customerDTO.getCityDestination());
        customer.setCommodity(customerDTO.getCommodity());
        customer.setMoveType(customerDTO.getMoveType());
        customer.setTariffs(new ArrayList<>());
        customer.setInsertedBy(currentUser);
        customer.setUpdatedBy(currentUser);
        customer.setInsertedDate(new Date());
        customer.setUpdatedDate(new Date());
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
            throw new RuntimeException("Customer dengan nama '" + customerDTO.getName() + "' dan destinasi '" 
                                    + customerDTO.getCityDestination() + "' sudah ada!");
        }

        if (customerDTO.getName().isEmpty() || customerDTO.getCityDestination().isEmpty() || customerDTO.getAddress().isEmpty() || customerDTO.getSiteId().isEmpty()) {
            throw new RuntimeException("Nama customer, kota tujuan, alamat, dan Site ID wajib diisi.");
        }    

        customer.setSiteId(customerDTO.getSiteId());
        customer.setName(customerDTO.getName());
        customer.setAddress(customerDTO.getAddress());
        customer.setContractNo(customerDTO.getContractNo());
        customer.setCityOrigin(customerDTO.getCityOrigin());
        customer.setCityDestination(customerDTO.getCityDestination());
        customer.setCommodity(customerDTO.getCommodity());
        customer.setMoveType(customerDTO.getMoveType());
        customer.setUpdatedBy(currentUser);
        customer.setUpdatedDate(new Date());

        List<Tariff> existingTariffs = tariffDb.findByCustomerId(id);
        List<String> receivedTariffIds = new ArrayList<>();
        List<String> existingTariffTypes = new ArrayList<>();
        
        existingTariffs.forEach(tariff -> existingTariffTypes.add(tariff.getType()));

        if (customerDTO.getTariffs().isEmpty()) {
            tariffDb.deleteAll(existingTariffs);
            customer.setTariffs(new ArrayList<>());
        } else {
            for (TariffRequestDTO tariffDTO : customerDTO.getTariffs()) {
                String tariffId = generateTariffId(customer.getId(), tariffDTO.getType());
                if (tariffDTO.getTariffId() != null) {
                    Tariff tariff = existingTariffs.stream()
                        .filter(t -> t.getTariffId().equals(tariffDTO.getTariffId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Tariff tidak ditemukan"));
                    
                    if (!tariff.getType().equals(tariffDTO.getType()) && existingTariffTypes.contains(tariffDTO.getType())) {
                        throw new RuntimeException("Tipe tarif '" + tariffDTO.getType() + "' sudah ada untuk customer ini.");
                    }

                    tariff.setType(tariffDTO.getType());
                    tariff.setStdTariff(tariffDTO.getStdTariff());
                    tariff.setInsurance(tariffDTO.getInsurance());
                    tariff.setTips(tariffDTO.getTips());
                    tariff.setPolice(tariffDTO.getPolice());
                    tariff.setLolo(tariffDTO.getLolo());
                    tariff.setOthers(tariffDTO.getOthers());
                    tariff.setTotalTariff(
                        tariffDTO.getStdTariff() + tariffDTO.getInsurance() + tariffDTO.getTips() +
                        tariffDTO.getPolice() + tariffDTO.getLolo() + tariffDTO.getOthers()
                    );
                    receivedTariffIds.add(tariff.getTariffId());
                } else {
                    if (existingTariffTypes.contains(tariffDTO.getType())) {
                        throw new RuntimeException("Tipe tarif '" + tariffDTO.getType() + "' sudah ada untuk customer ini.");
                    }

                    Tariff newTariff = new Tariff();
                    newTariff.setTariffId(tariffId);
                    newTariff.setCustomer(customer);
                    newTariff.setType(tariffDTO.getType());
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

                    customer.getTariffs().add(newTariff);
                    receivedTariffIds.add(newTariff.getTariffId());
                }
            }

            existingTariffs.stream()
                .filter(t -> !receivedTariffIds.contains(t.getTariffId()))
                .forEach(tariffDb::delete);
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
        customerDTO.setMoveType(customer.getMoveType());

        List<TariffResponseDTO> listTariffResponse = new ArrayList<>();
        for (Tariff tariff : customer.getTariffs()) {
            var tariffResponse = new TariffResponseDTO();
            tariffResponse.setTariffId(tariff.getTariffId());
            tariffResponse.setCustomerId(tariff.getCustomer().getId());
            tariffResponse.setType(tariff.getType());
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
}
