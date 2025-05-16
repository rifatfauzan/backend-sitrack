package be_sitruck.backend_sitruck.restdto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateKomisiResponseDTO {
    private int commissionFee;
    private String komisiId;
    private String location;
    private int truckCommission;
    private String truckId;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;

}
