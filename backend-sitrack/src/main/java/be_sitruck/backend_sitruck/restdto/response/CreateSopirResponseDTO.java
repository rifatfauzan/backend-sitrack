package be_sitruck.backend_sitruck.restdto.response;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateSopirResponseDTO {
    private String driverId;
    private String driverName;
    private String driver_KTP_No;
    private Date driver_KTP_date;
    private String driver_SIM_No;
    private Date driver_SIM_date;
    private String driverContact;
    private String driverCo;
    private String driverCoContact;
    private String SiteId;
    private String driverNumber;
    private String driverRemarks;
    private String recordStatus;
    private String driverType;
    private Date driverJoinDate;
    private String RowStatus;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;

}
