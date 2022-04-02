package one.captl.RESTful.notification.model.classes.Notifs;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Data;
import one.captl.RESTful.notification.model.enums.IdType;
import one.captl.RESTful.notification.model.enums.NetworkStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "NotifNetwork")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class Network {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "NetworkStatuses",
            columnDefinition = "networkstatus"
    )
    @Type(type="pgsql_enum")
    private NetworkStatus networkStatus;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "EntityFrame",
            columnDefinition = "idtype"
    )
    @Type(type="pgsql_enum")
    private IdType entityFrame;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "AddedEntity",
            columnDefinition = "idtype"
    )
    @Type(type="pgsql_enum")
    private IdType addedEntity;

    @Column(
            name = "AddedEntityAccessId",
            columnDefinition = "bigint"
    )
    private Long addedEntityAccessId;

    @CreationTimestamp
    private LocalDateTime creationTime;


}

/*

todo: Make network status enum in postgres
* Entity Frame dictates the possible values for the Added Entity
* Frame-Entity
* Gp-user,LPFirm,PortfolioFirm,ServiceProvider firm
* Lp-user,ServiceProviderFirm
* Portfolio-user,ServiceProviderFirm
* ServiceProviderFirm-user
* */
/*
    CREATE TYPE networkstatus AS ENUM (
        'Addition','Removed','Leaving'
    );

* */