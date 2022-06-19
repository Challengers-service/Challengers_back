package com.challengers.user;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class UserDocumentation {
    public static RestDocumentationResultHandler getCurrentUser() {
        FieldDescriptor[] responseUser= new FieldDescriptor[]{
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("image").type(JsonFieldType.STRING).description("이미지"),
                fieldWithPath("bio").type(JsonFieldType.STRING).description("한줄 소개"),
                fieldWithPath("role").type(JsonFieldType.STRING).description("역할"),
                fieldWithPath("provider").type(JsonFieldType.STRING).description("가입경로"),
                fieldWithPath("providerId").type(JsonFieldType.STRING).description("oauth id"),
        };
        return document("user/me",
                responseFields(responseUser)
        );
    }
}
