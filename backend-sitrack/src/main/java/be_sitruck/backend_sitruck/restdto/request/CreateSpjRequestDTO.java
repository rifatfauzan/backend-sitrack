package be_sitruck.backend_sitruck.restdto.request;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSpjRequestDTO {
    
    @NotBlank(message = "Order ID tidak boleh kosong.")
    private String orderId;

    @NotBlank(message = "Customer ID tidak boleh kosong.")
    private String customerId;
    
    @NotBlank(message = "Vehicle ID tidak boleh kosong.")
    private String vehicleId;
    
    @NotBlank(message = "Chassis ID tidak boleh kosong.")
    private String chassisId;
    
    @NotBlank(message = "Chassis Size tidak boleh kosong.")
    private int chassisSize;
    
    @NotBlank(message = "Container Type tidak boleh kosong.")
    private String containerType;
    
    @NotBlank(message = "Container Quantity tidak boleh kosong.")
    private int containerQty;
    
    @NotBlank(message = "Driver ID tidak boleh kosong.")
    private String driverId;
    
    @NotBlank(message = "Date Out tidak boleh kosong.")
    private Date dateOut;

    @NotBlank(message = "Date In tidak boleh kosong.")
    private Date dateIn;
    private Date actualDateIn;
    private int othersCommission;
    private String remarksOperasional;
    private String remarksSupervisor;
    private int status;
}
