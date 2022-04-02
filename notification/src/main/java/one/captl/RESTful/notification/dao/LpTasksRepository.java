package one.captl.RESTful.notification.dao;

import one.captl.RESTful.notification.model.classes.LpTasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "lptasks")
public interface LpTasksRepository extends JpaRepository<LpTasks,Long> {
}
