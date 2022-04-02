package one.captl.RESTful.notification.model.classes;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Data;
import one.captl.RESTful.notification.model.enums.IdType;
import one.captl.RESTful.notification.model.enums.NotificationType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Notifications")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(
            name = "userid",
            columnDefinition = "bigint"
    )
    private Long userid;   // Who this notification is for

    @Enumerated(EnumType.STRING)
    @Column(
            name = "NotificationType",
            columnDefinition = "notificationtype"
    )
    @Type(type="pgsql_enum")
    private NotificationType notificationType;

    @Column(
            name = "notificationId",
            columnDefinition = "bigint"
    )
    private Long notificationId;

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

    @Column(
            name = "Read",
            columnDefinition = "boolean"
    )
    private Boolean read=false;

    private LocalDateTime notificationTime;

}
/*

CREATE TYPE notificationtype AS ENUM(
    'Filing',
    'Doc',
    'Delegation',
    'Network'
);

* */