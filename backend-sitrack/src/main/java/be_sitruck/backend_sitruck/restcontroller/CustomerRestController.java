package be_sitruck.backend_sitruck.restcontroller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be_sitruck.backend_sitruck.restdto.request.CreateCustomerRequestDTO;
import be_sitruck.backend_sitruck.restdto.request.UpdateCustomerRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.BaseResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.CustomerResponseDTO;
import be_sitruck.backend_sitruck.restservice.CustomerRestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/customer")
public class CustomerRestController {
    
    @Autowired
    private CustomerRestService customerRestService;

    @PostMapping("/add")
    public ResponseEntity<?> addCustomer(@RequestBody CreateCustomerRequestDTO customerRequestDTO){
        var baseResponseDTO = new BaseResponseDTO<CustomerResponseDTO>();
        try {
            CustomerResponseDTO customerResponseDTO = customerRestService.addCustomer(customerRequestDTO);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Customer berhasil ditambahkan!");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(customerResponseDTO);
            return ResponseEntity.ok(baseResponseDTO);

        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }
    
    @PreAuthorize("hasAuthority('Operasional')")
    @GetMapping("all")
    public ResponseEntity<?> getAllCustomers() {
        var baseResponseDTO = new BaseResponseDTO<List<CustomerResponseDTO>>();
        try {
            List<CustomerResponseDTO> listCustomer = customerRestService.getAllCustomer();
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(listCustomer);
            baseResponseDTO.setMessage(String.format("List customer berhasil ditemukan sebanyak %d user", listCustomer.size()));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }
    
    @PreAuthorize("hasAuthority('Operasional')")
    @GetMapping("detail/{id}")
    public ResponseEntity<?> getDetailCustomer(@PathVariable("id") String id) {
        var baseResponseDTO = new BaseResponseDTO<CustomerResponseDTO>();
        try {
            CustomerResponseDTO customerResponseDTO = customerRestService.getCustomerById(id);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Customer berhasil ditemukan!");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(customerResponseDTO);
            return ResponseEntity.ok(baseResponseDTO);

        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable("id") String id, @RequestBody UpdateCustomerRequestDTO customerRequestDTO){
        var baseResponseDTO = new BaseResponseDTO<CustomerResponseDTO>();
        try {
            CustomerResponseDTO customerResponseDTO = customerRestService.updateCustomer(id, customerRequestDTO);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Customer berhasil diupdate!");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(customerResponseDTO);
            return ResponseEntity.ok(baseResponseDTO);

        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }
}
