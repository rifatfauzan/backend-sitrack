package be_sitruck.backend_sitruck.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "spj")
public class Spj {
    @Id
    @Column(name = "spj_id", nullable = false)
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false, referencedColumnName = "order_id")
    private Order order;
    
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false, referencedColumnName = "vehicle_id")
    private Truck vehicle;

    @ManyToOne
    @JoinColumn(name = "chassis_id", nullable = false, referencedColumnName = "chassis_id")
    private Chassis chassis;

    @Column(name = "chassis_size", nullable = false)
    private int chassisSize;

    @Column(name = "container_type", nullable = false)
    private String containerType;

    @Column(name = "container_quantity", nullable = false)
    private int containerQty;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false, referencedColumnName = "driver_id")
    private SopirModel driver;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_out")
    private Date dateOut;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_in")
    private Date dateIn;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "actual_date_in")
    private Date actualDateIn;

    @Column(name = "commission", nullable = false)
    private int commission;

    @Column(name = "others_commission")
    private int othersCommission;

    @Column(name = "remarks_operasional")
    private String remarksOperasional;

    @Column(name = "remarks_supervisor")
    private String remarksSupervisor;

    @Column(name = "status")
    private int status; // 0 = Ditolak, 1 = Perlu Disetujui, 2 = Butuh Revisi, 3 = Ongoing, 4 = Selesai

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

    @Column(name = "approved_by")
    private String approvedBy;
    
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "approved_date")
    private Date approvedDate;
}
