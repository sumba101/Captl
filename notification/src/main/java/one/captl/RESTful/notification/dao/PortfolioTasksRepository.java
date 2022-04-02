package one.captl.RESTful.notification.dao;

import one.captl.RESTful.notification.model.classes.PortfolioTasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "portfoliotasks")
public interface PortfolioTasksRepository extends JpaRepository<PortfolioTasks,Long> {
}
