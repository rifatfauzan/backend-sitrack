package be_sitruck.backend_sitruck.restcontroller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be_sitruck.backend_sitruck.restdto.response.BaseResponseDTO;
import be_sitruck.backend_sitruck.restservice.*;

@RestController
@RequestMapping("/api/referencestat")
public class ReferenceStatRestController {

    @Autowired
    private CustomerRestService customerRestService;

    @Autowired
    private ChassisRestService chassisRestService;

    @Autowired
    private TruckRestService truckRestService;

    @Autowired
    private SopirRestService sopirRestService;

    @GetMapping("")
    public ResponseEntity<BaseResponseDTO<Map<String, Long>>> getReferenceStats() {
        BaseResponseDTO<Map<String, Long>> response = new BaseResponseDTO<>();

        try {
            Map<String, Long> data = new HashMap<>();
            data.put("trucks", truckRestService.countTrucks());
            data.put("chassis", chassisRestService.countChassis());
            data.put("drivers", sopirRestService.countSopir());
            data.put("customers", customerRestService.countCustomer());

            response.setStatus(200);
            response.setMessage("Statistik referensi berhasil diambil");
            response.setTimestamp(new Date());
            response.setData(data);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Gagal mengambil data referensi: " + e.getMessage());
            response.setTimestamp(new Date());
            return ResponseEntity.status(500).body(response);
        }
    }
}
