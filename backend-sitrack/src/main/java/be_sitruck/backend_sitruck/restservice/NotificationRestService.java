package be_sitruck.backend_sitruck.restservice;

import be_sitruck.backend_sitruck.model.Notification;
import be_sitruck.backend_sitruck.model.NotificationCategory;

import java.util.Date;
import java.util.List;

public interface NotificationRestService {
    List<Notification> getAllNotifications();
    Notification getNotificationById(Long id);
    List<Notification> getNotificationsByCategory(NotificationCategory category);
    List<Notification> getNotificationsByReferenceType(String referenceType);
    void bulkDeleteNotifications(List<Long> ids);
    Notification markAsRead(Long id);
    Notification createOrUpdateNotification(String title, String message, NotificationCategory category,
                                   String referenceId, String referenceType, Date expiryDate, Integer daysRemaining);
    void checkExpiringDocuments();
    void createOrderNotification(String orderId, String title, String message);
    void createOrderApprovalNotification(String orderId);
    void createOrderStatusNotification(String orderId, int status);

}
