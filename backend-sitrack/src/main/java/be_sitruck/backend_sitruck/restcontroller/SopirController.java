package be_sitruck.backend_sitruck.restcontroller;

import org.springframework.web.bind.annotation.RestController;

import be_sitruck.backend_sitruck.model.SopirModel;
import be_sitruck.backend_sitruck.restdto.request.CreateSopirRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.BaseResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateSopirResponseDTO;
import be_sitruck.backend_sitruck.restservice.SopirRestService;
import jakarta.validation.Valid;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/sopir")
public class SopirController {

    @Autowired
    private SopirRestService sopirRestService;
    
    @PostMapping("/add")
    public ResponseEntity<?> addSopir (@RequestBody CreateSopirRequestDTO sopirDTO){
        try{ 
            CreateSopirResponseDTO sopirResponseDTO = sopirRestService.addSopir(sopirDTO);
            var response = new BaseResponseDTO<CreateSopirResponseDTO>();
            response.setData(sopirResponseDTO);
            response.setMessage("Sopir berhasil ditambahkan");
            response.setTimestamp(new Date());
            response.setStatus(201);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e){
            var response = new BaseResponseDTO<>();
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            response.setStatus(400);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('Admin', 'Supervisor', 'Manager', 'Operasional')")
    @GetMapping("/all")
    public ResponseEntity<?> viewAllSopir(){
        var response = new BaseResponseDTO<>();
        response.setData(sopirRestService.viewAllSopir());
        response.setMessage("Data sopir berhasil ditampilkan");
        response.setTimestamp(new Date());
        response.setStatus(200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('Admin', 'Supervisor', 'Manager', 'Operasional')")
    @GetMapping("/detail/{driverId}")
    public ResponseEntity<?> viewSopirById(@PathVariable("driverId") String driverId){
        try{
            var response = new BaseResponseDTO<>();
            response.setData(sopirRestService.viewSopirById(driverId));
            response.setMessage("Data sopir berhasil ditampilkan");
            response.setTimestamp(new Date());
            response.setStatus(200);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            var response = new BaseResponseDTO<>();
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            response.setStatus(400);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{driverId}")
    public ResponseEntity<?> updateSopir(@PathVariable("driverId") String driverId, @RequestBody CreateSopirRequestDTO sopirDTO){
        try{
            var response = new BaseResponseDTO<>();
            response.setData(sopirRestService.updateSopir(driverId, sopirDTO));
            response.setMessage("Data sopir berhasil diupdate");
            response.setTimestamp(new Date());
            response.setStatus(200);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            var response = new BaseResponseDTO<>();
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            response.setStatus(400);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


}
