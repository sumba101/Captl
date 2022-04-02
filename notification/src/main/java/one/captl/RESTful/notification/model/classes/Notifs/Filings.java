package one.captl.RESTful.notification.model.classes.Notifs;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Data;
import one.captl.RESTful.notification.model.enums.FilingType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "NotifFilings")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class Filings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(
            name = "userid",
            columnDefinition = "bigint"
    )
    private Long byUserId;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "FilingType",
            columnDefinition = "filingtype"
    )
    @Type(type="pgsql_enum")
    private FilingType filingType;

    @CreationTimestamp
    private LocalDateTime creationTime;


}
/*
    CREATE TYPE filingtype AS ENUM (
        'Details',
        'Team',
        'LP',
        'Portfolio',
        'Trustees',
        'AuditFirms',
        'LawFirms',
        'ValuationFirms',
        'Banks'
    );
*/