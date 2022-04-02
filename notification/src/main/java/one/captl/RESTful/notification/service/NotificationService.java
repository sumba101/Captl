package one.captl.RESTful.notification.service;

import one.captl.RESTful.notification.dao.NotificationsRepository;
import one.captl.RESTful.notification.model.classes.Notification;
import one.captl.RESTful.notification.model.enums.IdType;
import one.captl.RESTful.notification.model.enums.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    @Qualifier(value = "notifications")
    private NotificationsRepository notificationsRepo;

    // Addition functions
    public String addNotification(Long userid, NotificationType notificationType,Long notificationId,IdType idType,Long accessId){
        Notification notification= new Notification();
        notification.setUserid(userid);

        notification.setNotificationType(notificationType);
        notification.setNotificationId(notificationId);
        notification.setIdType(idType);
        notification.setAccessId(accessId);


        notification.setNotificationTime(LocalDateTime.now());

        notification=notificationsRepo.save(notification);
        return String.valueOf(notification.getId());
    }

    // Getter functions
    public List<Notification> getAllNotificationsOf(Long userId){
        if(notificationsRepo.findAllByUseridOrderByNotificationTimeDesc(userId).isEmpty()){
            throw new RuntimeException("No notifications for user");
        }
        return notificationsRepo.findAllByUseridOrderByNotificationTimeDesc(userId).get();
    }

    public Page<Notification> getPageTopN(Long userId, Optional<Integer> pageNumber, Optional<Integer> numberOfResults, Optional<String> sortBy){
        if(notificationsRepo.findAllByUserid(userId).isEmpty()){
            throw new RuntimeException("No notifications for user");
        }

        PageRequest pageRequest=PageRequest.of(pageNumber.orElse(0),numberOfResults.orElse(10), Sort.Direction.DESC,sortBy.orElse("notificationTime"));
        return notificationsRepo.findByUserid(userId,pageRequest);
    }


    // Update functions
    public void readNotification(long nid) {
        if(notificationsRepo.findById(nid).isEmpty()){
            throw new RuntimeException("Notification not found");
        }
        Notification notification = notificationsRepo.findById(nid).get();
        notification.setRead(true);
        notificationsRepo.save(notification);
    }

    public void readAllNotifications(Long userId){
        if(notificationsRepo.findAllByUseridOrderByNotificationTimeDesc(userId).isEmpty()){
            throw new RuntimeException("No notifications for user");
        }
        List<Notification> notificationList=notificationsRepo.findAllByUseridOrderByNotificationTimeDesc(userId).get();
        for (Notification notification :
                notificationList) {
            notification.setRead(true);
            notificationsRepo.save(notification);
        }
    }

    // Delete functions

    public void deleteNotification(long nid){
        if(notificationsRepo.findById(nid).isEmpty()){
            throw new RuntimeException("Notification not found");
        }
        notificationsRepo.deleteById(nid);
    }

    public void deleteAllNotificationsOf(Long userId){
        if(notificationsRepo.findAllByUseridOrderByNotificationTimeDesc(userId).isEmpty()){
            throw new RuntimeException("No notifications for user");
        }
        notificationsRepo.deleteAllByUserid(userId);
    }

}
