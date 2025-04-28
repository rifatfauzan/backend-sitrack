package be_sitruck.backend_sitruck.restdto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDTO {
    private Date orderDate;

    @NotNull(message = "Customer ID tidak boleh kosong!")
    private String customerId;

    private Integer qtyChassis20;
    private Integer qtyChassis40;

    
    // private String siteId;
    private String remarksOperasional;
    private String moveType;
    private Integer downPayment;

    private Integer qty120mtfl = 0;
    private Integer qty120mt = 0;
    private Integer qty220mtfl = 0;
    private Integer qty220mt = 0;
    private Integer qty140mtfl = 0;
    private Integer qty140mt = 0;
    private Integer qty120mt120fl = 0;
    private Integer qty120mt220fl = 0;
    private Integer qty220mt120fl = 0;
    private Integer qty220mt220fl = 0;
    private Integer qtyCh120fl = 0;
    private Integer qtyCh220fl = 0;
    private Integer qtyCh140fl = 0;
}
