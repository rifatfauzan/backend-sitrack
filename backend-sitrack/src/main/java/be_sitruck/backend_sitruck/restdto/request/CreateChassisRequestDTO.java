package be_sitruck.backend_sitruck.restdto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateChassisRequestDTO {
    private String chassisId;
    private String chassisSize;
    private String chassisYear;
    private String chassisNumber;
    private String chassisAxle;
    private String chassisKIRNo;
    private Date chassisKIRDate;
    private String chassisType;
    private String siteId;
}