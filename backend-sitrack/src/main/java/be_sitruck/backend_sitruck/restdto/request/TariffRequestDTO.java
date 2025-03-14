package be_sitruck.backend_sitruck.restdto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TariffRequestDTO {
    private String tariffId;  // bisa null kalo baru ditambahin

    @NotBlank(message = "Tipe chassis tidak boleh kosong.")
    private String type;

    @NotNull(message = "Standard Tariff tidak boleh kosong.")
    private int stdTariff;
    private int insurance;
    private int tips;
    private int police;
    private int LOLO;
    private int others;
}
