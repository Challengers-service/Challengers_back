package com.challengers.notification.controller;

import com.challengers.notification.dto.NotificationListDto;
import com.challengers.notification.dto.NotificationRequest;
import com.challengers.notification.service.NotificationService;
import com.challengers.security.CurrentUser;
import com.challengers.security.UserPrincipal;
import com.challengers.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/notification")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("")
    public ResponseEntity<List<NotificationListDto>> getAllNotifications(@CurrentUser UserPrincipal user){
        return ResponseEntity.ok(notificationService.getAllNotifications(user.getId()));
    }

    @PostMapping("")
    public ResponseEntity<Void> saveNotification(@RequestBody NotificationRequest notificationRequest){
        notificationService.saveNotification(notificationRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{notificationId}")
    public ResponseEntity<Void> updateNotification(@CurrentUser UserPrincipal userPrincipal, @PathVariable Long notificationId){
        notificationService.updateNotification(userPrincipal.getId(), notificationId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@CurrentUser UserPrincipal userPrincipal, @PathVariable("notificationId") Long notificationId){
        notificationService.deleteNotification(userPrincipal.getId(), notificationId);
        return ResponseEntity.ok().build();
    }
}
