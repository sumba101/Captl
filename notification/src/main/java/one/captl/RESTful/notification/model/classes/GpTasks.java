package one.captl.RESTful.notification.model.classes;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import one.captl.RESTful.notification.model.enums.GpInfoTasks;
import one.captl.RESTful.notification.model.enums.AllActivities;
import one.captl.RESTful.notification.model.enums.VintageOneTimeDocs;
import one.captl.RESTful.notification.model.enums.VintageOngoing;
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
@Table(name = "GpTasks")
@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class
)
public class GpTasks {
    @Id
    private Long vintageRoundId;

    @Type(type = "jsonb")
    @Column(
            name = "StarredWork",
            columnDefinition = "jsonb"
    )
    private List<AllActivities> starredWork;

    @Type(type = "jsonb")
    @Column(
            name = "TasksStatus",
            columnDefinition = "jsonb"
    )
    private HashMap<GpInfoTasks,Boolean> infoTasks;

    @Type(type = "jsonb")
    @Column(
            name = "GPOneTimeDocs",
            columnDefinition = "jsonb"
    )
    private HashMap<VintageOneTimeDocs,Boolean> gpOneTimeDocs;

    @Type(type = "jsonb")
    @Column(
            name = "GPOngoingDocs",
            columnDefinition = "jsonb"
    )
    private HashMap<VintageOngoing,Boolean> gpOngoingDocs;

    @Type(type = "jsonb")
    @Column(
            name = "Deadlines",
            columnDefinition = "jsonb"
    )
    private HashMap<AllActivities,Frequencies> deadlines;

    @Type(type = "jsonb")
    @Column(
            name = "Reminders",
            columnDefinition = "jsonb"
    )
    private HashMap<AllActivities,Frequencies> reminders;

    @CreationTimestamp
    private LocalDateTime creationTime;

    @UpdateTimestamp
    private LocalDateTime lastModifiedTime;
}
