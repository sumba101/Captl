package one.captl.RESTful.notification.model.classes.Notifs;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Data;
import one.captl.RESTful.notification.model.enums.DelegatedStatus;
import one.captl.RESTful.notification.model.enums.DocumentType;
import one.captl.RESTful.notification.model.enums.FilingType;
import one.captl.RESTful.notification.model.enums.IdType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "NotifDelegation")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class DelegationNotifications {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "DelegatedStatus",
            columnDefinition = "delegatedstatus"
    )
    @Type(type="pgsql_enum")
    private DelegatedStatus delegatedStatus;

    @Column(
            name = "Receiver",
            columnDefinition = "boolean"
    )
    private Boolean receiver; //[1 is yes 0 is no]

    @Enumerated(EnumType.STRING)
    @Column(
            name = "MasterEntityType",
            columnDefinition = "idtype"
    )
    @Type(type="pgsql_enum")
    private IdType masterEntityType;

    @Column(
            name = "MasterEntityId",
            columnDefinition = "bigint"
    )
    private Long masterEntityId;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "SlaveEntityType",
            columnDefinition = "idtype"
    )
    @Type(type="pgsql_enum")
    private IdType slaveEntityType;

    @Column(
            name = "SlaveEntityId",
            columnDefinition = "bigint"
    )
    private Long slaveEntityId;

    private String delegationType; // restricted by an enum Delegation Type

    @Enumerated(EnumType.STRING)
    @Column(
            name = "DocumentType",
            columnDefinition = "doctype"
    )
    @Type(type="pgsql_enum")
    private DocumentType documentType;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "FilingType",
            columnDefinition = "filingtype"
    )
    @Type(type="pgsql_enum")
    private FilingType filingType;


    private Boolean reassigned=false; //If the task has been reassigned to a new dude

    private Long delegationId;


    @CreationTimestamp
    private LocalDateTime creationTime;

}
/*

CREATE TYPE delegatedstatus AS ENUM (
    'Assigned','Removed','Completed'
);
*
* If Reassignment is True, then the shown notification would be changed up accordingly, nothing else needs a change
 */