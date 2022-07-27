package com.challengers.notification.service;

import com.challengers.notification.domain.Notification;
import com.challengers.notification.domain.NotificationStatus;
import com.challengers.notification.dto.NotificationListDto;
import com.challengers.notification.dto.NotificationRequest;
import com.challengers.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public List<NotificationListDto> getAllNotifications(Long userId){
        return notificationRepository.findAllNotificationByRecipientId(userId);
    }

    @Transactional
    public void saveNotification(NotificationRequest notificationRequest){
        Notification notification = Notification.builder()
                .senderId(notificationRequest.getSenderId())
                .recipientId(notificationRequest.getRecipientId())
                .type(notificationRequest.getType())
                .targetId(notificationRequest.getTargetId())
                .message(notificationRequest.getMessage())
                .status(NotificationStatus.NOT_READ)
                .build();

        notificationRepository.save(notification);
    }

    @Transactional
    public void updateNotification(Long userId, Long notificationId){
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(NoSuchElementException::new);
        if(notification.getRecipientId() == userId){
            notification.read();
        }
    }

    @Transactional
    public void deleteNotification(Long userId, Long notificationId){
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(NoSuchElementException::new);
        if(notification.getRecipientId() == userId){
            notificationRepository.deleteById(notificationId);
        }
    }
}
