package be_sitruck.backend_sitruck.restdto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TariffResponseDTO {
    private String tariffId;
    private String customerId;
    private String type;
    private int stdTariff;
    private int insurance;
    private int tips;
    private int police;
    private int lolo;
    private int others;
    private int totalTariff;
}
