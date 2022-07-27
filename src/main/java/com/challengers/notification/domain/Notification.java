package com.challengers.notification.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "recipient_id")
    private Long recipientId;

    @Enumerated(value = EnumType.STRING)
    private NotificationType type;

    private Long targetId;

    private String message;

    @Enumerated(value = EnumType.STRING)
    private NotificationStatus status;

    @Builder
    public Notification(Long senderId, Long recipientId, NotificationType type, Long targetId, String message, NotificationStatus status) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.type = type;
        this.targetId = targetId;
        this.message = message;
        this.status = status;
    }

    public void read(){
        this.status = NotificationStatus.READ;
    }
}
