package be_sitruck.backend_sitruck.restdto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApproveOrderRequestDTO {

    private String orderId;
    private String remarksSupervisor;
    private Integer orderStatus;

}
