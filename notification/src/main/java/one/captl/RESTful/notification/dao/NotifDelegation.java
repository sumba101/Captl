package one.captl.RESTful.notification.dao;

import one.captl.RESTful.notification.model.classes.Notifs.DelegationNotifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "NotifDelegation")
public interface NotifDelegation extends JpaRepository<DelegationNotifications,Long> {
}
