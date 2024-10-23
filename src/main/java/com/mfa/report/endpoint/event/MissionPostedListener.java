package com.mfa.report.endpoint.event;

import com.mfa.report.model.Direction;
import com.mfa.report.model.Mission;
import com.mfa.report.model.User;
import com.mfa.report.model.event.MissionPostedEvent;
import com.mfa.report.service.NotificationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MissionPostedListener {
  @Autowired private NotificationService notificationService;

  @EventListener
  public void onMissionPosted(MissionPostedEvent event) {
    Mission mission = event.getMission();
    Direction direction = event.getDirection();
    User poster = mission.getPostedBy();
    List<User> responsibles = direction.getResponsible();
    responsibles.stream()
        .filter(responsible -> !responsible.equals(poster))
        .forEach(
            responsible -> {
              notificationService.notifyMission(responsible, mission);
              notificationService.createMissionNotification(responsible, mission);
            });
  }

    @EventListener
    public void onMissionUpdated(MissionPostedEvent event) {
        Mission mission = event.getMission();
        Direction direction = event.getDirection();

        User poster = mission.getPostedBy();

        List<User> responsibles = direction.getResponsible();

        responsibles.stream()
                .filter(responsible -> !responsible.equals(poster))
                .forEach(
                        responsible -> {
                            notificationService.notifyMission(responsible, mission);
                            notificationService.createMissionNotification(responsible, mission);
                        });
    }
}
