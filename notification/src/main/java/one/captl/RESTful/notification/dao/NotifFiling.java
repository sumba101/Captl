package one.captl.RESTful.notification.dao;

import one.captl.RESTful.notification.model.classes.Notifs.Filings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "NotifFiling")
public interface NotifFiling extends JpaRepository<Filings,Long> {
}
