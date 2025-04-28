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
@Table(name = "asset")
public class Asset {
    
    @Id
    @Column(name = "asset_id", nullable = false, length = 100)
    private String assetId;

    @Column(name = "jenis_asset", length = 100, nullable = false)
    private String jenisAsset;

    @Column(name = "jumlah_stok", nullable = false)
    private Integer jumlahStok;

    @Column(name = "brand", length = 100, nullable = false)
    private String brand;

    @Column(name = "asset_remark", length = 300)
    private String assetRemark;

    @Column(name = "requested_stok", nullable = false)
    private Integer requestedStok = 0;

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
}
