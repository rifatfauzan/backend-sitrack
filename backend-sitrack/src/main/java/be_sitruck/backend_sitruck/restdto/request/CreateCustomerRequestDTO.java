package be_sitruck.backend_sitruck.restdto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerRequestDTO {
    
    @NotBlank(message = "Site ID tidak boleh kosong.")
    private String siteId;

    @NotBlank(message = "Nama tidak boleh kosong.")
    private String name;
    private String address;
    private String contractNo;
    private String cityId;
    private String cityOrigin;
    private String cityDestination;
    private String commodity;
    private String moveType;
}
