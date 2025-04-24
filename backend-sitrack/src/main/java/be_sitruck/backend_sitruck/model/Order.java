package be_sitruck.backend_sitruck.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking_order")
public class Order implements Serializable{

    @Id
    @Column(name = "order_id", length = 8, nullable = false)
    private String orderId;

    @Column(name = "order_date")
    @Temporal(TemporalType.DATE)
    private Date orderDate;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    // @Column(name = "customer_id")
    // private String customer.getId();

    @Column(name = "qty_chassis_20")
    private Integer qtyChassis20;

    @Column(name = "qty_chassis_40")
    private Integer qtyChassis40;

    @Column(name = "site_id", length = 3, columnDefinition = "CHAR(3) DEFAULT 'JKT'")
    private String siteId;

    @Column(name = "remarks_operasional")
    private String remarksOperasional;

    @Column(name = "remarks_supervisor")
    private String remarksSupervisor;

    @Column(name = "move_type")
    private String moveType;

    @Column(name = "down_payment")
    private Integer downPayment;

    // @Column(name = "approval_status")
    // private Integer approvalStatus;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_date")
    @Temporal(TemporalType.DATE)
    private Date approvedDate;

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

    @Column(name = "order_status")
    private int orderStatus;

    @Column(name = "qty_120mtfl")
    private Integer qty120mtfl;

    @Column(name = "qty_120mt")
    private Integer qty120mt;

    @Column(name = "qty_220mtfl")
    private Integer qty220mtfl;

    @Column(name = "qty_220mt")
    private Integer qty220mt;

    @Column(name = "qty_140mtfl")
    private Integer qty140mtfl;

    @Column(name = "qty_140mt")
    private Integer qty140mt;

    @Column(name = "qty_120mt120fl")
    private Integer qty120mt120fl;

    @Column(name = "qty_120mt220fl")
    private Integer qty120mt220fl;

    @Column(name = "qty_220mt120fl")
    private Integer qty220mt120fl;

    @Column(name = "qty_220mt220fl")
    private Integer qty220mt220fl;

    @Column(name = "qty_ch120fl")
    private Integer qtyCh120fl;

    @Column(name = "qty_ch220fl")
    private Integer qtyCh220fl;

    @Column(name = "qty_ch140fl")
    private Integer qtyCh140fl;

}
