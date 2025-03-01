package be_sitruck.backend_sitruck.restcontroller;

import be_sitruck.backend_sitruck.model.UserModel;
import be_sitruck.backend_sitruck.repository.UserDb;
import be_sitruck.backend_sitruck.restdto.request.LoginJwtRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.LoginJwtResponseDTO;
import be_sitruck.backend_sitruck.security.jwt.JwtUtils;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDb userDb;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginJwtRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
    
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserModel user = userDb.findByUsername(loginRequest.getUsername());
            String jwt = jwtUtils.generateJwtToken(user);
            
            return ResponseEntity.ok(new LoginJwtResponseDTO(jwt));
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Username atau password salah!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout berhasil");
    }
}

