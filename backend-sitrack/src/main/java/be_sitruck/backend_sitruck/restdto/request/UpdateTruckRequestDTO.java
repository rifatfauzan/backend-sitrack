package be_sitruck.backend_sitruck.restdto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTruckRequestDTO {

    @NotNull(message = "Vehicle ID is required")
    private String vehicleId;

    @NotBlank(message = "Vehicle brand is required")
    private String vehicleBrand;

    @NotBlank(message = "Vehicle year is required")
    private String vehicleYear;

    @NotBlank(message = "Vehicle plate number is required")
    private String vehiclePlateNo;

    @NotNull(message = "Vehicle STNK date is required")
    private Date vehicleSTNKDate;

    @NotBlank(message = "Vehicle KIR number is required")
    private String vehicleKIRNo;

    @NotNull(message = "Vehicle KIR date is required")
    private Date vehicleKIRDate;

    // Tambahan properti yang bisa null
    private String vehicleCylinder;
    private String vehicleChassisNo;
    private String vehicleEngineNo;
    private String vehicleBizLicenseNo;
    private Date vehicleBizLicenseDate;
    private String vehicleDispensationNo;
    private Date vehicleDispensationDate;
    private String vehicleRemarks;
    private String siteId;
    private String vehicleType;
    private String division;
    private String dept = "TR";
    private String recordStatus = "A";
    private String rowStatus = "A";
    private String vehicleNumber;
    private String updatedBy;
    private Date updatedDate;

}
