package com.mfa.report.repository.Dao;

import com.mfa.report.model.*;
import com.mfa.report.model.enumerated.RealizationType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class ActivityDAO {

  @PersistenceContext private EntityManager entityManager;

  public List<Activity> findActivitiesByDateRangeAndDirection(
      String directionId, LocalDate startDate, LocalDate endDate,int page,int pageSize) {

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Activity> query = cb.createQuery(Activity.class);

    Root<Activity> activity = query.from(Activity.class);

// Using JoinType.LEFT creates a LEFT JOIN, returning all records from the "Activity" table
// even if there are no matching records in the joined tables. If no match is found, the joined columns will be null.

    Join<Activity, Mission> mission = activity.join("mission", JoinType.LEFT);

    List<Predicate> predicates = new ArrayList<>();

// Add some conditions and pagination
    if (directionId != null && !directionId.isEmpty() && !directionId.equals("all")) {
      Predicate directionPredicate = cb.equal(mission.get("direction").get("id"), directionId);
      predicates.add(directionPredicate);
    }

    if (startDate != null && endDate != null) {
      Predicate dateRangePredicate = cb.between(activity.get("dueDatetime"), startDate, endDate);
      predicates.add(dateRangePredicate);
    }

    query.select(activity).where(cb.and(predicates.toArray(new Predicate[0])));
    TypedQuery<Activity> typedQuery = entityManager.createQuery(query);
    typedQuery.setFirstResult((page - 1) * pageSize);
    typedQuery.setMaxResults(pageSize);
    return  typedQuery.getResultList();

  }

  public List<Map<String, Object>> findEfficiencyByDateRangeAndDirection(
          LocalDate startDate, LocalDate endDate, int page, int pageSize) {

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

    Root<Activity> activity = query.from(Activity.class);
    Join<Activity, Mission> mission = activity.join("mission", JoinType.LEFT);
    Join<Mission, Direction> direction = mission.join("direction", JoinType.LEFT);
    Join<Activity, PerformanceRealization> performance = activity.join("performanceRealization", JoinType.LEFT);

    List<Predicate> predicates = new ArrayList<>();
    if (startDate != null && endDate != null) {
      Predicate dateRangePredicate = cb.between(activity.get("dueDatetime"), startDate, endDate);
      predicates.add(dateRangePredicate);
    }

    // Condition pour sélectionner uniquement les performances de type PERCENTAGE
    Predicate percentageCondition = cb.equal(performance.get("realizationType"), RealizationType.percentage);

    // Condition pour compter les activités "terminées"
    Predicate completedCondition = cb.and(
            cb.equal(performance.get("KPI"), 100),
            percentageCondition
    );

    // Multiselect avec les champs nécessaires, incluant les calculs pour les performances de type PERCENTAGE
    query.multiselect(
            direction.get("id"),                                      // Identifiant de la direction
            direction.get("name"),                                    // Nom de la direction
            cb.count(activity),                                       // Nombre total d'activités
            cb.sum(cb.<Long>selectCase().when(completedCondition, 1L).otherwise(0L)), // Nombre d'activités "terminées"
            cb.sum(cb.<Long>selectCase().when(completedCondition, 0L).otherwise(1L)), // Nombre d'activités "en cours"
            cb.avg(cb.<Double>selectCase().when(percentageCondition, performance.get("KPI")).otherwise(0.0)), // Moyenne des KPI pour les performances de type PERCENTAGE
            cb.sum(cb.<Double>selectCase().when(percentageCondition, performance.get("KPI")).otherwise(0.0))  // Somme des KPI pour les performances de type PERCENTAGE
    );

    query.where(cb.and(predicates.toArray(new Predicate[0])));
    query.groupBy(direction.get("id"), direction.get("name"));
    query.orderBy(cb.desc(cb.avg(performance.get("KPI"))));

    TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
    typedQuery.setFirstResult((page - 1) * pageSize);
    typedQuery.setMaxResults(pageSize);

    List<Object[]> results = typedQuery.getResultList();
    List<Map<String, Object>> resultList = new ArrayList<>();

    for (Object[] result : results) {
      Map<String, Object> resultMap = new HashMap<>();
      long totalActivities = (Long) result[2];
      double sumPerformanceIndicators = (Double) result[6];
      double efficiencyPercentage = (totalActivities > 0) ? (totalActivities/sumPerformanceIndicators) * 100 : 0;

      resultMap.put("directionId", result[0]);
      resultMap.put("directionName", result[1]);
      resultMap.put("totalActivities", totalActivities);
      resultMap.put("completedActivities", result[3]);
      resultMap.put("ongoingActivities", result[4]);
      resultMap.put("averagePerformanceIndicator", result[5]);
      resultMap.put("efficiencyPercentage", efficiencyPercentage);

      resultList.add(resultMap);
    }

    return resultList;
  }



}
