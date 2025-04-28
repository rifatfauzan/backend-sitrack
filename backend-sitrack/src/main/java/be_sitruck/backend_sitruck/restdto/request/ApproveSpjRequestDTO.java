package be_sitruck.backend_sitruck.restdto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApproveSpjRequestDTO {
    private String spjId;
    private int status;
    private String remarksSupervisor;
}
