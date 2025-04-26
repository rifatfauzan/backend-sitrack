package be_sitruck.backend_sitruck.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "request_asset_item")
public class RequestAssetItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // auto increment id

    @ManyToOne
    @JoinColumn(name = "request_asset_id", nullable = false)
    private RequestAsset requestAsset;

    @Column(name = "asset_id", nullable = false, length = 100)
    private String assetId;

    @Column(name = "requested_quantity", nullable = false)
    private Integer requestedQuantity;
}
