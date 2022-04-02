package one.captl.RESTful.notification.model.classes;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import one.captl.RESTful.notification.model.enums.LpDocs;
import one.captl.RESTful.notification.model.enums.LpInfoTasks;
import one.captl.RESTful.notification.model.enums.AllActivities;
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
@Table(name = "LpTasks")
@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class
)
public class LpTasks {
    @Id
    private Long lpRoundId;

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
    private HashMap<LpInfoTasks,Boolean> infoTasks;

    @Type(type = "jsonb")
    @Column(
            name = "LpOneTimeDocs",
            columnDefinition = "jsonb"
    )
    private HashMap<LpDocs,Boolean> lpOneTimeDocs;

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
