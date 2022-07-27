package com.challengers.notification.repository;

import com.challengers.notification.domain.Notification;
import com.challengers.notification.dto.NotificationListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("select new com.challengers.notification.dto.NotificationListDto(n.id,n.type,n.message,n.targetId,n.status,u.id,u.name,u.image) from Notification n INNER JOIN User u ON n.senderId = u.id where n.recipientId = :recipientId")
    List<NotificationListDto> findAllNotificationByRecipientId(@Param("recipientId") Long recipientId);
}
