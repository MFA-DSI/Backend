package com.mfa.report.repository.Dao;

import com.mfa.report.model.*;
import com.mfa.report.model.enumerated.ActivityStatus;
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

    // Définition des entités principales et jointures
    Root<Direction> direction = query.from(Direction.class);
    Join<Direction, Mission> mission = direction.join("mission", JoinType.LEFT);
    Join<Mission, Activity> activity = mission.join("activity", JoinType.LEFT);
    Join<Activity, PerformanceRealization> performance = activity.join("performanceRealization", JoinType.LEFT);

    // Liste des prédicats pour les filtres
    List<Predicate> predicates = new ArrayList<>();

    // Filtre sur la plage de dates
    if (startDate != null) {
      if (endDate != null) {
        predicates.add(cb.between(activity.get("dueDatetime"), startDate, endDate));
      } else {
        predicates.add(cb.greaterThanOrEqualTo(activity.get("dueDatetime"), startDate));
      }
    }

    // Conditions pour les activités terminées et en cours
    Predicate completedCondition = cb.equal(performance.get("status"), "finished");
    Predicate ongoingCondition = cb.notEqual(performance.get("status"), "finished");

    // Sélection des données
    query.multiselect(
            direction.get("id"), // ID de la direction
            direction.get("name"), // Nom de la direction
            cb.countDistinct(activity.get("id")), // Nombre total exact d'activités (0 si aucune activité)
            cb.coalesce(cb.sum(cb.<Long>selectCase().when(completedCondition, 1L).otherwise(0L)), 0L), // Activités terminées
            cb.coalesce(cb.sum(cb.<Long>selectCase().when(ongoingCondition, 1L).otherwise(0L)), 0L), // Activités en cours
            cb.coalesce(cb.avg(cb.<Double>selectCase()
                    .when(cb.equal(performance.get("realizationType"), RealizationType.percentage),
                            performance.get("KPI")).otherwise(cb.literal(null))), 0.0) // Moyenne KPI
    );

    // Application des filtres
    if (!predicates.isEmpty()) {
      query.where(cb.or(cb.and(predicates.toArray(new Predicate[0])), cb.isNull(activity.get("id"))));
    }

    // Groupement et tri
    query.groupBy(direction.get("id"), direction.get("name"));
    query.orderBy(cb.asc(direction.get("name")));

    // Pagination
    TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
    typedQuery.setFirstResult((page - 1) * pageSize);
    typedQuery.setMaxResults(pageSize);

    // Exécution de la requête
    List<Object[]> results = typedQuery.getResultList();
    List<Map<String, Object>> resultList = new ArrayList<>();

    // Construction des résultats
    for (Object[] result : results) {
      Map<String, Object> resultMap = new HashMap<>();

      Long totalActivities = (Long) result[2];
      Long completedActivities = (Long) result[3];
      Long ongoingActivities = (Long) result[4];
      Double averagePerformanceIndicator = (Double) result[5];

      // Calcul de l'efficacité (ratio des activités terminées)
      Double efficiencyPercentage = (totalActivities != null && totalActivities > 0)
              ? (completedActivities.doubleValue() / totalActivities.doubleValue()) * 100
              : 0.0;

      resultMap.put("directionId", result[0]);
      resultMap.put("directionName", result[1]);
      resultMap.put("totalActivities", totalActivities);
      resultMap.put("completedActivities", completedActivities);
      resultMap.put("ongoingActivities", ongoingActivities);
      resultMap.put("averagePerformanceIndicator", averagePerformanceIndicator);
      resultMap.put("efficiencyPercentage", efficiencyPercentage);

      resultList.add(resultMap);
    }

    return resultList;
  }





  public List<Map<String, Object>> findMonthlyActivityCountByYearAndDirection(
          int year, String directionId, int page, int pageSize) {

    LocalDate startDate = LocalDate.of(year, 1, 1);
    LocalDate endDate = LocalDate.of(year, 12, 31);

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

    Root<Activity> activity = query.from(Activity.class);
    Join<Activity, Mission> mission = activity.join("mission", JoinType.LEFT);
    Join<Mission, Direction> direction = mission.join("direction", JoinType.LEFT);

    List<Predicate> predicates = new ArrayList<>();

    // Condition on the year range
    Predicate dateRangePredicate = cb.between(activity.get("dueDatetime"), startDate, endDate);
    predicates.add(dateRangePredicate);

    // Condition on the direction if specified
    if (directionId != null) {
      Predicate directionPredicate = cb.equal(direction.get("id"), directionId);
      predicates.add(directionPredicate);
    }

    // Group by month
    Expression<LocalDate> monthExpression = cb.function("DATE_TRUNC", LocalDate.class, cb.literal("month"), activity.get("dueDatetime"));
    query.multiselect(monthExpression, cb.count(activity));
    query.where(cb.and(predicates.toArray(new Predicate[0])));
    query.groupBy(monthExpression);
    query.orderBy(cb.asc(monthExpression));

    TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
    List<Object[]> results = typedQuery.getResultList();

    // Initialize a map with all months of the year set to 0 totalActivities
    Map<LocalDate, Long> monthlyCounts = new HashMap<>();
    for (int month = 1; month <= 12; month++) {
      monthlyCounts.put(LocalDate.of(year, month, 1), 0L);
    }

    // Populate actual counts from the query results
    for (Object[] result : results) {
      LocalDate month = (LocalDate) result[0];
      Long count = (Long) result[1];
      monthlyCounts.put(month, count);
    }

    // Convert the map to the desired result format
    List<Map<String, Object>> resultList = new ArrayList<>();
    for (Map.Entry<LocalDate, Long> entry : monthlyCounts.entrySet()) {
      Map<String, Object> resultMap = new HashMap<>();
      resultMap.put("date", entry.getKey());
      resultMap.put("totalActivities", entry.getValue());
      resultList.add(resultMap);
    }

    // Apply pagination manually
    int fromIndex = Math.min((page - 1) * pageSize, resultList.size());
    int toIndex = Math.min(fromIndex + pageSize, resultList.size());

    return resultList.subList(fromIndex, toIndex);
  }



}
