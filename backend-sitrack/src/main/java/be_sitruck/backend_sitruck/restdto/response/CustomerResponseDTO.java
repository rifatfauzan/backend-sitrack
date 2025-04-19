package be_sitruck.backend_sitruck.restdto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO {
    private String id;
    private String siteId;
    private String name;
    private String address;
    private String contractNo;
    private String cityOrigin;
    private String cityDestination;
    private String commodity;
    private int commission;
    private List<TariffResponseDTO> tariffs;
    private String insertedBy;
    private String updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date insertedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date updatedDate;
}
