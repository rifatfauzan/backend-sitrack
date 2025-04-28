package be_sitruck.backend_sitruck.restdto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRequestAssetRequestDTO {
    private String requestAssetId;                 
    private Integer status;                     
    private String requestRemark;                 
    private String createdBy;                      
    private Date createdDate;                     
    private String updatedBy;                      
    private Date updatedDate;                      
    private String approvalBy;
    private Date approvalDate;
    private List<RequestAssetItemDTO> assets;      
}