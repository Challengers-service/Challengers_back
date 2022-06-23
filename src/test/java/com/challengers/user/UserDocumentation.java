package com.challengers.user;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.RequestPartDescriptor;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;

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
        return document("user/getCurrentUser",
                responseFields(responseUser)
        );
    }

    public static RestDocumentationResultHandler updateUser() {
        ParameterDescriptor[] requestUser = new ParameterDescriptor[]{
                parameterWithName("name").description("이름").optional(),
                parameterWithName("bio").description("한줄 소개").optional(),
                parameterWithName("isImageChanged").description("디폴트 이미지 변경 유무")
        };
        RequestPartDescriptor[] requestPart = new RequestPartDescriptor[]{
                partWithName("image").description("이미지").optional()
        };

        return document("user/updateUser",
                requestParameters(requestUser),
                requestParts(requestPart)
        );
    }
}
