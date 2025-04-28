package be_sitruck.backend_sitruck.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "request_asset")
public class RequestAsset {

    @Id
    @Column(name = "request_asset_id", nullable = false, length = 100)
    private String requestAssetId;

    @OneToMany(mappedBy = "requestAsset", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestAssetItem> items;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "request_remark", length = 300)
    private String requestRemark;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "approval_by", length = 50)
    private String approvalBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "approval_date")
    private Date approvalDate;

}


