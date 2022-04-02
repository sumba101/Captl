package one.captl.RESTful.notification.dao;

import one.captl.RESTful.notification.model.classes.Notifs.Network;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "NotifNetwork")
public interface NotifNetwork extends JpaRepository<Network,Long> {
}
