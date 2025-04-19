package be_sitruck.backend_sitruck.restdto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAssetResponseDTO {

    private String assetId;
    private String message;

}
