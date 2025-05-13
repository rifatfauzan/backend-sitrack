package be_sitruck.backend_sitruck.restdto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTruckRequestDTO {

    @NotNull(message = "Vehicle ID is required")
    private String vehicleId;

    @NotBlank(message = "Vehicle brand is required")
    private String vehicleBrand;

    @Pattern(regexp = "\\d{4}", message = "Year harus 4 digit angka")
    @NotBlank(message = "Vehicle year is required")
    private String vehicleYear;

    @NotBlank(message = "Vehicle plate number is required")
    private String vehiclePlateNo;

    @Future(message = "Tanggal Expired STNK harus lebih dari hari ini!")
    @NotNull(message = "Vehicle STNK date is required")
    private Date vehicleSTNKDate;

    @NotBlank(message = "Vehicle KIR number is required")
    private String vehicleKIRNo;

    @Future(message = "Tanggal Expired KIR harus lebih dari hari ini!")
    @NotNull(message = "Vehicle KIR date is required")
    private Date vehicleKIRDate;

    @NotBlank(message = "Division is required")
    @Pattern(regexp = "\\d{2}", message = "Division harus 2 digit angka")
    private String division;

    // Tambahan properti yang bisa null
    @Size(max = 5, message = "Cylinder max 5 characters")
    private String vehicleCylinder;

    @Size(max = 20, message = "Chassis No max 20 characters")
    private String vehicleChassisNo;

    @Size(max = 20, message = "Engine No max 20 characters")
    private String vehicleEngineNo;

    @Size(max = 20, message = "Biz License No max 20 characters")
    private String vehicleBizLicenseNo;

    private Date vehicleBizLicenseDate;

    @Size(max = 20, message = "Dispensation No max 20 characters")
    private String vehicleDispensationNo;

    private Date vehicleDispensationDate;

    @Size(max = 300, message = "Remarks max 300 characters")
    private String vehicleRemarks;

    @Size(max = 3, message = "Site ID max 3 characters")
    private String siteId;

    @Size(max = 20, message = "Vehicle Type max 20 characters")
    private String vehicleType;

    private String dept = "TR";

    private String recordStatus = "A";

    private String rowStatus = "A";

    @Size(max = 6, message = "Vehicle Number max 6 characters")
    private String vehicleNumber;

    @DecimalMin(value = "0.0", inclusive = true, message = "Fuel consumption minimal 0.0")
    @DecimalMax(value = "999.9", inclusive = true, message = "Fuel consumption maksimal 999.9")
    private Double vehicleFuelConsumption = 0.0;

    private String vehicleGroup;

    private String insertedBy;
    private Date insertedDate;
    private String updatedBy;
    private Date updatedDate;

}
