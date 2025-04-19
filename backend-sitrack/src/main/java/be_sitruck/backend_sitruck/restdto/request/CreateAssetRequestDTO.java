package be_sitruck.backend_sitruck.restdto.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAssetRequestDTO {
    private String assetId;
    private String jenisAsset;
    private Integer jumlahStok;
    private String brand;
    private String assetRemark;
    
    private Integer requestedStok = 0; // Default 0

    private String createdBy;
    private Date createdDate;
    private String UpdatedBy;
    private Date UpdatedDate;
}
