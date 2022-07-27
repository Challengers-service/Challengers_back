package com.challengers.notification.dto;

import com.challengers.notification.domain.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class NotificationRequest {
    @NotBlank
    private NotificationType type;

    @NotBlank
    private Long senderId; //알림 보낸 유저 id

    @NotBlank
    private Long recipientId; // 알림 받는 유저 id

    private Long targetId; // postid 등 알람의 주체가되는 타겟의 아이디
    private String message;

    @Builder
    public NotificationRequest(NotificationType type, Long senderId, Long recipientId, Long targetId, String message) {
        this.type = type;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.targetId = targetId;
        this.message = message;
    }
}
