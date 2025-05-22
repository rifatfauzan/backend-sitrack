package be_sitruck.backend_sitruck.restdto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateKomisiRequestDTO {
    private String location;
    private int commissionFee;
    private int truckCommission;
    private String truckId;
}
