package be_sitruck.backend_sitruck.restdto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TariffRequestDTO {
    private String tariffId;  // bisa null kalo baru ditambahin

    @NotBlank(message = "Chassis size tidak boleh kosong.")
    private int chassisSize;

    @NotBlank(message = "Move type tidak boleh kosong.")
    private String moveType;

    @NotBlank(message = "Standard Tariff tidak boleh kosong.")
    private int stdTariff;
    private int insurance;
    private int tips;
    private int police;
    private int lolo;
    private int others;
}
