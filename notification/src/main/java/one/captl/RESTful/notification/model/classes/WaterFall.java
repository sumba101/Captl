package one.captl.RESTful.notification.model.classes;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Data
@AllArgsConstructor
public class WaterFall {
    private String nameOfPortfolio;

    private Long equityPercentage; // represented as 10 for 10% and so on

    private BigDecimal totalInvestment;

    private BigDecimal exitValue; /// Distributed contribution for the exit value can be generated with the distribution-contribution api in capital calls

    private BigDecimal miscellaneousCost;

    private Boolean retain;

    private Optional<BigDecimal> totalAdjustment;

    private LocalDate fromDate;
    private LocalDate toDate;

}
