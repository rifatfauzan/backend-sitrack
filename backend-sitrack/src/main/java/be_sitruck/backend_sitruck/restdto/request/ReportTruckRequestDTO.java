package be_sitruck.backend_sitruck.restdto.request;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportTruckRequestDTO {
    private String reportTruckId;
    @NotNull(message = "Date is required")
    private Date date;
    @Past(message = "Start Repair hanya bisa di masa lampau")
    private Date startRepair;
    @PastOrPresent(message = "Finish Repair hanya bisa di masa lampau atau hari ini")
    private Date finishRepair;
    @NotNull(message = "Vehicle is required")
    private String vehicleId;
    private String description;
    private String createdBy;
    private List<ReportTruckAssetDTO> assets;
    private String vehiclePlateNo;
    private String vehicleType;
}
