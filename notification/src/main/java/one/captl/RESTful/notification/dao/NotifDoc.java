package one.captl.RESTful.notification.dao;

import one.captl.RESTful.notification.model.classes.Notifs.Docs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "NotifDoc")
public interface NotifDoc extends JpaRepository<Docs,Long> {
}
