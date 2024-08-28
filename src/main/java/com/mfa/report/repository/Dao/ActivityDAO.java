package com.mfa.report.repository.Dao;

import com.mfa.report.model.Activity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Repository
@AllArgsConstructor
public class ActivityDAO {

    @PersistenceContext
    private EntityManager entityManager;


    public List<Activity> findActivitiesByDateRangeAndDirection(LocalDate startDate, LocalDate endDate, String missionId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Activity> query = cb.createQuery(Activity.class);
        Root<Activity> activity = query.from(Activity.class);

        List<Predicate> predicates = new ArrayList<>();

        // Filtre sur les dates
        if (startDate != null && endDate != null) {
            predicates.add(cb.between(activity.get("creationDatetime"), startDate, endDate));
        }

        // Filtre sur la direction
        if (missionId != null && !missionId.isEmpty()) {
            predicates.add(cb.equal(activity.get("mission").get("id"), missionId));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }
}
