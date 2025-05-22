package be_sitruck.backend_sitruck.restservice;

import be_sitruck.backend_sitruck.model.Notification;
import be_sitruck.backend_sitruck.model.NotificationCategory;
import be_sitruck.backend_sitruck.model.UserModel;
import be_sitruck.backend_sitruck.repository.NotificationDb;
import be_sitruck.backend_sitruck.repository.OrderDb;
import be_sitruck.backend_sitruck.repository.UserDb;
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

    @Autowired
    private UserDb userDb;

    @Override
    public List<Notification> getAllNotificationsForUser(UserModel user) {
        return notificationDb.findByUserAndIsActiveTrue(user);
    }

    @Override
    public Notification getNotificationByIdAndUser(Long id, UserModel user) {
        return notificationDb.findByIdAndUser(id, user)
            .orElseThrow(() -> new RuntimeException("Notification not found"));
    }

    @Override
    public Notification markAsRead(Long id, UserModel user) {
        Notification notification = getNotificationByIdAndUser(id, user);
        notification.setIsRead(true);
        return notificationDb.save(notification);
    }

    @Override
    public void bulkDeleteNotifications(List<Long> ids, UserModel user) {
        List<Notification> notifications = notificationDb.findAllByIdAndUser(ids, user);
        
        notifications.forEach(notif -> {
            if (notif.getIsRead()) {
                notif.setIsActive(false);
                notificationDb.save(notif);
            }
        });
    }

    @Override
    public Notification createOrUpdateNotification(
        String title, String message, NotificationCategory category,
        String referenceId, String referenceType, Date expiryDate,
        Integer daysRemaining, UserModel user
    ) {
        Optional<Notification> existingNotification = 
            notificationDb.findByCategoryAndReferenceTypeAndReferenceIdAndUser(
                category, referenceType.toUpperCase(), referenceId, user
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
            notification.setUser(user);
            return notificationDb.save(notification);
        }
    }

    public void createNotificationForRoles(
        String title, String message, NotificationCategory category,
        String referenceId, String referenceType, Date expiryDate,
        Integer daysRemaining, List<Long> roleIds
    ) {
        List<UserModel> users = userDb.findByRole_IdIn(roleIds);
        users.forEach(user -> {
            createOrUpdateNotification(
                title, message, category,
                referenceId, referenceType, expiryDate,
                daysRemaining, user
            );
        });
    }

    private String generateRedirectEndpoint(String referenceType, String referenceId) {
        return switch (referenceType.toUpperCase()) {
            case "TRUCK" -> "/trucks/detail?id=" + referenceId;
            case "CHASSIS" -> "/chassis/detail?id=" + referenceId;
            case "SOPIR" -> "/sopir/" + referenceId;
            case "ORDER" -> "/order/detail?id=" + referenceId;
            case "REQUEST_ASSET" -> "/request-assets/detail?id=" + referenceId;
            case "SPJ" -> "spj/detail?id=" + referenceId;
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
            String truckBrand = truck.getVehicleBrand();
            processDocument(
                truck.getVehicleSTNKDate(), "STNK", "TRUCK", truck.getVehicleId(), truckBrand,
                NotificationCategory.VEHICLE_STNK_EXPIRY, today
            );
            processDocument(
                truck.getVehicleKIRDate(), "KIR", "TRUCK", truck.getVehicleId(), truckBrand,
                NotificationCategory.VEHICLE_KIR_EXPIRY, today
            );
        });
    }

    private void processChassisDocuments(Date today) {
        chassisRestService.getAllChassis().forEach(chassis -> {
            processDocument(
                chassis.getChassisKIRDate(), "KIR", "CHASSIS", chassis.getChassisId(), chassis.getChassisId(),
                NotificationCategory.CHASSIS_KIR_EXPIRY, today
            );
        });
    }

    private void processDriverDocuments(Date today) {
        sopirRestService.viewAllSopir().forEach(driver -> {
            String driverName = driver.getDriverName();
            processDocument(
                driver.getDriver_SIM_Date(), "SIM", "SOPIR", driver.getDriverId(), driverName,
                NotificationCategory.DRIVER_SIM_EXPIRY, today
            );
        });
    }

    private void processDocument(Date expiryDate, String docType, String referenceType,
                                String referenceId, String referenceName, NotificationCategory category,
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
            message = docType + " pada " + referenceType + " " + referenceName + 
                    " sudah expired sejak tanggal " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(expiryDate);
            daysRemaining = 0;
        } else if (days <= 30) {
            title = docType + " Akan Expired";
            message = docType + " pada " + referenceType + " " + referenceName + 
                    " akan expired pada tanggal " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(expiryDate);
            daysRemaining = (int) days + 1;
        } else {
            return;
        }

        createNotificationForRoles(
            title, message, category,
            referenceId, referenceType, expiryDate,
            daysRemaining, Arrays.asList(1L, 2L, 3L)
        );
    }

    @Override
    public void createOrderApprovalNotification(String orderId, List<Long> roleIds) {
        String title = "Persetujuan Order Diperlukan";
        String message = String.format("Order dengan ID %s memerlukan persetujuan", orderId);
        createNotificationForRoles(
            title, message, NotificationCategory.ORDER_UPDATE,
            orderId, "ORDER", null, null, Arrays.asList(1L, 2L, 3L)
        );
    }

    @Override
    public void createOrderStatusNotification(String orderId, int status, List<Long> roleIds) {
        String statusLabel = getStatusLabel(status);
        String title = "Status Order Diperbarui";
        String message = String.format("Order dengan ID %s telah berubah status menjadi: %s", orderId, statusLabel);
        
        if (status == 0 || status == 2 || status == 3) {
            deactivateNotificationsByCategoryAndReference(
                NotificationCategory.ORDER_UPDATE,
                "ORDER",
                orderId
            );
        }
        
        createNotificationForRoles(
            title, message, NotificationCategory.ORDER_UPDATE,
            orderId, "ORDER", null, null, Arrays.asList(4L)
        );
    }

    @Override
    public void createRequestAssetApprovalNotification(String requestAssetId, List<Long> roleIds) {
        String title = "Persetujuan Request Asset Diperlukan";
        String message = String.format("Request Asset dengan ID %s memerlukan persetujuan", requestAssetId);
        createNotificationForRoles(
            title, message, NotificationCategory.REQUEST_ASSET_UPDATE,
            requestAssetId, "REQUEST_ASSET", null, null, Arrays.asList(1L, 2L, 3L)
        );
    }

    @Override
    public void createRequestAssetStatusNotification(
        String requestAssetId, 
        int status, 
        List<Long> roleIds
    ) {
        String statusLabel = getRequestAssetStatusLabel(status);
        String title = "Status Request Asset Diperbarui";
        String message = String.format(
            "Request Asset dengan ID %s telah diproses ke status: %s", 
            requestAssetId, 
            statusLabel
        );
        
        if (status == 2 || status == 3 || status == 1) {
            deactivateNotificationsByCategoryAndReference(
                NotificationCategory.REQUEST_ASSET_UPDATE,
                "REQUEST_ASSET",
                requestAssetId
            );
        }
        
        createNotificationForRoles(
            title,
            message,
            NotificationCategory.REQUEST_ASSET_UPDATE,
            requestAssetId,
            "REQUEST_ASSET",
            null,
            null,
            Arrays.asList(5L)
        );
    }

    @Override
    public void createSpjApprovalNotification(String spjId, List<Long> roleIds) {
        String title = "Persetujuan SPJ Diperlukan";
        String message = String.format("SPJ dengan ID %s memerlukan persetujuan", spjId);
        createNotificationForRoles(
            title, message, NotificationCategory.SPJ_UPDATE,
            spjId, "SPJ", null, null, Arrays.asList(1L, 2L, 3L)
        );
    }

    @Override
    public void createSpjStatusNotification(String spjId, int status, List<Long> roleIds) {
        String statusLabel = getStatusLabel(status);
        String title = "Status SPJ Diperbarui";
        String message = String.format("SPJ dengan ID %s telah berubah status menjadi: %s", spjId, statusLabel);
        
        if (status == 0 || status == 2 || status == 3) {
            deactivateNotificationsByCategoryAndReference(
                NotificationCategory.SPJ_UPDATE,
                "SPJ",
                spjId
            );
        }
        
        createNotificationForRoles(
            title, message, NotificationCategory.SPJ_UPDATE,
            spjId, "SPJ", null, null, Arrays.asList(4L)
        );
    }

    private String getRequestAssetStatusLabel(int status) {
        return switch (status) {
            case 0 -> "Pending";
            case 1 -> "Approved";
            case 2 -> "Needs Revision";
            case 3 -> "Rejected";
            default -> "Unknown";
        };
    }

    private String getStatusLabel(int status) {
        return switch (status) {
            case 0 -> "Rejected";
            case 1 -> "Pending Approval";
            case 2 -> "Needs Revision";
            case 3 -> "Ongoing";
            case 4 -> "Done";
            default -> "Unknown";
        };
    }

    @Override
    public void deactivateNotificationsByCategoryAndReference(NotificationCategory category, String referenceType, String referenceId) {
        List<Notification> notifications = notificationDb.findByCategoryAndReferenceTypeAndReferenceIdAndIsActiveTrue(
            category, referenceType, referenceId);
        
        for (Notification notification : notifications) {
            notification.setIsActive(false);
            notificationDb.save(notification);
        }
    }
}
