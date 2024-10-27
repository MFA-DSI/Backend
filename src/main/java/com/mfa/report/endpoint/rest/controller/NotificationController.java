package com.mfa.report.endpoint.rest.controller;


import com.mfa.report.endpoint.rest.mapper.NotificationMapper;
import com.mfa.report.endpoint.rest.model.DTO.NotificationDTO;
import com.mfa.report.endpoint.rest.model.RestEntity.Notification;
import com.mfa.report.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/direction")
public class NotificationController {
    private final NotificationService service;
    private final NotificationMapper mapper;

    @GetMapping("/notification/user")
    public List<NotificationDTO> getAllNotification(@RequestParam String userId,@RequestParam(defaultValue = "1") int page,
    @RequestParam(defaultValue = "15") int pageSize){
        return service.getNotification(userId,page,pageSize).stream().map(mapper::toDomain).collect(Collectors.toUnmodifiableList());
    }


    @PutMapping("/notification/update")
    public ResponseEntity<Notification> updateNotification(@RequestParam String id){
        return ResponseEntity.ok().body(mapper.toDomainView(service.updateNotificationStatus(id)));}}