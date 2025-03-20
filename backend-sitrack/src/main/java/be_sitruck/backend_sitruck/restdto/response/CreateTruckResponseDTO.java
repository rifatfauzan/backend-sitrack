package be_sitruck.backend_sitruck.restdto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class CreateTruckResponseDTO {
    
    private String vehicleId;
    // private String vehicleBrand;
    // private String vehicleYear;
    // private String vehiclePlateNo;
    // private String vehicleKIRNo;
    private String message;

}

