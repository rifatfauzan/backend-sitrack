package be_sitruck.backend_sitruck.restservice;

import be_sitruck.backend_sitruck.model.Notification;
import be_sitruck.backend_sitruck.model.NotificationCategory;
import be_sitruck.backend_sitruck.repository.NotificationDb;
import be_sitruck.backend_sitruck.repository.OrderDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class NotificationRestServiceImpl implements NotificationRestService {

    @Autowired
    private NotificationDb notificationDb;

    @Autowired
    private TruckRestService truckRestService;

    @Autowired
    private SopirRestService sopirRestService;

    @Autowired
    private ChassisRestService chassisRestService;

    @Autowired
    private OrderDb orderDb;

    @Override
    public List<Notification> getAllNotifications() {
        return notificationDb.findByIsActiveTrue();
    }

    @Override
    public Notification getNotificationById(Long id) {
        return notificationDb.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
    }

    @Override
    public List<Notification> getNotificationsByCategory(NotificationCategory category) {
        return notificationDb.findByCategoryAndIsActiveTrue(category);
    }

    @Override
    public List<Notification> getNotificationsByReferenceType(String referenceType) {
        return notificationDb.findByReferenceTypeAndIsActiveTrue(referenceType);
    }

    @Override
    public Notification markAsRead(Long id) {
        Notification notification = getNotificationById(id);
        notification.setIsRead(true);
        return notificationDb.save(notification);
    }

    @Override
    public void bulkDeleteNotifications(List<Long> ids) {
        for (Long id: ids){
            Notification notification = getNotificationById(id);
            if (Boolean.TRUE.equals(notification.getIsRead())) {
                notification.setIsActive(false);
                notificationDb.save(notification);
            }
        }
    }

    @Override
    public Notification createOrUpdateNotification(String title, String message, 
                                                  NotificationCategory category,
                                                  String referenceId, String referenceType, 
                                                  Date expiryDate, Integer daysRemaining) {
        
        Optional<Notification> existingNotification = 
            notificationDb.findByCategoryAndReferenceTypeAndReferenceId(
                category, 
                referenceType.toUpperCase(), 
                referenceId
            );

        if (existingNotification.isPresent()) {
            Notification notification = existingNotification.get();
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setExpiryDate(expiryDate);
            notification.setDaysRemaining(daysRemaining);
            notification.setIsActive(true);
            notification.setIsRead(false);
            return notificationDb.save(notification);
        } else {
            Notification notification = new Notification();
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setCategory(category);
            notification.setReferenceId(referenceId);
            notification.setReferenceType(referenceType.toUpperCase());
            notification.setExpiryDate(expiryDate);
            notification.setCreatedDate(new Date());
            notification.setIsRead(false);
            notification.setIsActive(true);
            notification.setDaysRemaining(daysRemaining);
            notification.setRedirectEndpoint(generateRedirectEndpoint(referenceType, referenceId));
            return notificationDb.save(notification);
        }
    }

    private String generateRedirectEndpoint(String referenceType, String referenceId) {
        return switch (referenceType.toUpperCase()) {
            case "TRUCK" -> "/trucks/detail?id=" + referenceId;
            case "CHASSIS" -> "/chassis/detail?id=" + referenceId;
            case "SOPIR" -> "/sopir/" + referenceId;
            case "ORDER" -> "/order/detail?id=" + referenceId;
            case "REQUEST_ASSET" -> "/request-assets/detail?id=" + referenceId;
            default -> "#";
        };
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkExpiringDocuments() {
        Date today = new Date();
        processTruckDocuments(today);
        processChassisDocuments(today);
        processDriverDocuments(today);
    }

    private void processTruckDocuments(Date today) {
        truckRestService.getAllTruck().forEach(truck -> {
            processDocument(
                truck.getVehicleSTNKDate(), 
                "STNK", 
                "Truck", 
                truck.getVehicleId(), 
                NotificationCategory.VEHICLE_STNK_EXPIRY, 
                today
            );
            processDocument(
                truck.getVehicleKIRDate(), 
                "KIR", 
                "Truck", 
                truck.getVehicleId(), 
                NotificationCategory.VEHICLE_KIR_EXPIRY, 
                today
            );
        });
    }

    private void processChassisDocuments(Date today) {
        chassisRestService.getAllChassis().forEach(chassis -> {
            processDocument(
                chassis.getChassisKIRDate(), 
                "KIR", 
                "Chassis", 
                chassis.getChassisId(), 
                NotificationCategory.CHASSIS_KIR_EXPIRY, 
                today
            );
        });
    }

    private void processDriverDocuments(Date today) {
        sopirRestService.viewAllSopir().forEach(driver -> {
            processDocument(
                driver.getDriver_SIM_Date(), 
                "SIM", 
                "SOPIR", 
                driver.getDriverId(), 
                NotificationCategory.DRIVER_SIM_EXPIRY, 
                today
            );
        });
    }

    private void processDocument(Date expiryDate, String docType, String referenceType,
                                String referenceId, NotificationCategory category,
                                Date today) {
        if (expiryDate == null) return;

        long diff = expiryDate.getTime() - today.getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        boolean isExpired = days < 0;

        String title;
        String message;
        int daysRemaining;

        if (isExpired) {
            title = docType + " Expired";
            message = docType + " pada " + referenceType + " " + referenceId + 
                    " sudah expired sejak " + Math.abs(days) + " hari yang lalu";
            daysRemaining = 0;
        } else if (days <= 30) {
            title = docType + " Akan Expired";
            message = docType + " pada " + referenceType + " " + referenceId + 
                    " akan expired dalam " + (days + 1) + " hari";
            daysRemaining = (int) days + 1;
        } else {
            return;
        }

        createOrUpdateNotification(
            title,
            message,
            category,
            referenceId,
            referenceType,
            expiryDate,
            daysRemaining
        );
    }

    @Override
    public void createOrderNotification(String orderId, String title, String message) {
        this.createOrUpdateNotification(
            title,
            message,
            NotificationCategory.ORDER_UPDATE,
            orderId,
            "ORDER",
            null,
            null
        );
    }

    @Override
    public void createOrderApprovalNotification(String orderId) {
        String title = "Persetujuan Order Diperlukan";
        String message = String.format(
            "Order dengan ID %s memerlukan persetujuan", 
            orderId
        );
        
        createOrderNotification(orderId, title, message);
    }

    @Override
    public void createOrderStatusNotification(String orderId, int status) {
        String statusLabel = getStatusLabel(status);
        String title = "Status Order Diperbarui";
        String message = String.format(
            "Order dengan ID %s telah berubah status menjadi: %s", 
            orderId, 
            statusLabel
        );
        
        createOrderNotification(orderId, title, message);
    }

    @Override
    public void createRequestAssetNotification(String requestAssetId, String title, String message) {
        this.createOrUpdateNotification(
            title,
            message,
            NotificationCategory.REQUEST_ASSET_UPDATE,
            requestAssetId,
            "REQUEST_ASSET",
            null,
            null
        );
    }

    @Override
    public void createRequestAssetApprovalNotification(String requestAssetId) {
        String title = "Persetujuan Request Asset Diperlukan";
        String message = String.format(
            "Request Asset dengan ID %s memerlukan persetujuan",
            requestAssetId
        );
        createRequestAssetNotification(requestAssetId, title, message);
    }

    @Override
    public void createRequestAssetStatusNotification(String requestAssetId, int status, String role) {
        String statusLabel = getRequestAssetStatusLabel(status);
        String title = "Status Request Asset Diperbarui";
        String message = role.equals("OPERASIONAL") 
            ? String.format("Request Asset dengan ID %s telah %s", requestAssetId, statusLabel)
            : String.format("Request Asset dengan ID %s telah diproses ke status: %s", requestAssetId, statusLabel);
        
        createRequestAssetNotification(requestAssetId, title, message);
    }

    private String getRequestAssetStatusLabel(int status) {
        switch (status) {
            case 0: return "Pending";
            case 1: return "Approved";
            case 2: return "Needs Revision";
            case 3: return "Rejected";
            default: return "Unknown";
        }
    }

    private static String getStatusLabel(int status) {
        switch (status) {
        case 0: return "Rejected";
        case 1: return "Pending Approval";
        case 2: return "Needs Revision";
        case 3: return "Ongoing";
        case 4: return "Done";
        default: return "Unknown";
        }
    }
}