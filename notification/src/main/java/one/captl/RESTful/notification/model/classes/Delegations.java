package one.captl.RESTful.notification.model.classes;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import one.captl.RESTful.notification.model.enums.DocumentType;
import one.captl.RESTful.notification.model.enums.FilingType;
import one.captl.RESTful.notification.model.enums.IdType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "DelegatedTasks")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class
)
@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
public class Delegations {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "AssignedToIdType",
            columnDefinition = "idtype"
    )
    @Type(type="pgsql_enum")
    private IdType assignToIdType;

    @Column(
            name = "AssignedTo",
            columnDefinition = "bigint"
    )
    private Long assignedTo;

    @Column(
            name = "AssignedBy",
            columnDefinition = "bigint"
    )
    private Long assignedBy;
    /// This has the userid of the person that did the assigning of tasks


    @CreationTimestamp
    private LocalDateTime creationTime;

    @UpdateTimestamp
    private LocalDateTime lastModifiedTime;


    @Enumerated(EnumType.STRING)
    @Column(
            name = "IdType",
            columnDefinition = "idtype"
    )
    @Type(type="pgsql_enum")
    private IdType idType;

    /*
    Depending upon the Id type this can be:
        vintage id
        lp round id
        portfolio round id
    */
    @Column(
            name = "accessid",
            columnDefinition = "bigint"
    )
    private Long accessId;

    LocalDateTime deadline;

    @Type(type = "list-array")
    @Column(
            name = "masterSubscribers",
            columnDefinition = "bigint[]"
    )
    private List<Long> masterSubscribers;

    @Type(type = "list-array")
    @Column(
            name = "slaveSubscribers",
            columnDefinition = "bigint[]"
    )
    private List<Long> slaveSubscribers;

    @Column(
            name = "CompletionStatus",
            columnDefinition = "boolean"
    )
    private Boolean completed=false; //default start state

    @Enumerated(EnumType.STRING)
    @Column(
            name = "Filingtype",
            columnDefinition = "filingtype"
    )
    @Type(type="pgsql_enum")
    private FilingType infoFilingType; // From the Id Type

    @Enumerated(EnumType.STRING)
    @Column(
            name = "DocumentType",
            columnDefinition = "doctype"
    )
    @Type(type = "pgsql_enum")
    private DocumentType documentType = null;

    private Long documentContainerId;

    private String delegationType; // restricted by an enum Delegation Type

    @Type(type = "jsonb")
    @Column(
            name = "DelegationObject",
            columnDefinition = "jsonb"
    )
    private Object delegationObject;
/*
    Should cover:-
        1)InfoFilling object : InfoFilling
        2)Document Id : Long
        3)Captable object : CapTable
        4)Management Fees :
        5)Establishment Expenses :
        6)Operational Expenses :
        7)Portfolio Investments :

 */


}
