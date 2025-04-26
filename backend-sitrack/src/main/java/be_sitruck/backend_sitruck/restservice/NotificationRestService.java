package be_sitruck.backend_sitruck.restservice;

import be_sitruck.backend_sitruck.model.Notification;
import be_sitruck.backend_sitruck.model.NotificationCategory;
import be_sitruck.backend_sitruck.model.UserModel;
import java.util.Date;
import java.util.List;

public interface NotificationRestService {
    List<Notification> getAllNotificationsForUser(UserModel user);
    Notification getNotificationByIdAndUser(Long id, UserModel user);
    Notification markAsRead(Long id, UserModel user);
    void bulkDeleteNotifications(List<Long> ids, UserModel user);
    
    Notification createOrUpdateNotification(String title, String message, NotificationCategory category,
                                   String referenceId, String referenceType, Date expiryDate,
                                   Integer daysRemaining, UserModel user);
    
    void checkExpiringDocuments();
    
    void createOrderApprovalNotification(String orderId, List<Long> roleIds);
    void createOrderStatusNotification(String orderId, int status, List<Long> roleIds);
    void createRequestAssetApprovalNotification(String requestAssetId, List<Long> roleIds);
    void createRequestAssetStatusNotification(String requestAssetId, int status, List<Long> roleIds);
}
