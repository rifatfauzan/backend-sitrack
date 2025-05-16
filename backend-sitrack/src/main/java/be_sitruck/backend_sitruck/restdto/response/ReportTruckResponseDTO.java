package be_sitruck.backend_sitruck.restdto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportTruckResponseDTO {
    private String reportTruckId;
    private String message;
}
