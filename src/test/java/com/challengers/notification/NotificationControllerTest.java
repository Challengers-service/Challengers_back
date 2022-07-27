package com.challengers.notification;

import com.challengers.common.WithMockCustomUser;
import com.challengers.common.documentation.DocumentationWithSecurity;
import com.challengers.notification.controller.NotificationController;
import com.challengers.notification.domain.NotificationStatus;
import com.challengers.notification.domain.NotificationType;
import com.challengers.notification.dto.NotificationListDto;
import com.challengers.notification.dto.NotificationRequest;
import com.challengers.notification.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.ArrayList;

import static com.challengers.user.domain.User.DEFAULT_IMAGE_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NotificationController.class)
public class NotificationControllerTest extends DocumentationWithSecurity {
    private ArrayList<NotificationListDto> notificationListDtoArrayList = new ArrayList<NotificationListDto>();
    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    NotificationService notificationService;

    @BeforeEach
    public void setup(){
        NotificationListDto notificationListDto1 = NotificationListDto.builder()
                .id(1L)
                .type(NotificationType.POST)
                .message("김준성님이 하진우님 게시물에 댓글을 달았습니다.")
                .targetId(1L)
                .status(NotificationStatus.NOT_READ)
                .senderId(2L)
                .senderName("김준성")
                .senderImage(DEFAULT_IMAGE_URL)
                .build();

        NotificationListDto notificationListDto2 = NotificationListDto.builder()
                .id(2L)
                .type(NotificationType.CHALLENGE)
                .message("하진우님이 김준성님 챌린지에 후기를 남겼습니다.")
                .targetId(1L)
                .status(NotificationStatus.NOT_READ)
                .senderId(1L)
                .senderName("하진우")
                .senderImage(DEFAULT_IMAGE_URL)
                .build();


        notificationListDtoArrayList.add(notificationListDto1);
        notificationListDtoArrayList.add(notificationListDto2);
    }

    @DisplayName("알림 조회")
    @WithMockCustomUser
    @Test
    public void getAllNotifications() throws Exception {
        when(notificationService.getAllNotifications(any())).thenReturn(notificationListDtoArrayList);

        mockMvc.perform(get("/api/notification").header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A"))
                .andExpect(status().isOk())
                .andDo(NotificationDocumentation.getAllNotifications())
                .andDo(print())
                .andReturn();
    }

    @DisplayName("알림 저장")
    @WithMockCustomUser
    @Test
    public void saveNotification() throws Exception {
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .type(NotificationType.POST)
                .senderId(1L)
                .recipientId(2L)
                .targetId(1L)
                .message("하진우님이 김준성님 챌린지에 후기를 남겼습니다.")
                .build();

        doNothing().when(notificationService).saveNotification(any());

        mockMvc.perform(post("/api/notification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(notificationRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(NotificationDocumentation.saveNotification())
                .andReturn();
    }

    @DisplayName("알림 수정")
    @WithMockCustomUser
    @Test
    public void updateNotification() throws Exception {
        doNothing().when(notificationService).updateNotification(any(),any());

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/notification/{notificationId}",1)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(NotificationDocumentation.updateNotification())
                .andDo(print())
                .andReturn();
    }

    @DisplayName("알림 삭제")
    @WithMockCustomUser
    @Test
    public void deleteNotification() throws Exception {
        doNothing().when(notificationService).deleteNotification(any(),any());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/notification/{notificationId}",1)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(NotificationDocumentation.deleteNotification())
                .andDo(print())
                .andReturn();
    }
}
