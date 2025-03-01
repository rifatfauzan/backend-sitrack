package be_sitruck.backend_sitruck.restdto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUserResponseDTO {
    private Long id;
    private String username;
    private String role;
}
