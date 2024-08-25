package com.mfa.report.service;


import com.mfa.report.repository.NotificationRepository;
import com.mfa.report.repository.exception.NotFoundException;
import com.mfa.report.model.Notification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;


    public Notification getNotification(String userId){
        return repository.findByUserId(userId).orElseThrow(() -> new NotFoundException("userId with id." + userId + " not found or not have anything to view "));
    }

    public void addNotification(Notification notification){
         repository.save(notification);
    }
}
