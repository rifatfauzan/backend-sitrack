package be_sitruck.backend_sitruck.repository;

import be_sitruck.backend_sitruck.model.Notification;
import be_sitruck.backend_sitruck.model.NotificationCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface NotificationDb extends JpaRepository<Notification, Long> {
    List<Notification> findByIsActiveTrue();
    List<Notification> findByCategoryAndIsActiveTrue(NotificationCategory category);
    List<Notification> findByReferenceTypeAndIsActiveTrue(String referenceType);
    List<Notification> findByIsReadFalseAndIsActiveTrue();
    
    @Query("SELECT n FROM Notification n WHERE " +
           "n.category = :category AND " +
           "n.referenceType = :referenceType AND " +
           "n.referenceId = :referenceId AND " +
           "n.isActive = true")
    Optional<Notification> findByCategoryAndReferenceTypeAndReferenceId(
        @Param("category") NotificationCategory category,
        @Param("referenceType") String referenceType,
        @Param("referenceId") String referenceId
    );
}
