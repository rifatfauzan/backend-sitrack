package be_sitruck.backend_sitruck.model;

import jakarta.persistence.*;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "truck")
public class Truck {
    
    @Id
    @Column(name = "Vehicle_ID", updatable = false, nullable = false)
    private String vehicleId;
    
    @Column(name = "Vehicle_Brand", length = 20, nullable = false)
    private String vehicleBrand;
    
    @Column(name = "Vehicle_Year", length = 4, nullable = false)
    private String vehicleYear;
    
    @Column(name = "Vehicle_Plate_No", length = 10, nullable = false)
    private String vehiclePlateNo;
    
    @Column(name = "Vehicle_STNK_Date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date vehicleSTNKDate;
    
    @Column(name = "Vehicle_KIR_No", length = 20, nullable = false)
    private String vehicleKIRNo;
    
    @Column(name = "Vehicle_KIR_Date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date vehicleKIRDate;
    
    @Column(name = "Vehicle_Cylinder", length = 5)
    private String vehicleCylinder;
    
    @Column(name = "Vehicle_Chassis_No", length = 20)
    private String vehicleChassisNo;
    
    @Column(name = "Vehicle_Engine_No", length = 20)
    private String vehicleEngineNo;
    
    @Column(name = "Vehicle_Biz_License_No", length = 20)
    private String vehicleBizLicenseNo;
    
    @Column(name = "Vehicle_Biz_License_Date")
    @Temporal(TemporalType.DATE)
    private Date vehicleBizLicenseDate;
    
    @Column(name = "Vehicle_Dispensation_No", length = 20)
    private String vehicleDispensationNo;
    
    @Column(name = "Vehicle_Dispensation_Date")
    @Temporal(TemporalType.DATE)
    private Date vehicleDispensationDate;
    
    @Column(name = "Vehicle_Remarks", length = 300)
    private String vehicleRemarks;
    
    @Column(name = "Site_ID", length = 3, columnDefinition = "char(3) default 'JKT'")
    private String siteId;
    
    @Column(name = "Vehicle_Type", length = 20)
    private String vehicleType;

    @Column(name = "division", length = 2, nullable = false)
    private String division;

    @Column(name = "dept", length = 2, columnDefinition = "CHAR(2) DEFAULT 'TR'")
    private String dept = "TR";

    @Column(name = "record_status", length = 1, columnDefinition = "CHAR(1) DEFAULT 'A'")
    private String recordStatus = "A";

    @Column(name = "row_status", length = 1, columnDefinition = "CHAR(1) DEFAULT 'A'")
    private String rowStatus = "A";
    
    @Column(name = "Vehicle_Number", length = 6)
    private String vehicleNumber;
    
    @Column(name = "Inserted_By", length = 20)
    private String insertedBy;
    
    @Column(name = "Inserted_Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertedDate;
    
    @Column(name = "Updated_By", length = 20)
    private String updatedBy;
    
    @Column(name = "Updated_Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    
}
