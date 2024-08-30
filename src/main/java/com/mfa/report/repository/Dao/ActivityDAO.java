package com.mfa.report.repository.Dao;

import com.mfa.report.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class ActivityDAO {

  @PersistenceContext private EntityManager entityManager;

  public List<Activity> findActivitiesByDateRangeAndDirection(
      String directionId, LocalDate startDate, LocalDate endDate) {

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Activity> query = cb.createQuery(Activity.class);

    Root<Activity> activity = query.from(Activity.class);

    Join<Activity, Mission> mission = activity.join("mission");
    Join<Activity, Task> task = activity.join("taskList");
    Join<Activity, NextTask> nextTask = activity.join("nexTaskList");
    Join<Activity, Recommendation> recommendation = activity.join("recommendations");
    Join<Activity, PerformanceRealization> performanceRealization =
        activity.join("performanceRealization");

    // Set the conditions
    Predicate directionPredicate = cb.equal(mission.get("direction").get("id"), directionId);
    Predicate dateRangePredicate = cb.between(activity.get("creationDatetime"), startDate, endDate);

    query.select(activity).where(cb.and(directionPredicate, dateRangePredicate));

    return entityManager.createQuery(query).getResultList();
  }
}
