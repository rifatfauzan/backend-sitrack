package be_sitruck.backend_sitruck.restcontroller;

import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api")
public class TestController {
    
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> testEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Backend SiTrack is successfully deployed!");
        response.put("status", "OK");
        response.put("timestamp", new Date().toString());
        
        return ResponseEntity.ok(response);
    }
}
