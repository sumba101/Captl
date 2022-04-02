package one.captl.RESTful.notification.model.classes;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class PortfolioInvestments {
    private String nameOfPortfolio;
    MultipartFile document;

    private String overview;

    private String securitiesAcquired;

    @Max(value = 100)
    @Min(value = 0)
    private Long equityPercentage; // represented as 10 for 10% and so on

    private BigDecimal investmentAmount;
    Long fromUserId;
    List<Long> toIds;
    Boolean editedContribution = Boolean.FALSE;
    BigDecimal usedProreta;

    private LocalDateTime deadline;
    Map<Long, BigDecimal> anchorLps;
    Map<Long, BigDecimal> lps;
    Map<Long, BigDecimal> gps;
    Map<Long, BigDecimal> trustees;
    Map<Long, BigDecimal> fundFirm;
    private String roundName;
    private Boolean firstTime; // Set to false if it if follow on

}
