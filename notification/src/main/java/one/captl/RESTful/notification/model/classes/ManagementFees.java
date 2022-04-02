package one.captl.RESTful.notification.model.classes;

import lombok.Data;
import one.captl.RESTful.notification.model.enums.ManagementFeeType;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class ManagementFees {
    private BigDecimal usedAmount; // Can calculate the percentage from this value

    MultipartFile document;
    Long fromUserId;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;
    List<Long> toIds;
    Boolean editedContribution = Boolean.FALSE;
    Map<Long, BigDecimal> anchorLps;

    private LocalDateTime deadline;
    Map<Long, BigDecimal> lps;
    Boolean fromDistribution = Boolean.FALSE;
    private BigDecimal writtenOff;
    private ManagementFeeType feeType; //Restricted with enum as flat percentage or step down
}
