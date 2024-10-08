package com.mfa.report.service;

import com.mfa.report.model.NextTask;
import com.mfa.report.model.Notification;
import com.mfa.report.model.Recommendation;
import com.mfa.report.model.User;
import com.mfa.report.repository.NotificationRepository;
import com.mfa.report.repository.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService {
  private final NotificationRepository repository;
  private final NextTaskService nextTaskService;
  private final UserService userService;

  public List<Notification> getNotification(String userId) {
    return repository
        .findAllByUserId(userId)
        .orElseThrow(() -> new NotFoundException("userId with id." + userId + " not found"));
  }

  public void addNotification(Notification notification) {
    repository.save(notification);
  }

  public void sendNotificationToResponsible(
      List<User> responsibles, Recommendation recommandation) {
    for (User responsible : responsibles) {
      Notification notification = new Notification();
      notification.setUser(responsible);

      notification.setDescription(
          responsible.getGrade()
              + " "
              + responsible.getFirstname()
              + " a soumis une récommendation sur "
              + recommandation.getActivity().getDescription());
      notification.setRecommendation(recommandation);
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
          sendNotification(task, user);
        }
      }
    }
  }

  @Async
  public void sendNotification(NextTask task, User user) {
    Notification notification = new Notification();
    notification.setDescription("The task '" + task.getDescription() + "' is overdue.");
    notification.setUser(user);
    repository.save(notification);
  }

  public Notification updateNotificationStatus(String id){
    Notification notification = repository.findById(id).orElseThrow(()->new NotFoundException("Notification with id:"+id+" not found"));
    notification.setViewStatus(true);
    return repository.save(notification);

  }
}
