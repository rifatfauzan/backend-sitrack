package be_sitruck.backend_sitruck.restdto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpjResponseDTO {
    private String id;
    private String orderId;
    private String customerId;
    private String vehicleId;
    private String chassisId;
    private int chassisSize;
    private String containerType;
    private int containerQty;
    private String driverId;
    private Date dateOut;
    private Date dateIn;
    private Date actualDateIn;
    private int commission;
    private int othersCommission;
    private String remarksOperasional;
    private String remarksSupervisor;
    private int status;
    private String insertedBy;
    private Date insertedDate;
    private String updatedBy;
    private Date updatedDate;
    private String approvedBy;
    private Date approvedDate;
}
