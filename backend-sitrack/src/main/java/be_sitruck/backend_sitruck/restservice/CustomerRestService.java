package be_sitruck.backend_sitruck.restservice;

import java.util.List;
import java.util.Map;

import be_sitruck.backend_sitruck.restdto.request.CreateCustomerRequestDTO;
import be_sitruck.backend_sitruck.restdto.request.UpdateCustomerRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.CustomerResponseDTO;

public interface CustomerRestService {
    List<CustomerResponseDTO> getAllCustomer();
    CustomerResponseDTO getCustomerById(String id);
    CustomerResponseDTO addCustomer(CreateCustomerRequestDTO customer);
    CustomerResponseDTO updateCustomer(String id, UpdateCustomerRequestDTO customer);
    List<Map<String, Object>> getCustomerTransactionStats(int year);
    long countCustomer();
}
