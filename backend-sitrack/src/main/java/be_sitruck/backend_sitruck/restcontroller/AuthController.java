package be_sitruck.backend_sitruck.restcontroller;


import be_sitruck.backend_sitruck.restdto.request.LoginJwtRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.BaseResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.LoginJwtResponseDTO;
import be_sitruck.backend_sitruck.restservice.UserRestService;
import be_sitruck.backend_sitruck.security.jwt.JwtUtils;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    UserRestService userRestService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginJwtRequestDTO request) {
        var baseResponseDTO = new BaseResponseDTO<LoginJwtResponseDTO>();
        try {
            var user = userRestService.authenticateWithUsername(request.getUsername(), request.getPassword());
            
            String token = jwtUtils.generateJwtToken(user);

            var loginResponse = new LoginJwtResponseDTO(
                token
            );

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Login berhasil!");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(loginResponse);
            
            return ResponseEntity.ok(baseResponseDTO);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.UNAUTHORIZED.value());
            baseResponseDTO.setMessage("Username atau Password salah!");
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponseDTO);
        }
    }

    
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        var baseResponseDTO = new BaseResponseDTO<>();
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setMessage("Logout berhasil!");
        baseResponseDTO.setTimestamp(new Date());
        return ResponseEntity.ok(baseResponseDTO);
    }
}

