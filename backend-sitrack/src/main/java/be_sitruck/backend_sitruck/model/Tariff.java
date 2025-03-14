package be_sitruck.backend_sitruck.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tariff")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tariff {

    @Id
    @Column(name = "tariff_id", nullable = false)
    private String tariffId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "std_tariff", nullable = false)
    private int stdTariff;

    @Column(name = "insurance")
    private int insurance;

    @Column(name = "tips")
    private int tips;

    @Column(name = "police")
    private int police;

    @Column(name = "LOLO")
    private int LOLO;

    @Column(name = "others")
    private int others;

    @Column(name = "total_tariff")
    private int totalTariff;
}
