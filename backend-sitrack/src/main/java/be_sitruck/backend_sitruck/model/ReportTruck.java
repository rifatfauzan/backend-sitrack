package be_sitruck.backend_sitruck.model;

import java.util.Date;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "report_truck")
public class ReportTruck {
    
    @Id
    @Column(name = "report_truck_id", nullable = false, length = 100)
    private String reportTruckId;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date Date;

    @Column(name = "start_repair")
    @Temporal(TemporalType.DATE)
    private Date startRepair;

    @Column(name = "finish_repair")
    @Temporal(TemporalType.DATE)
    private Date finishRepair;

    @Column(name = "vehicle_id")
    private String vehicleId;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "reportTruck", cascade = CascadeType.ALL)
    private List<ReportTruckAsset> assets;

    @Column(name = "vehicle_plate_no", length = 20)
    private String vehiclePlateNo;

    @Column(name = "vehicle_type", length = 20)
    private String vehicleType;

    @Column(name = "vehicle_brand", length = 20)
    private String vehicleBrand;

    @Column(name = "created_by", length = 50)
    private String createdBy;

}
