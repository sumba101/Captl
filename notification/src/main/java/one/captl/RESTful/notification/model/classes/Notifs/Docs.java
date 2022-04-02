package one.captl.RESTful.notification.model.classes.Notifs;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Data;
import one.captl.RESTful.notification.model.enums.DocStatusType;
import one.captl.RESTful.notification.model.enums.DocumentType;
import one.captl.RESTful.notification.model.enums.IdType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Data
@Entity
@Table(name = "NotifDocument")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class Docs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "DocumentType",
            columnDefinition = "doctype"
    )
    @Type(type="pgsql_enum")
    private DocumentType documentType;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "DocumentStatus",
            columnDefinition = "docstatus"
    )
    @Type(type="pgsql_enum")
    private DocStatusType docStatusType;


    private Boolean sharedNotification=false;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "ByEntityType",
            columnDefinition = "idtype"
    )
    @Type(type="pgsql_enum")
    private IdType byEntityType;

    @Column(
            name = "ByEntityAccessId",
            columnDefinition = "bigint"
    )
    private Long byEntityAccessId;


    @Enumerated(EnumType.STRING)
    @Column(
            name = "SharedToIdType",
            columnDefinition = "idtype"
    )
    @Type(type="pgsql_enum")
    private IdType shareToIdType;

    @Column(
            name = "SharedToId",
            columnDefinition = "bigint"
    )
    private Long sharedToId;
}
/*
* uploaded is only for the GPs, Shared is only between the LP and GP firms
* */
/*
    CREATE TYPE docstatus AS ENUM (
    'Uploaded','Review','Deleted','Shared','SharedTo'
    );

    CREATE TYPE idtype AS ENUM (
    'VintageId',
    'PortfolioRoundId',
    'LpRoundId',
    'User',
    'ServiceProvider'
    );

    CREATE TYPE doctype AS ENUM (
    'FundClosureDocs_privatePlacementMemo',
    'FundClosureDocs_contributionAgreement',

    'FundClosureDocs_financialDueDiligence',
    'FundClosureDocs_legalDueDiligence',

    'FundClosureDocs_trusteeRep',
    'FundClosureDocs_investmentManageRep',
    'FundClosureDocs_conditionsPrecedentRep',

    'FundClosureDocs_legalOpinion',
    'FundClosureDocs_taxOpinion',
    'FundClosureDocs_sideLetter',

    'FundAdminReps_annualReport',
    'FundAdminReps_quarterlyReport',

    'FundAdminReps_halfYearlyValReport',
    'FundAdminReps_annualValReport',

    'FundAdminReps_auditedFinancials',
    'FundAdminReps_bankStatements',

    'FundCreation_articlesOfAssociation',
    'FundCreation_memoOfAssociation',
    'FundAdditionalInfo_otherDisclosures',

    'FundCreation_trustDeed',
    'FundCreation_investmentManageAgreement',
    'FundCreation_regulatoryApproval',
    'FundCreation_carryPolicy',

    'InvestmentDocs_termSheet',

    'InvestmentDocs_shareholdersAgreement',
    'InvestmentDocs_shareSubscriptionAgreement',
    'InvestmentDocs_conditionsPrecedent',
    'InvestmentDocs_postClosing',
    'InvestmentDocs_complianceReport',
    'InvestmentDocs_fundsReceipt',

    'InvestmentDocs_financialDueDiligence',
    'InvestmentDocs_legalDueDiligence',
    'InvestmentDocs_commercialDueDiligence',

    'OngoingOps_operatingPlan',
    'OngoingOps_boardPresentations',
    'OngoingOps_boardMins',
    'OngoingOps_auditedFinancials',
    'OngoingOps_complianceRep',
    'OngoingOps_bankStatements',

    'PortfolioAdditionalInfo_articlesOfAssociation',
    'PortfolioAdditionalInfo_memoOfAssociation',
    'PortfolioAdditionalInfo_otherDisclosures',

    'Lpdocs_regulatoryApproval',
    'Lpdocs_accreditedInvestorRep'
    );

*/