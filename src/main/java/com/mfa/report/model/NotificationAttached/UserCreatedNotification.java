package com.mfa.report.model.NotificationAttached;


import com.mfa.report.model.Direction;
import com.mfa.report.model.Notification;
import com.mfa.report.model.User;
import com.mfa.report.model.enumerated.NotificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("USER_ADDED")
@AllArgsConstructor
public class UserCreatedNotification extends Notification {

    @ManyToOne(cascade = CascadeType.ALL)
    private User newUser;  // L'utilisateur qui vient d'être créé

    // Constructeur par défaut
    public UserCreatedNotification() {}

    // Constructeur avec l'utilisateur créé et le destinataire
    public UserCreatedNotification(User newUser, User recipient) {
        super.setDescription("L'utilisateur " + newUser.getGrade()+" "+newUser.getFirstname()+ " "+newUser.getLastname() + " a besoin d'approbation au membre de votre direction");
        super.setUser(recipient);  // Destinataire de la notification
        super.setCreationDatetime(LocalDateTime.now());  // Date et heure de création de la notification
        super.setNotificationType(NotificationStatus.user_added);  // Type de la notification
        this.newUser = newUser;  // L'utilisateur nouvellement créé
    }

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }
}
