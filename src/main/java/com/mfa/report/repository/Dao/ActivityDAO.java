package com.mfa.report.repository.Dao;

import com.mfa.report.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Repository
@AllArgsConstructor
public class ActivityDAO {

    @PersistenceContext
    private EntityManager entityManager;

  public List<Activity> findActivitiesByDateRangeAndDirection( String directionId ,
      LocalDate startDate, LocalDate endDate) {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);

// Déclaration des racines
      Root<Direction> d = cq.from(Direction.class);
      Join<Direction, Mission> m = d.join("mission");
      Join<Mission, Activity> a = m.join("activity");
      Join<Activity, Task> t = a.join("taskList");
      //Join<Activity, NextTask> n = a.join("nextTask");
      Join<Activity, Recommendation> r = a.join("recommendations");
      Join<Activity, PerformanceRealization> p = a.join("performanceRealization");

// Sélection des champs souhaités
      cq.multiselect(
              a.get("description"),
              a.get("observation"),
              a.get("prediction"),
              t,
              //n,
              r,
              p
      );

// Conditions de la requête
        log.info(String.valueOf("diretion:"+directionId + "date :"+startDate + "end date :"+endDate));
      if(directionId != null){
          cq.where(
                  cb.equal(d.get("id"), "550e8400-e29b-41d4-a716-446655440000")
          );
      }



// Création de la requête
      List<Object[]> results = entityManager.createQuery(cq).getResultList();

      log.info(results.toString());
      ModelMapper modelMapper = new ModelMapper();
      List<Activity> activities = results.stream()
              .map(row -> modelMapper.map(row, Activity.class))
              .collect(Collectors.toList());
        return  activities;
  }
}
