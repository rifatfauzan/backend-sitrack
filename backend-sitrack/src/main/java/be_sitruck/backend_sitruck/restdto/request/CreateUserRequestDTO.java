package be_sitruck.backend_sitruck.restdto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUserRequestDTO {
    private String username;
    private String password;
    private String role;
}

