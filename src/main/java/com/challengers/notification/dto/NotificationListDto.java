package com.challengers.notification.dto;

import com.challengers.notification.domain.NotificationStatus;
import com.challengers.notification.domain.NotificationType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NotificationListDto {

    private Long id;
    private NotificationType type;
    private String message;
    private Long targetId;
    private NotificationStatus status;
    private Long senderId;
    private String senderName;
    private String senderImage;

    @Builder
    public NotificationListDto(Long id, NotificationType type, String message, Long targetId, NotificationStatus status, Long senderId, String senderName, String senderImage) {
        this.id = id;
        this.type = type;
        this.message = message;
        this.targetId = targetId;
        this.status = status;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderImage = senderImage;
    }
}
