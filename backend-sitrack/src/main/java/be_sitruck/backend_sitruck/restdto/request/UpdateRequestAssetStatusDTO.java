package be_sitruck.backend_sitruck.restdto.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequestAssetStatusDTO {
    private Integer status;
    private String requestRemark;
    private String approvalBy;
    private Date approvalDate;
}