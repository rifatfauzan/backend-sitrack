package be_sitruck.backend_sitruck.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {
    
    @Id
    @Column(name = "customer_id", nullable = false, length = 8)
    private String id;

    @Column(name = "site_id", nullable = false, length = 3)
    private String siteId;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String name;

    @Column(name = "customer_address", length = 100)
    private String address;

    @Column(name = "contract_no", length = 20)
    private String contractNo;

    @Column(name = "city_origin", length = 100)
    private String cityOrigin;

    @Column(name = "city_destination", length = 100)
    private String cityDestination;

    @Column(name = "commodity", length = 50)
    private String commodity;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tariff> tariffs;

    @Column(name = "inserted_by", nullable = false)
    private String insertedBy;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "inserted_date", nullable = false)
    private Date insertedDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updated_date")
    private Date updatedDate;
}

