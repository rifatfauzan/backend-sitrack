package be_sitruck.backend_sitruck.restcontroller;

import be_sitruck.backend_sitruck.model.Notification;
import be_sitruck.backend_sitruck.model.NotificationCategory;
import be_sitruck.backend_sitruck.restdto.response.BaseResponseDTO;
import be_sitruck.backend_sitruck.restservice.ChassisRestService;
import be_sitruck.backend_sitruck.restservice.NotificationRestService;
import be_sitruck.backend_sitruck.restservice.SopirRestService;
import be_sitruck.backend_sitruck.restservice.TruckRestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationRestController {

    @Autowired
    private NotificationRestService notificationService;

    @Autowired
    private TruckRestService truckRestService;

    @Autowired
    private SopirRestService sopirRestService;

    @Autowired
    private ChassisRestService chassisRestService;

    @GetMapping
    public ResponseEntity<BaseResponseDTO<List<Notification>>> getAllNotifications() {
        BaseResponseDTO<List<Notification>> response = new BaseResponseDTO<>();
        response.setData(notificationService.getAllNotifications());
        response.setMessage("Berhasil mendapatkan semua notifikasi");
        response.setTimestamp(new Date());
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<BaseResponseDTO<List<Notification>>> getByCategory(
            @PathVariable NotificationCategory category) {
        BaseResponseDTO<List<Notification>> response = new BaseResponseDTO<>();
        response.setData(notificationService.getNotificationsByCategory(category));
        response.setMessage("Berhasil mendapatkan notifikasi berdasarkan kategori");
        response.setTimestamp(new Date());
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<BaseResponseDTO<Map<String, Object>>> getNotificationDetails(
            @PathVariable Long id) {
        Notification notification = notificationService.getNotificationById(id);
        Map<String, Object> data = new HashMap<>();
        data.put("notification", notification);
        data.put("referenceData", getReferenceData(notification));

        BaseResponseDTO<Map<String, Object>> response = new BaseResponseDTO<>();
        response.setData(data);
        response.setMessage("Detail notifikasi beserta data referensi");
        response.setTimestamp(new Date());
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<BaseResponseDTO<Notification>> markAsRead(@PathVariable Long id) {
        BaseResponseDTO<Notification> response = new BaseResponseDTO<>();
        response.setData(notificationService.markAsRead(id));
        response.setMessage("Notifikasi berhasil ditandai sebagai telah dibaca");
        response.setTimestamp(new Date());
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/bulk-delete")
    public ResponseEntity<BaseResponseDTO<String>> bulkDeleteNotifications(@RequestBody List<Long> ids) {
        notificationService.bulkDeleteNotifications(ids);
        BaseResponseDTO<String> response = new BaseResponseDTO<>();
        response.setMessage("Notifikasi berhasil dihapus");
        response.setTimestamp(new Date());
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/trigger-check")
    public ResponseEntity<BaseResponseDTO<String>> manualTrigger() {
        notificationService.checkExpiringDocuments();
        BaseResponseDTO<String> response = new BaseResponseDTO<>();
        response.setMessage("Pengecekan dokumen kedaluwarsa berhasil dipicu");
        response.setTimestamp(new Date());
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    private Object getReferenceData(Notification notification) {
        return switch (notification.getReferenceType().toUpperCase()) {
            case "TRUCK" -> truckRestService.getTruckById(notification.getReferenceId());
            case "CHASSIS" -> chassisRestService.getChassisById(notification.getReferenceId());
            case "SOPIR" -> sopirRestService.viewSopirById(notification.getReferenceId());
            default -> null;
        };
    }
}
