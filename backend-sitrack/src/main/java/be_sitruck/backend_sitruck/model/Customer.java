package be_sitruck.backend_sitruck.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {
    
    @Id
    @Column(name = "customer_id", nullable = false)
    private String id;

    @Column(name = "site_id", nullable = false)
    private String siteId;

    @Column(name = "customer_name", nullable = false)
    private String name;

    @Column(name = "customer_address")
    private String address;

    @Column(name = "contract_no")
    private String contractNo;

    @Column(name = "city_id")
    private String cityId;

    @Column(name = "city_origin")
    private String cityOrigin;

    @Column(name = "city_destination")
    private String cityDestination;

    @Column(name = "commodity")
    private String commodity;

    @Column(name = "move_type")
    private String moveType;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tariff> tariffs;

    @Column(name = "inserted_by", nullable = false)
    private String insertedBy;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "inserted_date", nullable = false)
    private Date insertedDate;

    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date", nullable = false)
    private Date updatedDate;
}

