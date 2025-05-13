package be_sitruck.backend_sitruck.repository;

import be_sitruck.backend_sitruck.model.Notification;
import be_sitruck.backend_sitruck.model.NotificationCategory;
import be_sitruck.backend_sitruck.model.UserModel;

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
    List<Notification> findByUserAndIsActiveTrue(UserModel user);
    
    @Query("SELECT n FROM Notification n WHERE " +
           "n.category = :category AND " +
           "n.referenceType = :referenceType AND " +
           "n.referenceId = :referenceId AND " +
           "n.user = :user AND " +
           "n.isActive = true")
    Optional<Notification> findByCategoryAndReferenceTypeAndReferenceIdAndUser(
        @Param("category") NotificationCategory category,
        @Param("referenceType") String referenceType,
        @Param("referenceId") String referenceId,
        @Param("user") UserModel user
    );

    @Query("SELECT n FROM Notification n WHERE n.id IN :ids AND n.user = :user")
    List<Notification> findAllByIdAndUser(@Param("ids") List<Long> ids, @Param("user") UserModel user);
    
    @Query("SELECT n FROM Notification n WHERE n.id = :id AND n.user = :user")
    Optional<Notification> findByIdAndUser(@Param("id") Long id, @Param("user") UserModel user);

    @Query("SELECT n FROM Notification n WHERE " +
           "n.category = :category AND " +
           "n.referenceType = :referenceType AND " +
           "n.referenceId = :referenceId AND " +
           "n.isActive = true")
    List<Notification> findByCategoryAndReferenceTypeAndReferenceIdAndIsActiveTrue(
        @Param("category") NotificationCategory category,
        @Param("referenceType") String referenceType,
        @Param("referenceId") String referenceId
    );
}
