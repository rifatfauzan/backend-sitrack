package be_sitruck.backend_sitruck.restcontroller;

import org.springframework.web.bind.annotation.RestController;

import be_sitruck.backend_sitruck.model.TestEntity;
import be_sitruck.backend_sitruck.repository.TestRepository;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    TestRepository testRepository;
    
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> testEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Backend SiTrack is successfully deployed!");
        response.put("status", "OK");
        response.put("timestamp", new Date().toString());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/test/data")
    public ResponseEntity<TestEntity> createTestData(@RequestBody TestEntity test) {
        TestEntity saved = testRepository.save(test);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/test/data")
    public ResponseEntity<List<TestEntity>> getAllTestData() {
        return ResponseEntity.ok(testRepository.findAll());
    }
}
