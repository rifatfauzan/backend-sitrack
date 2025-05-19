package be_sitruck.backend_sitruck.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "report_truck_asset")
public class ReportTruckAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // auto increment id

    @Column(name = "asset_id", nullable = false, length = 100)
    private String assetId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "report_truck_id")
    private ReportTruck reportTruck;
}
