package one.captl.RESTful.notification.service;

import one.captl.RESTful.notification.dao.DelegationRepository;
import one.captl.RESTful.notification.feignclients.DocumentsFeignClient;
import one.captl.RESTful.notification.feignclients.ServiceProviderFeignClient;
import one.captl.RESTful.notification.feignclients.VintageFeignClient;
import one.captl.RESTful.notification.model.classes.*;
import one.captl.RESTful.notification.model.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskDelegationService {

    @Autowired
    @Qualifier(value = "delegatedTasks")
    private DelegationRepository delegationRepo;

    @Autowired
    private DocumentsFeignClient documentsFeignClient;

    @Autowired
    NotificationService notificationService;

    @Autowired
    NotifServices notifServices;

    @Autowired
    private ServiceProviderFeignClient serviceProviderFeignClient;
    @Autowired
    private VintageFeignClient vintageFeignClient;


    //Addition functions
    public String addDelegatedTask(DelegationType delegationType, IdType idType, Long accessId, Long assignedBy, IdType assignedToIdType, Long assignedTo, Optional<FilingType> filingType, Optional<DocumentType> documentType, List<Long> masterUsersToBeNotified, List<Long> slaveUsersToBeNotified, LocalDateTime deadline) {
        Delegations delegations = new Delegations();
        if (delegationType.equals(DelegationType.Document)) {
            if (documentType.isEmpty()) {
                throw new RuntimeException("Document type needed");
            }
            delegations.setDocumentType(documentType.get());
            // We are putting id type as user and giving the assignedBy as the accessId cos this should be kinda like property of the assignedby user
            // Possible write users are everyone in the assignedToIdType and assignedTo id
            List<Long> toIds;
            if (assignedToIdType.equals(IdType.User)) {
                toIds = List.of(assignedTo);
            } else if (assignedToIdType.equals(IdType.ServiceProvider)) {
                toIds = serviceProviderFeignClient.getSPTeam(assignedTo);
                if (toIds == null) {
                    throw new RuntimeException("service provider microservice unavailable");
                }
            } else if (assignedToIdType.equals(IdType.PortfolioRoundId)) {
                toIds = vintageFeignClient.getPortfolioRoundTeamWithAccessId(assignedTo);
                if (toIds == null) {
                    throw new RuntimeException("Fund microservice unavailable");
                }
            } else {
                throw new RuntimeException("AssignedToType not allowed");
            }
            long docContainerId = documentsFeignClient.initiateDocContainer(IdType.User, assignedBy, documentType.get(), assignedBy, toIds, List.of(assignedBy));
            if (docContainerId == -1) {
                throw new RuntimeException("Document service unavailable");
            }
            delegations.setDocumentContainerId(docContainerId);
        }

        else if (delegationType.equals(DelegationType.InfoFiling)){
            if(filingType.isEmpty())
                throw new RuntimeException("FilingType is needed");
            delegations.setInfoFilingType(filingType.get());
        }

        delegations.setIdType(idType);
        delegations.setAccessId(accessId);
        delegations.setAssignedBy(assignedBy);
        delegations.setAssignedTo(assignedTo);
        delegations.setAssignToIdType(assignedToIdType);
        delegations.setDeadline(deadline);
        delegations.setMasterSubscribers(masterUsersToBeNotified);
        delegations.setSlaveSubscribers(slaveUsersToBeNotified);

        delegations.setDelegationType(delegationType.name());
        delegations = delegationRepo.save(delegations);
        Long delegationId=delegations.getId();
        for (Long userid :
                masterUsersToBeNotified) {
            Long id=notifServices.addEntryDelegation(delegationId, DelegatedStatus.Assigned, false, IdType.User, assignedBy, assignedToIdType, assignedTo, delegationType, false,documentType,filingType);
            notificationService.addNotification(userid, NotificationType.Delegation,id,idType, accessId);
        }

        for (Long userid :
                slaveUsersToBeNotified) {
            Long id=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Assigned, true, IdType.User, assignedBy, assignedToIdType, assignedTo, delegationType,false,documentType,filingType);
            notificationService.addNotification(userid, NotificationType.Delegation,id,idType, accessId);
        }


        return String.valueOf(delegationId);
    }

    //Getter functions
    public Delegations getById(long Id) {
        if (delegationRepo.findById(Id).isEmpty()) {
            throw new RuntimeException("Task not found");
        }
        return delegationRepo.findById(Id).get();
    }

    public List<Delegations> getByAssignedTo(Long id, IdType assignToIdType) {
        if (delegationRepo.findAllByAssignedToAndAssignToIdType(id, assignToIdType).isEmpty()) {
            throw new RuntimeException("Person has not been assigned anything pending");
        }
        return delegationRepo.findAllByAssignedToAndAssignToIdType(id, assignToIdType).get();
    }

    public List<Delegations> getByAssignedBy(Long id) {
        if (delegationRepo.findAllByAssignedBy(id).isEmpty()) {
            throw new RuntimeException("Person has not assigned anything pending");
        }
        return delegationRepo.findAllByAssignedBy(id).get();
    }

    public Object getDelegationObject(Long id, DelegationType delegationType){
        if (delegationRepo.findById(id).isEmpty()) {
            throw new RuntimeException("Person has not assigned anything pending");
        }
        Delegations delegations= delegationRepo.findById(id).get();
        if(DelegationType.valueOf(delegations.getDelegationType())!=delegationType)
            throw new RuntimeException("Object is not of given delegation type");
        return delegations.getDelegationObject();
    }


    //Update functions
    public void updateDocTaskCompletion(long id,Long docId) {
        if (delegationRepo.findById(id).isEmpty()){
            throw new RuntimeException("Task not found");
        }
        Delegations delegations = delegationRepo.findById(id).get();
        if(DelegationType.valueOf(delegations.getDelegationType())!=DelegationType.Document)
            throw new RuntimeException("Object is not of Document delegation");

        delegations.setCompleted(true);
        delegations.setDelegationObject(docId);
        delegationRepo.save(delegations);


        DelegationType delegationType=DelegationType.valueOf(delegations.getDelegationType());
        Long assignedBy = delegations.getAssignedBy();
        IdType assignedToIdType=delegations.getAssignToIdType();
        Long assignedToId=delegations.getAssignedTo();
        FilingType filingType=delegations.getInfoFilingType();
        DocumentType documentType=delegations.getDocumentType();
        Long delegationId=delegations.getId();

        List<Long> masterUsersToBeNotified=delegations.getMasterSubscribers();
        List<Long> slaveUsersToBeNotified=delegations.getSlaveSubscribers();

        for (Long userid :
                masterUsersToBeNotified) {
            Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Completed, false, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType, false,Optional.ofNullable(documentType),Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }

        for (Long userid :
                slaveUsersToBeNotified) {
            Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Completed, true, IdType.User, assignedBy, assignedToIdType,assignedToId, delegationType,false, Optional.ofNullable(documentType),Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }
    }


    public void updateCaptableCompletion(long id, Captable captable) {
        if (delegationRepo.findById(id).isEmpty()){
            throw new RuntimeException("Task not found");
        }
        Delegations delegations = delegationRepo.findById(id).get();
        if(DelegationType.valueOf(delegations.getDelegationType())!=DelegationType.CapTable)
            throw new RuntimeException("Object is not of Captable delegation");

        delegations.setDelegationObject(captable);
        delegationRepo.save(delegations);
        delegations.setCompleted(true);

        DelegationType delegationType=DelegationType.valueOf(delegations.getDelegationType());


        Long assignedBy = delegations.getAssignedBy();
        IdType assignedToIdType=delegations.getAssignToIdType();
        Long assignedToId=delegations.getAssignedTo();
        FilingType filingType=delegations.getInfoFilingType();
        DocumentType documentType=delegations.getDocumentType();
        Long delegationId=delegations.getId();

        List<Long> masterUsersToBeNotified=delegations.getMasterSubscribers();
        List<Long> slaveUsersToBeNotified=delegations.getSlaveSubscribers();

        for (Long userid :
                masterUsersToBeNotified) {
            Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Completed, false, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType, false,Optional.ofNullable(documentType),Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }

        for (Long userid :
                slaveUsersToBeNotified) {
            Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Completed, true, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType, false,Optional.ofNullable(documentType),Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }
    }


    public void updateEstablishmentFeesCompletion(long id, EstablishmentExpenses establishmentExpenses) {
        if (delegationRepo.findById(id).isEmpty()) {
            throw new RuntimeException("Task not found");
        }
        Delegations delegations = delegationRepo.findById(id).get();
        if (DelegationType.valueOf(delegations.getDelegationType()) != DelegationType.EstablishmentExpenses)
            throw new RuntimeException("Object is not of Establishment expenses delegation");

        delegations.setDelegationObject(establishmentExpenses);
        delegationRepo.save(delegations);
        delegations.setCompleted(true);

        DelegationType delegationType=DelegationType.valueOf(delegations.getDelegationType());


        Long assignedBy = delegations.getAssignedBy();
        IdType assignedToIdType=delegations.getAssignToIdType();
        Long assignedToId=delegations.getAssignedTo();
        FilingType filingType=delegations.getInfoFilingType();
        DocumentType documentType=delegations.getDocumentType();
        Long delegationId=delegations.getId();

        List<Long> masterUsersToBeNotified=delegations.getMasterSubscribers();
        List<Long> slaveUsersToBeNotified=delegations.getSlaveSubscribers();

        for (Long userid :
                masterUsersToBeNotified) {
            Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Completed, false, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType, false,Optional.ofNullable(documentType),Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }

        for (Long userid :
                slaveUsersToBeNotified) {
            Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Completed, true, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType, false,Optional.ofNullable(documentType),Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }
    }


    public void updateOperationalFeesCompletion(long id, OperationalExpenses operationalExpenses) {
        if (delegationRepo.findById(id).isEmpty()) {
            throw new RuntimeException("Task not found");
        }
        Delegations delegations = delegationRepo.findById(id).get();
        if (DelegationType.valueOf(delegations.getDelegationType()) != DelegationType.OperationalExpenses)
            throw new RuntimeException("Object is not of Operational Expenses delegation");

        delegations.setDelegationObject(operationalExpenses);
        delegationRepo.save(delegations);
        delegations.setCompleted(true);

        DelegationType delegationType=DelegationType.valueOf(delegations.getDelegationType());


        Long assignedBy = delegations.getAssignedBy();
        IdType assignedToIdType=delegations.getAssignToIdType();
        Long assignedToId=delegations.getAssignedTo();
        FilingType filingType=delegations.getInfoFilingType();
        DocumentType documentType=delegations.getDocumentType();
        Long delegationId=delegations.getId();

        List<Long> masterUsersToBeNotified=delegations.getMasterSubscribers();
        List<Long> slaveUsersToBeNotified=delegations.getSlaveSubscribers();

        for (Long userid :
                masterUsersToBeNotified) {
            Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Completed, false, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType, false,Optional.ofNullable(documentType),Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }

        for (Long userid :
                slaveUsersToBeNotified) {
            Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Completed, true, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType, false,Optional.ofNullable(documentType),Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }
    }

    public void updatePortfolioInvestmentFeesCompletion(long id, PortfolioInvestments portfolioInvestments) {
        if (delegationRepo.findById(id).isEmpty()) {
            throw new RuntimeException("Task not found");
        }
        Delegations delegations = delegationRepo.findById(id).get();
        if (DelegationType.valueOf(delegations.getDelegationType()) != DelegationType.PortfolioInvestments)
            throw new RuntimeException("Object is not of PortfolioInvestments delegation");

        delegations.setDelegationObject(portfolioInvestments);
        delegationRepo.save(delegations);
        delegations.setCompleted(true);

        DelegationType delegationType=DelegationType.valueOf(delegations.getDelegationType());


        Long assignedBy = delegations.getAssignedBy();
        IdType assignedToIdType=delegations.getAssignToIdType();
        Long assignedToId=delegations.getAssignedTo();
        FilingType filingType=delegations.getInfoFilingType();
        DocumentType documentType=delegations.getDocumentType();
        Long delegationId=delegations.getId();

        List<Long> masterUsersToBeNotified=delegations.getMasterSubscribers();
        List<Long> slaveUsersToBeNotified=delegations.getSlaveSubscribers();

        for (Long userid :
                masterUsersToBeNotified) {
            Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Completed, false, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType, false,Optional.ofNullable(documentType),Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }

        for (Long userid :
                slaveUsersToBeNotified) {
            Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Completed, true, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType, false,Optional.ofNullable(documentType),Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }
    }

    public void updateDistributionWaterfallCompletion(long id, WaterFall waterFall) {
        if (delegationRepo.findById(id).isEmpty()) {
            throw new RuntimeException("Task not found");
        }
        Delegations delegations = delegationRepo.findById(id).get();
        if (DelegationType.valueOf(delegations.getDelegationType()) != DelegationType.DistributionWaterfall)
            throw new RuntimeException("Object is not of Distribution Waterfall delegation");

        delegations.setDelegationObject(waterFall);
        delegationRepo.save(delegations);
        delegations.setCompleted(true);

        DelegationType delegationType=DelegationType.valueOf(delegations.getDelegationType());


        Long assignedBy = delegations.getAssignedBy();
        IdType assignedToIdType=delegations.getAssignToIdType();
        Long assignedToId=delegations.getAssignedTo();
        FilingType filingType=delegations.getInfoFilingType();
        DocumentType documentType=delegations.getDocumentType();
        Long delegationId=delegations.getId();

        List<Long> masterUsersToBeNotified=delegations.getMasterSubscribers();
        List<Long> slaveUsersToBeNotified=delegations.getSlaveSubscribers();

        for (Long userid :
                masterUsersToBeNotified) {
            Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Completed, false, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType, false,Optional.ofNullable(documentType),Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }

        for (Long userid :
                slaveUsersToBeNotified) {
            Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Completed, true, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType, false,Optional.ofNullable(documentType),Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }
    }


    public void updateManagementFeesCompletion(long id, ManagementFees managementFees) {
        if (delegationRepo.findById(id).isEmpty()) {
            throw new RuntimeException("Task not found");
        }
        Delegations delegations = delegationRepo.findById(id).get();
        if (DelegationType.valueOf(delegations.getDelegationType()) != DelegationType.ManagementFees)
            throw new RuntimeException("Object is not of Management Fees delegation");

        delegations.setDelegationObject(managementFees);
        delegationRepo.save(delegations);
        delegations.setCompleted(true);

        DelegationType delegationType=DelegationType.valueOf(delegations.getDelegationType());

        Long assignedBy = delegations.getAssignedBy();
        IdType assignedToIdType=delegations.getAssignToIdType();
        Long assignedToId=delegations.getAssignedTo();
        FilingType filingType=delegations.getInfoFilingType();
        DocumentType documentType=delegations.getDocumentType();
        Long delegationId=delegations.getId();

        List<Long> masterUsersToBeNotified=delegations.getMasterSubscribers();
        List<Long> slaveUsersToBeNotified=delegations.getSlaveSubscribers();

        for (Long userid :
                masterUsersToBeNotified) {
            Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Completed, false, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType, false,Optional.ofNullable(documentType),Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }

        for (Long userid :
                slaveUsersToBeNotified) {
            Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Completed, true, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType, false,Optional.ofNullable(documentType),Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }
    }



    public void updateInfoTaskCompletion(long id, InfoFilling infoFilling) {
        if (delegationRepo.findById(id).isEmpty()){
            throw new RuntimeException("Task not found");
        }
        Delegations delegations = delegationRepo.findById(id).get();
        if(DelegationType.valueOf(delegations.getDelegationType())!=DelegationType.InfoFiling)
            throw new RuntimeException("Object is not of Infofiling delegation");

        delegations.setCompleted(true);
        delegations.setDelegationObject(infoFilling);
        delegationRepo.save(delegations);

        DelegationType delegationType=DelegationType.valueOf(delegations.getDelegationType());


        Long assignedBy = delegations.getAssignedBy();
        IdType assignedToIdType=delegations.getAssignToIdType();
        Long assignedToId=delegations.getAssignedTo();
        FilingType filingType=delegations.getInfoFilingType();
        DocumentType documentType=delegations.getDocumentType();
        Long delegationId=delegations.getId();

        List<Long> masterUsersToBeNotified=delegations.getMasterSubscribers();
        List<Long> slaveUsersToBeNotified=delegations.getSlaveSubscribers();

        for (Long userid :
                masterUsersToBeNotified) {
            Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Completed, false, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType, false,Optional.ofNullable(documentType),Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }

        for (Long userid :
                slaveUsersToBeNotified) {
            Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Completed, true, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType, false,Optional.ofNullable(documentType),Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }
    }

    public void addMasterSubscriber(Long id,List<Long> addedMasters){
        if (delegationRepo.findById(id).isEmpty()){
            throw new RuntimeException("Task not found");
        }
        Delegations delegations = delegationRepo.findById(id).get();
        delegations.setMasterSubscribers(addedMasters);
        delegationRepo.save(delegations);
    }

    public void addSlaveSubscriber(Long id,List<Long> addedSlaves){
        if (delegationRepo.findById(id).isEmpty()){
            throw new RuntimeException("Task not found");
        }
        Delegations delegations = delegationRepo.findById(id).get();
        delegations.setSlaveSubscribers(addedSlaves);
        delegationRepo.save(delegations);
    }

    public void updateDeadline(Long id,LocalDateTime deadline){
        if (delegationRepo.findById(id).isEmpty()){
            throw new RuntimeException("Task not found");
        }
        Delegations delegations = delegationRepo.findById(id).get();
        delegations.setDeadline(deadline);
        delegationRepo.save(delegations);
    }


    public void updateAssignedTo(long Id,IdType assignToIdType,Long assignedTo) {
        if (delegationRepo.findById(Id).isEmpty()){
            throw new RuntimeException("Task not found");
        }
        Delegations delegations = delegationRepo.findById(Id).get();
        delegations.setAssignedTo(assignedTo);
        delegations.setAssignToIdType(assignToIdType);
        delegationRepo.save(delegations);

        DelegationType delegationType=DelegationType.valueOf(delegations.getDelegationType());
        Long assignedBy = delegations.getAssignedBy();
        IdType assignedToIdType=delegations.getAssignToIdType();
        Long assignedToId=delegations.getAssignedTo();
        FilingType filingType=delegations.getInfoFilingType();
        DocumentType documentType=delegations.getDocumentType();
        Long delegationId=delegations.getId();

        List<Long> masterUsersToBeNotified=delegations.getMasterSubscribers();
        List<Long> slaveUsersToBeNotified=delegations.getSlaveSubscribers();

        for (Long userid :
                masterUsersToBeNotified) {
            Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Assigned, false, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType,true, Optional.ofNullable(documentType),Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }

        for (Long userid :
                slaveUsersToBeNotified) {
            Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Assigned, true, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType,true, Optional.ofNullable(documentType),Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }

    }

    public void updateDelegationTask(long Id,AllActivities allActivities,DelegationType delegationType){
        if (delegationRepo.findById(Id).isEmpty()){
            throw new RuntimeException("Task not found");
        }

        if(!(delegationType==DelegationType.Document || delegationType==DelegationType.InfoFiling))
            throw new RuntimeException("Delegation type to change to must be infofiling or document type");

        Delegations delegations = delegationRepo.findById(Id).get();
        if (delegations.getCompleted()) {
            throw new RuntimeException("Task is completed. Cant change delegation type");
        }

        Long assignedBy = delegations.getAssignedBy();
        IdType assignedToIdType = delegations.getAssignToIdType();
        Long assignedToId = delegations.getAssignedTo();
        FilingType filingType = delegations.getInfoFilingType();
        DocumentType documentType = delegations.getDocumentType();
        Long delegationId = delegations.getId();

        List<Long> masterUsersToBeNotified = delegations.getMasterSubscribers();
        List<Long> slaveUsersToBeNotified = delegations.getSlaveSubscribers();
        DelegationType init_delegationtype=DelegationType.valueOf(delegations.getDelegationType());

        for (Long userid :
                masterUsersToBeNotified) {
            Long nid = notifServices.addEntryDelegation(delegationId, DelegatedStatus.Removed, false, IdType.User, assignedBy, assignedToIdType, assignedToId, init_delegationtype, false, Optional.ofNullable(documentType), Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation, nid, delegations.getIdType(), delegations.getAccessId());
        }

        for (Long userid :
                slaveUsersToBeNotified) {
            Long nid = notifServices.addEntryDelegation(delegationId, DelegatedStatus.Removed, true, IdType.User, assignedBy, assignedToIdType, assignedToId, init_delegationtype, false, Optional.ofNullable(documentType), Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation, nid, delegations.getIdType(), delegations.getAccessId());
        }

        if (delegationType.equals(DelegationType.InfoFiling)) {
            delegations.setDelegationType(delegationType.name());
            delegations.setInfoFilingType(FilingType.valueOf(allActivities.name()));
            delegations.setDocumentType(null);
        }
        else {
            delegations.setDelegationType(delegationType.name());
            delegations.setDocumentType(DocumentType.valueOf(allActivities.name()));
            delegations.setDelegationType(null);
        }
        delegationRepo.save(delegations);


        filingType = delegations.getInfoFilingType();
        documentType = delegations.getDocumentType();
        delegationId = delegations.getId();

        for (Long userid :
                masterUsersToBeNotified) {
            Long nid = notifServices.addEntryDelegation(delegationId, DelegatedStatus.Assigned, false, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType, false, Optional.ofNullable(documentType), Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation, nid, delegations.getIdType(), delegations.getAccessId());
        }

        for (Long userid :
                slaveUsersToBeNotified) {
            Long nid = notifServices.addEntryDelegation(delegationId, DelegatedStatus.Assigned, true, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType, false, Optional.ofNullable(documentType), Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }


    }

    //Deletion functions
    public void deleteById(long Id) {
        if (delegationRepo.findById(Id).isEmpty()){
            throw new RuntimeException("Task not found");
        }
        Delegations delegations=delegationRepo.findById(Id).get();


        Long assignedBy = delegations.getAssignedBy();
        IdType assignedToIdType=delegations.getAssignToIdType();
        Long assignedToId=delegations.getAssignedTo();
        FilingType filingType=delegations.getInfoFilingType();
        DocumentType documentType=delegations.getDocumentType();
        Long delegationId=delegations.getId();
        DelegationType delegationType=DelegationType.valueOf(delegations.getDelegationType());


        List<Long> masterUsersToBeNotified=delegations.getMasterSubscribers();
        List<Long> slaveUsersToBeNotified=delegations.getSlaveSubscribers();

        for (Long userid :
                masterUsersToBeNotified) {
            Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Removed, false, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType,false, Optional.ofNullable(documentType),Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }

        for (Long userid :
                slaveUsersToBeNotified) {
            Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Removed, true, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType,false, Optional.ofNullable(documentType),Optional.ofNullable(filingType));
            notificationService.addNotification(userid, NotificationType.Delegation,nid,delegations.getIdType(), delegations.getAccessId());
        }
        delegationRepo.deleteById(Id);

    }

    public void deleteByAssignedTo(IdType assignedToIdType,Long assignedTo) {
        if (delegationRepo.findAllByAssignedToAndAssignToIdType(assignedTo, assignedToIdType).isEmpty()) {
            throw new RuntimeException("Person has not been assigned anything pending");
        }
        List<Delegations> delegations = delegationRepo.findAllByAssignedToAndAssignToIdType(assignedTo, assignedToIdType).get();
        for (Delegations del :
                delegations) {

            Long assignedBy = del.getAssignedBy();
            FilingType filingType=del.getInfoFilingType();
            DocumentType documentType=del.getDocumentType();
            Long delegationId=del.getId();
            DelegationType delegationType=DelegationType.valueOf(del.getDelegationType());

            List<Long> masterUsersToBeNotified=del.getMasterSubscribers();
            List<Long> slaveUsersToBeNotified=del.getSlaveSubscribers();

            for (Long userid :
                    masterUsersToBeNotified) {
                Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Removed, false, IdType.User, assignedBy, assignedToIdType, assignedTo, delegationType,false, Optional.ofNullable(documentType),Optional.ofNullable(filingType));
                notificationService.addNotification(userid, NotificationType.Delegation,nid,del.getIdType(), del.getAccessId());
            }

            for (Long userid :
                    slaveUsersToBeNotified) {
                Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Removed, true, IdType.User, assignedBy, assignedToIdType, assignedTo, delegationType,false, Optional.ofNullable(documentType),Optional.ofNullable(filingType));
                notificationService.addNotification(userid, NotificationType.Delegation,nid,del.getIdType(), del.getAccessId());
            }
        }
        delegationRepo.deleteAllByAssignedToAndAssignToIdType(assignedTo, assignedToIdType);

    }

    public void deleteByAssignedBy(Long assignedBy) {
        if (delegationRepo.findAllByAssignedBy(assignedBy).isEmpty()){
            throw new RuntimeException("Person has not assigned anything pending");
        }

        List<Delegations> delegations=delegationRepo.findAllByAssignedBy(assignedBy).get();
        for (Delegations del :
                delegations) {

            IdType assignedToIdType=del.getAssignToIdType();
            Long assignedToId=del.getAssignedTo();
            DelegationType delegationType=DelegationType.valueOf(del.getDelegationType());

            FilingType filingType=del.getInfoFilingType();
            DocumentType documentType=del.getDocumentType();
            Long delegationId=del.getId();

            List<Long> masterUsersToBeNotified=del.getMasterSubscribers();
            List<Long> slaveUsersToBeNotified=del.getSlaveSubscribers();

            for (Long userid :
                    masterUsersToBeNotified) {
                Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Removed, false, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType,false, Optional.ofNullable(documentType),Optional.ofNullable(filingType));
                notificationService.addNotification(userid, NotificationType.Delegation,nid,del.getIdType(), del.getAccessId());
            }

            for (Long userid :
                    slaveUsersToBeNotified) {
                Long nid=notifServices.addEntryDelegation(delegationId,DelegatedStatus.Removed, true, IdType.User, assignedBy, assignedToIdType, assignedToId, delegationType,false, Optional.ofNullable(documentType),Optional.ofNullable(filingType));
                notificationService.addNotification(userid, NotificationType.Delegation,nid,del.getIdType(), del.getAccessId());
            }
        }

        delegationRepo.deleteAllByAssignedBy(assignedBy);
    }
    
    public void deleteMasterSubscriber(Long Id,Long uid){
        if (delegationRepo.findById(Id).isEmpty()){
            throw new RuntimeException("Task not found");
        }

        Delegations delegations = delegationRepo.findById(Id).get();
        List<Long> mList = delegations.getMasterSubscribers();
        mList.remove(uid);
        delegations.setMasterSubscribers(mList);
        delegationRepo.save(delegations);
    }

    public void deleteSlaveSubscriber(Long Id,Long uid){
        if (delegationRepo.findById(Id).isEmpty()){
            throw new RuntimeException("Task not found");
        }

        Delegations delegations = delegationRepo.findById(Id).get();
        List<Long> sList = delegations.getSlaveSubscribers();
        sList.remove(uid);
        delegations.setSlaveSubscribers(sList);
        delegationRepo.save(delegations);

    }

}
