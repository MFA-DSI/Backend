package com.mfa.report.service;

import com.mfa.report.model.*;
import com.mfa.report.model.NotificationAttached.MissionAddedNotification;
import com.mfa.report.model.NotificationAttached.NextTaskNotification;
import com.mfa.report.model.NotificationAttached.RecommendationNotification;
import com.mfa.report.model.NotificationAttached.UserCreatedNotification;
import com.mfa.report.model.event.UserCreatedEvent;
import com.mfa.report.repository.NotificationRepository;
import com.mfa.report.repository.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService {
  private final NotificationRepository repository;
  private final NextTaskService nextTaskService;
  private final UserService userService;

  public List<Notification> getNotification(String userId, int page, int pageSize) {
    Pageable pageable =
        PageRequest.of(page - 1, pageSize,Sort.by("creationDatetime").descending());

    return repository.findAllByUserId(userId, pageable).getContent();
  }

  public void addNotification(Notification notification) {
    repository.save(notification);
  }

  public void deleteNotificationByMissionId(Mission mission) {
    repository.deleteAllNotificationFromMission_Id(mission);
  }

  public void sendRecommendationNotificationToResponsible(
      List<User> responsibles, Recommendation recommandation) {
    for (User responsible : responsibles) {
      RecommendationNotification notification =
          new RecommendationNotification(recommandation, responsible);
      repository.save(notification);
    }
  }

  @Scheduled(cron = "0 0 0 * * ?")
  public void sendNotificationForPastTasks() {
    List<NextTask> pastTasks = nextTaskService.getNextTaskPastDate();

    if (!pastTasks.isEmpty()) {
      List<User> users = nextTaskService.getResponsibleFromNextTask(LocalDate.now());
      for (NextTask task : pastTasks) {
        for (User user : users) {
          sendTaskNotification(task, user);
        }
      }
    }
  }

  @Async
  public void sendTaskNotification(NextTask task, User user) {
    NextTaskNotification notification = new NextTaskNotification(task, user);
    repository.save(notification);
  }

  public Notification updateNotificationStatus(String id) {
    Notification notification =
        repository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Notification with id:" + id + " not found"));
    notification.setViewStatus(true);
    return repository.save(notification);
  }

  public void createMissionNotification(User user, Mission mission) {
    MissionAddedNotification notification = new MissionAddedNotification(mission, user);
    repository.save(notification);
  }

  public void  createUserDirectionNotification(User admin,User user){
    UserCreatedNotification notification = new UserCreatedNotification(admin,user);
    repository.save(notification);
  }

  public void createRecommendationNotification(User user, Recommendation recommendation) {
    RecommendationNotification notification = new RecommendationNotification(recommendation, user);
    repository.save(notification);
  }

  @Async
  public void notifyMission(User user, Mission mission) {
    // Implémentation de la notification (ex. envoi d'email ou de message)
    System.out.println(
        "Notifying " + user.getEmail() + " about mission " + mission.getDescription());
    // Utilise un service d'email ou de message ici (par exemple, envoi d'email)
  }

  @Async
  public void notifyRecommendation(User user, Recommendation recommendation) {
    // Implémentation de la notification (ex. envoi d'email ou de message)
    System.out.println(
        "Notifying "
            + user.getEmail()
            + " about recommendation "
            + recommendation.getDescription());
    // Utilise un service d'email ou de message ici (par exemple, envoi d'email)
  }
}
