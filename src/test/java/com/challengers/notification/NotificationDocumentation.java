package com.challengers.notification;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class NotificationDocumentation {
    public static RestDocumentationResultHandler getAllNotifications() {
        FieldDescriptor[] responseNotification= new FieldDescriptor[]{
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("알림 아이디"),
                fieldWithPath("[].type").type(JsonFieldType.STRING).description("알림 타입. 게시글 관련이면 POST를, 챌린지 관련이면 CHALLENGE를, 포인트 관련이면 POINT [POST, CHALLENGE, POINT]"),
                fieldWithPath("[].message").type(JsonFieldType.STRING).description("알림 내용. 예) 게시글 댓글, 좋아요, 챌린지 후기, 생성, 포인트 정산에 대한 메시지 내용"),
                fieldWithPath("[].targetId").type(JsonFieldType.NUMBER).description("게시글, 챌린지에 대한 id. 링크를 클릭하면 해당 게시글, 챌린지로 이동하기 위한 id"),
                fieldWithPath("[].status").type(JsonFieldType.STRING).description("알림 상태. [READ, NOT_READ]"),
                fieldWithPath("[].senderId").type(JsonFieldType.NUMBER).description("보낸 사람 아이디"),
                fieldWithPath("[].senderName").type(JsonFieldType.STRING).description("보낸 사람 이름"),
                fieldWithPath("[].senderImage").type(JsonFieldType.STRING).description("보낸 사람 이미지")
        };
        return document("notification/getAllNotifications",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(responseNotification)
        );
    }

    public static RestDocumentationResultHandler saveNotification() {
        FieldDescriptor[] requestNotification= new FieldDescriptor[]{
                fieldWithPath("type").type(JsonFieldType.STRING).description("알림 타입. 게시글 관련이면 POST를, 챌린지 관련이면 CHALLENGE를, 포인트 관련이면 POINT를 보내주시면 됩니다. [POST, CHALLENGE, POINT]"),
                fieldWithPath("senderId").type(JsonFieldType.NUMBER).description("보낸 사람 아이디"),
                fieldWithPath("recipientId").type(JsonFieldType.NUMBER).description("받는 사람 아이디"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("알림 내용. 게시글 댓글, 좋아요, 챌린지 후기, 생성, 포인트 정산에 대한 메시지를 저장합니다."),
                fieldWithPath("targetId").type(JsonFieldType.NUMBER).description("게시글, 챌린지에 대한 id. 링크를 클릭하면 해당 게시글, 챌린지로 이동합니다.")
        };
        return document("notification/saveNotification",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(requestNotification)
        );
    }

    public static RestDocumentationResultHandler updateNotification() {
        return document("notification/updateNotification",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(parameterWithName("notificationId").description("수정할 알림 id"))
        );
    }
    public static RestDocumentationResultHandler deleteNotification() {
        return document("notification/deleteNotification",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(parameterWithName("notificationId").description("삭제할 알림 id"))
        );
    }

}
