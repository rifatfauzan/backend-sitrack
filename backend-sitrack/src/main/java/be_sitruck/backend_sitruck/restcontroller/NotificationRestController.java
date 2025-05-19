package be_sitruck.backend_sitruck.restcontroller;

import be_sitruck.backend_sitruck.model.Notification;
import be_sitruck.backend_sitruck.model.UserModel;
import be_sitruck.backend_sitruck.restdto.response.BaseResponseDTO;
import be_sitruck.backend_sitruck.restservice.ChassisRestService;
import be_sitruck.backend_sitruck.restservice.NotificationRestService;
import be_sitruck.backend_sitruck.restservice.SopirRestService;
import be_sitruck.backend_sitruck.restservice.TruckRestService;
import be_sitruck.backend_sitruck.security.jwt.JwtUtils;
import be_sitruck.backend_sitruck.repository.UserDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDb userDb;

    @GetMapping
    public ResponseEntity<BaseResponseDTO<List<Notification>>> getUserNotifications(
        @RequestHeader("Authorization") String token
    ) {
        UserModel currentUser = getUserFromToken(token);
        List<Notification> notifications = notificationService.getAllNotificationsForUser(currentUser);
        
        BaseResponseDTO<List<Notification>> response = new BaseResponseDTO<>();
        response.setData(notifications);
        response.setMessage("Berhasil mendapatkan notifikasi");
        response.setTimestamp(new Date());
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<BaseResponseDTO<Notification>> markAsRead(
        @PathVariable Long id,
        @RequestHeader("Authorization") String token
    ) {
        UserModel currentUser = getUserFromToken(token);
        Notification notification = notificationService.markAsRead(id, currentUser);
        
        BaseResponseDTO<Notification> response = new BaseResponseDTO<>();
        response.setData(notification);
        response.setMessage("Notifikasi berhasil ditandai sebagai telah dibaca");
        response.setTimestamp(new Date());
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/bulk-delete")
    public ResponseEntity<BaseResponseDTO<String>> bulkDeleteNotifications(
        @RequestBody List<Long> ids,
        @RequestHeader("Authorization") String token
    ) {
        UserModel currentUser = getUserFromToken(token);
        notificationService.bulkDeleteNotifications(ids, currentUser);
        
        BaseResponseDTO<String> response = new BaseResponseDTO<>();
        response.setMessage("Notifikasi berhasil dihapus");
        response.setTimestamp(new Date());
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    private UserModel getUserFromToken(String token) {
        String username = jwtUtils.getUsernameFromJwtToken(token.replace("Bearer ", ""));
        UserModel user = userDb.findByUsernameIgnoreCase(username);
        if (user == null) {
            throw new RuntimeException("User tidak ditemukan");
        }
        return user;
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
}
