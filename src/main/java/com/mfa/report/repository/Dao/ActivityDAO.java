package com.mfa.report.repository.Dao;

import com.mfa.report.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
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

// Using JoinType.LEFT creates a LEFT JOIN, returning all records from the "Activity" table
// even if there are no matching records in the joined tables. If no match is found, the joined columns will be null.

    Join<Activity, Mission> mission = activity.join("mission", JoinType.LEFT);
    Join<Activity, Task> task = activity.join("taskList", JoinType.LEFT);
    Join<Activity, NextTask> nextTask = activity.join("nexTaskList", JoinType.LEFT);
    Join<Activity, Recommendation> recommendation = activity.join("recommendations", JoinType.LEFT);
    Join<Activity, PerformanceRealization> performanceRealization = activity.join("performanceRealization", JoinType.LEFT);

    List<Predicate> predicates = new ArrayList<>();

// Add some conditions and pagination
    if (directionId != null && !directionId.isEmpty()) {
      Predicate directionPredicate = cb.equal(mission.get("direction").get("id"), directionId);
      predicates.add(directionPredicate);
    }

    if (startDate != null && endDate != null) {
      Predicate dateRangePredicate = cb.between(activity.get("dueDatetime"), startDate, endDate);
      predicates.add(dateRangePredicate);
    }

    query.select(activity).where(cb.and(predicates.toArray(new Predicate[0])));

    return entityManager.createQuery(query).getResultList();

  }
}
