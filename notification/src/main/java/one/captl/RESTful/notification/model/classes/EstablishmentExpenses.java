package one.captl.RESTful.notification.model.classes;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class EstablishmentExpenses {
    private BigDecimal usedAmount;
    MultipartFile document;
    Long fromUserId;
    List<Long> toIds;

    private LocalDateTime deadline;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    Boolean editedContribution = Boolean.FALSE;
    BigDecimal usedProreta;
    Map<Long, BigDecimal> anchorLps;
    Map<Long, BigDecimal> lps;
    Map<Long, BigDecimal> gps;
    Map<Long, BigDecimal> trustees;
    Map<Long, BigDecimal> fundFirm;

}
