package com.mfa.report.endpoint.event;

import com.mfa.report.model.Direction;
import com.mfa.report.model.User;
import com.mfa.report.model.enumerated.Role;
import com.mfa.report.model.event.UserCreatedEvent;
import com.mfa.report.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserCreatedListener {

    @Autowired
    private NotificationService notificationService;

    @EventListener
    public void onUserCreated(UserCreatedEvent event) {
        User newUser = event.getUser();
        Direction direction = event.getDirection();

        // Message personnalisé pour les administrateurs de la directi

        // Récupère les utilisateurs ayant le rôle ADMIN ou SUPER_ADMIN dans la direction spécifiée
        List<User> adminUsers = direction.getResponsible().stream()
                .filter(user -> user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.SUPER_ADMIN))
                .collect(Collectors.toList());

        // Envoie la notification à chaque administrateur
        adminUsers.forEach(admin -> {
            notificationService.createUserDirectionNotification(newUser,admin);
        });
    }

}
