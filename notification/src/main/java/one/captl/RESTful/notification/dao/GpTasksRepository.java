package one.captl.RESTful.notification.dao;

import one.captl.RESTful.notification.model.classes.GpTasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "gptasks")
public interface GpTasksRepository extends JpaRepository<GpTasks,Long> {

}
