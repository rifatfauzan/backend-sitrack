package be_sitruck.backend_sitruck.restdto.response;

import java.util.Date;
import java.util.List;

import be_sitruck.backend_sitruck.model.Spj;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponseDTO {

    private String orderId;
    private Date orderDate;
    private String customerId;
    private List<SpjResponseDTO> spjList;

    private Integer qtyChassis20;
    private Integer qtyChassis40;
    private String siteId;
    private String remarksOperasional;
    private String remarksSupervisor;
    private String moveType;
    private Float downPayment;
    private Integer orderStatus;

    private Integer qty120mtfl;
    private Integer qty120mt;
    private Integer qty220mtfl;
    private Integer qty220mt;
    private Integer qty140mtfl;
    private Integer qty140mt;
    private Integer qty120mt120fl;
    private Integer qty120mt220fl;
    private Integer qty220mt120fl;
    private Integer qty220mt220fl;
    private Integer qtyCh120fl;
    private Integer qtyCh220fl;
    private Integer qtyCh140fl;

    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
    private String approvedBy;
    private Date approvedDate;
}
