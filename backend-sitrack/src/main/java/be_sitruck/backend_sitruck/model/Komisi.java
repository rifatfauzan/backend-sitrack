package be_sitruck.backend_sitruck.model;

import java.util.Date;

import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "komisi")
public class Komisi {
    
    @Id
    @Column(name = "komisi_id", nullable = false, length = 8)
    private String komisiId;

    @Column(name="location", nullable = false)
    private String location;

    @Column(name="komisi", nullable = false)
    private int commissionFee; 

    @Column(name="komisi_truk", nullable = false)
    private int truckCommission;

    @ManyToOne
    @JoinColumn(name = "truck_id", referencedColumnName = "Vehicle_ID")
    private Truck truck;


    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    @Temporal(TemporalType.DATE)
    private Date createdDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_date")
    @Temporal(TemporalType.DATE)
    private Date updatedDate;



}
