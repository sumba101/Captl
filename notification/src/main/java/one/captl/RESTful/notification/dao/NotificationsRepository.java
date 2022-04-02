package one.captl.RESTful.notification.dao;

import one.captl.RESTful.notification.model.classes.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository(value = "notifications")
public interface NotificationsRepository extends JpaRepository<Notification,Long>{
    Optional<List<Notification>> findAllByUseridOrderByNotificationTimeDesc(Long userId);

    Page<Notification> findByUserid(Long userId, Pageable pageable);

    @Transactional
    void deleteAllByUserid(Long userid);

    List<Notification> findAllByUserid(Long userId);
}
