package be_sitruck.backend_sitruck.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "message", nullable = false, length = 500)
    private String message;
    
    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationCategory category;
    
    @Column(name = "reference_id")
    private String referenceId;
    
    @Column(name = "reference_type")
    private String referenceType;
    
    @Column(name = "expiry_date")
    @Temporal(TemporalType.DATE)
    private Date expiryDate;
    
    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;
    
    @Column(name = "created_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "days_remaining")
    private Integer daysRemaining;

    @Column(name = "redirect_endpoint", nullable = false)
    private String redirectEndpoint;

}
