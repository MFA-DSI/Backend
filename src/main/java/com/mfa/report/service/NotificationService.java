package com.mfa.report.service;

import com.mfa.report.model.Notification;
import com.mfa.report.model.Recommendation;
import com.mfa.report.model.User;
import com.mfa.report.repository.NotificationRepository;
import com.mfa.report.repository.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {
  private final NotificationRepository repository;

  public Notification getNotification(String userId) {
    return repository
        .findByUserId(userId)
        .orElseThrow(
            () ->
                new NotFoundException(
                    "userId with id." + userId + " not found"));
  }

  public void addNotification(Notification notification) {
    repository.save(notification);
  }


  public void sendNotificationToResponsible(List<User> responsibles, Recommendation recommandation) {
    for (User responsible : responsibles) {
      Notification notification = new Notification();
      notification.setUser(responsible);
      notification.setDescription(responsible.getUsername()+" a soumis une récommendation sur "+recommandation.getActivity().getDescription());
      notification.setRecommendation(recommandation);
      repository.save(notification);
    }
  }
}
