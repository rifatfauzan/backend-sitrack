package be_sitruck.backend_sitruck.model;

import java.util.Date;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "sopir")
public class SopirModel {

    @Id
    private String driverId ;

    @NotNull
    @Size(max = 50)
    @Column(name = "driver_name")
    private String driverName;

    @NotNull
    @Size(min = 16, max = 16)
    @Column(name = "driver_ktp_no")
    private String driver_KTP_No;

    // @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "driver_ktp_date", columnDefinition = "DATE")
    private Date driver_KTP_Date;

    @NotNull
    @Size(min = 13, max = 13)
    @Column(name = "driver_sim_no")
    private String driver_SIM_No;

    // @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "driver_sim_date", columnDefinition = "DATE")
    private Date driver_SIM_Date;

    // @NotNull
    @Size(max = 300)
    @Column(name = "driver_contact")
    private String driverContact;

    // @NotNull
    @Size(max = 50)
    @Column(name = "driver_co")
    private String driverCo;

    // @NotNull
    @Size(max = 300)
    @Column(name = "driver_co_contact")
    private String driverCoContact;

    // @NotNull
    @Size(max = 3)
    @Column(name = "siteId")
    private String SiteId;

    // @NotNull
    @Size(max = 6)
    @Column(name = "driver_number")
    private String driverNumber;

    @Size(max=300)
    @Column(name = "driver_remarks")
    private String driverRemarks;

    @Size(max=1)
    @Column(name = "record_status")
    private String recordStatus;

    @Size(max=1)
    @Column(name = "driver_type")
    private String driverType;

    @Temporal(TemporalType.DATE)
    @Column(name = "driver_join_date", columnDefinition = "DATE")
    private Date driverJoinDate;

    @Size(max=1)
    @Column(name = "rowStatus")
    private String rowStatus;

    @Column(name = "created_by")
    private String createdBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_date", columnDefinition = "DATE")
    private Date createdDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated_date", columnDefinition = "DATE")
    private Date updatedDate;
}
