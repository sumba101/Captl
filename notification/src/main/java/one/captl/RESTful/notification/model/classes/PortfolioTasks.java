package one.captl.RESTful.notification.model.classes;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import one.captl.RESTful.notification.model.enums.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Data
@Entity
@Table(name = "PortfolioTasks")
@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class
)
public class PortfolioTasks {
    @Id
    private Long portfolioRoundId;

    @Type(type = "jsonb")
    @Column(
            name = "StarredWork",
            columnDefinition = "jsonb"
    )
    private List<AllActivities> starredWork;

    @Type(type = "jsonb")
    @Column(
            name = "InfoTasks",
            columnDefinition = "jsonb"
    )
    private HashMap<PortfolioInfoTasks, Boolean> infoTasks; // DO NOT INITIALIZE ANY OF THESE WITH new HashMap(), these values being null is made use of elsewhere in the code

    @Type(type = "jsonb")
    @Column(
            name = "PortfolioRoundCreationDocs",
            columnDefinition = "jsonb"
    )
    private HashMap<PortfolioRoundCreationDocs, Boolean> portfolioRoundCreationDocs; // portfolio creation docs

    // Map it as VintageId -> HashMap<>
    @Type(type = "jsonb")
    @Column(
            name = "PortfolioOneTimeDocs",
            columnDefinition = "jsonb"
    )
    private HashMap<Long, HashMap<PortfolioOneTime, Boolean>> portfolioOneTimeDocs;

    @Type(type = "jsonb")
    @Column(
            name = "PortfolioOngoingDocs",
            columnDefinition = "jsonb"
    )
    private HashMap<Long, HashMap<PortfolioOngoing, Boolean>> portfolioOngoingDocs;


    @Type(type = "jsonb")
    @Column(
            name = "Deadlines",
            columnDefinition = "jsonb"
    )
    private HashMap<Long, HashMap<AllActivities, Frequencies>> deadlines;


    @Type(type = "jsonb")
    @Column(
            name = "Reminders",
            columnDefinition = "jsonb"
    )
    private HashMap<Long, HashMap<AllActivities, Frequencies>> reminders;
////////////////////////

    @CreationTimestamp
    private LocalDateTime creationTime;

    @UpdateTimestamp
    private LocalDateTime lastModifiedTime;
}
