package one.captl.RESTful.notification.dao;

import one.captl.RESTful.notification.model.classes.Delegations;
import one.captl.RESTful.notification.model.enums.IdType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository(value = "delegatedTasks")
public interface DelegationRepository extends JpaRepository<Delegations, Long> {
    Optional<List<Delegations>> findAllByAssignedBy(Long assignedBy);

    Optional<List<Delegations>> findAllByAssignedToAndAssignToIdType(Long assignedTo, IdType assignToIdtype);

    @Transactional
    void deleteAllByAssignedBy(Long assignedBy);

    @Transactional
    void deleteAllByAssignedToAndAssignToIdType(Long assignedTo, IdType assignToIdType);

}
