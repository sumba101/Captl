package one.captl.RESTful.notification.service;

import one.captl.RESTful.notification.dao.*;
import one.captl.RESTful.notification.model.classes.Notifs.DelegationNotifications;
import one.captl.RESTful.notification.model.classes.Notifs.Docs;
import one.captl.RESTful.notification.model.classes.Notifs.Filings;
import one.captl.RESTful.notification.model.classes.Notifs.Network;
import one.captl.RESTful.notification.model.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotifServices {
    @Autowired
    @Qualifier(value = "NotifDelegation")
    private NotifDelegation notifDelegation;

    @Autowired
    @Qualifier(value = "NotifFiling")
    private NotifFiling notifFiling;

    @Autowired
    @Qualifier(value = "NotifNetwork")
    private NotifNetwork notifNetwork;

    @Autowired
    @Qualifier(value = "NotifDoc")
    private NotifDoc notifDoc;

    @Autowired
    @Qualifier(value = "delegatedTasks")
    private DelegationRepository delegationRepo;

    public Long addEntryDelegation(Long delegationId, DelegatedStatus delegatedStatus, Boolean receiver, IdType masterEntityType,
                                   Long masterEntityId, IdType slaveEntityType, Long slaveEntityId, DelegationType delegationType, Boolean reassigned,
                                   Optional<DocumentType> documentType, Optional<FilingType> filingType) {
        if (delegationRepo.findById(delegationId).isEmpty()) {
            throw new RuntimeException("Delegated Task not found");
        }

        DelegationNotifications delegationNotifications = new DelegationNotifications();


        if (delegationType.equals(DelegationType.InfoFiling)||delegationType.equals(DelegationType.Document) ) {
            if(delegationType.equals(DelegationType.InfoFiling) && filingType.isPresent())
                delegationNotifications.setFilingType(filingType.get());
            else if(delegationType.equals(DelegationType.Document) && documentType.isPresent())
                delegationNotifications.setDocumentType(documentType.get());
            else
                throw new RuntimeException("DocumentType/FilingType is not present");
        }

        delegationNotifications.setDelegationType(delegationType.name());
        delegationNotifications.setDelegatedStatus(delegatedStatus);
        delegationNotifications.setReceiver(receiver);
        delegationNotifications.setMasterEntityType(masterEntityType);
        delegationNotifications.setMasterEntityId(masterEntityId);
        delegationNotifications.setSlaveEntityType(slaveEntityType);
        delegationNotifications.setSlaveEntityId(slaveEntityId);
        delegationNotifications.setReassigned(reassigned);
        delegationNotifications.setDelegationId(delegationId);
        delegationNotifications=notifDelegation.save(delegationNotifications);
        return delegationNotifications.getId();
    }

    public Long addEntryFiling(Long byUserId,FilingType filingType){
        Filings filings=new Filings();
        filings.setByUserId(byUserId);
        filings.setFilingType(filingType);
        filings=notifFiling.save(filings);
        return filings.getId();
    }

    public Long addEntryNetwork(NetworkStatus networkStatus,IdType entityFrame, IdType addedEntity, Long addedEntityAccessId){
        Network network=new Network();
        network.setNetworkStatus(networkStatus);
        if(entityFrame.equals(IdType.LpRoundId) && !(addedEntity.equals(IdType.User) ||addedEntity.equals(IdType.ServiceProvider)) ){
            throw new RuntimeException("Entity frame and added are not suitable");
        }
        if(entityFrame.equals(IdType.PortfolioRoundId)&& !(addedEntity.equals(IdType.User) ||addedEntity.equals(IdType.ServiceProvider))){
            throw new RuntimeException("Entity frame and added are not suitable");
        }
        if(entityFrame.equals(IdType.ServiceProvider) && !(addedEntity.equals(IdType.User)))
            throw new RuntimeException("Entity frame and added are not suitable");

        network.setEntityFrame(entityFrame);
        network.setAddedEntity(addedEntity);
        network.setAddedEntityAccessId(addedEntityAccessId);
        network=notifNetwork.save(network);
        return network.getId();
    }

    public Long addEntryDoc(DocumentType documentType, DocStatusType docStatusType, Boolean sharedNotification, Optional<IdType> sharedToIdType, Optional<Long> sharedToId, IdType byEntityType, Long byEntityTypeAccessId){
        Docs docs=new Docs();
        docs.setDocumentType(documentType);
        docs.setDocStatusType(docStatusType);
        docs.setSharedNotification(sharedNotification);
        docs.setByEntityType(byEntityType);
        docs.setByEntityAccessId(byEntityTypeAccessId);
        if(sharedNotification && sharedToId.isPresent() && sharedToIdType.isPresent()){
            docs.setShareToIdType(sharedToIdType.get());
            docs.setSharedToId(sharedToId.get());
        }
        docs=notifDoc.save(docs);
        return docs.getId();
    }

    public Docs getDocsEntity(Long id){
        if(notifDoc.findById(id).isEmpty())
            throw new RuntimeException("Incorrect Id");
        return notifDoc.findById(id).get();
    }
    public Filings getFilingsEntity(Long id){
        if(notifFiling.findById(id).isEmpty())
            throw new RuntimeException("Incorrect Id");
        return notifFiling.findById(id).get();

    }
    public DelegationNotifications getDelegationNotifsEntity(Long id){
        if(notifDelegation.findById(id).isEmpty())
            throw new RuntimeException("Incorrect Id");
        return notifDelegation.findById(id).get();

    }
    public Network getNetworkEntity(Long id){
        if(notifNetwork.findById(id).isEmpty())
            throw new RuntimeException("Incorrect Id");
        return notifNetwork.findById(id).get();
    }

    public void deleteSingleNotif(NotificationType notificationType,Long id){
        if(notificationType.equals(NotificationType.Delegation)){
            if(notifDelegation.findById(id).isEmpty())
                throw new RuntimeException("Incorrect Id");
            notifDelegation.deleteById(id);
        }
        else if(notificationType.equals(NotificationType.Network)){
            if(notifNetwork.findById(id).isEmpty())
                throw new RuntimeException("Incorrect Id");
            notifNetwork.deleteById(id);
        }
        else if(notificationType.equals(NotificationType.Doc)){
            if(notifDoc.findById(id).isEmpty())
                throw new RuntimeException("Incorrect Id");
             notifDoc.deleteById(id);

        }
        else{
            if(notifFiling.findById(id).isEmpty())
                throw new RuntimeException("Incorrect Id");
            notifFiling.deleteById(id);

        }
    }

}
