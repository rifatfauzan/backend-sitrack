package be_sitruck.backend_sitruck.restdto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAssetItemDTO {
    private String assetId;
    private Integer requestedQuantity;
    private String jenisAsset;
    private String brand;
    private Integer assetPrice;
}
