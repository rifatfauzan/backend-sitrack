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
@Table(name = "chassis")
public class Chassis implements Serializable {

    @Id
    @Column(name = "chassis_id", length = 8, nullable = false)
    private String chassisId;

    @Column(name = "chassis_size", length = 2)
    private String chassisSize;

    @Column(name = "chassis_year", length = 4)
    private String chassisYear;

    @Column(name = "chassis_number", length = 6)
    private String chassisNumber;

    @Column(name = "chassis_axle", length = 2)
    private String chassisAxle;

    @Column(name = "chassis_KIR_No", length = 20, nullable = false)
    private String chassisKIRNo;

    @Temporal(TemporalType.DATE)
    @Column(name = "chassis_KIR_date", nullable = false)
    private Date chassisKIRDate;

    @Column(name = "chassis_type", length = 1)
    private String chassisType;

    @Column(name = "chassis_remarks", length = 300)
    private String chassisRemarks;

    @Column(name = "inserted_by", length = 20)
    private String insertedBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "inserted_date")
    private Date insertedDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "division", length = 2, nullable = false)
    private String division;

    @Column(name = "dept", length = 2, columnDefinition = "CHAR(2) DEFAULT 'TR'")
    private String dept = "TR";

    @Column(name = "row_status", length = 1, columnDefinition = "CHAR(1) DEFAULT 'A'")
    private String rowStatus = "A";

    @Column(name = "site_id", length = 3, columnDefinition = "CHAR(3) DEFAULT 'JKT'")
    private String siteId = "JKT";
}
