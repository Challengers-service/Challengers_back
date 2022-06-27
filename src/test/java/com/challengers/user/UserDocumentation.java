package com.challengers.user;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.RequestPartDescriptor;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
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
                fieldWithPath("followerCount").type(JsonFieldType.NUMBER).description("팔로우 수"),
                fieldWithPath("followingCount").type(JsonFieldType.NUMBER).description("팔로잉 수"),
                fieldWithPath("awardList.[]").type(JsonFieldType.ARRAY).description("ONE_PARTICIPATION(챌린지 1회 참여), FIFTY_PARTICIPATION(챌린지 50회 참여), PERFECT_ATTENDANCE(30일 출석)"),
        };
        return document("user/getCurrentUser",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(responseUser)
        );
    }

    public static RestDocumentationResultHandler updateUser() {
        ParameterDescriptor[] requestUser = new ParameterDescriptor[]{
                parameterWithName("name").description("이름").optional(),
                parameterWithName("bio").description("한줄 소개").optional(),
                parameterWithName("isImageChanged").description("디폴트 이미지 변경 유무: 디폴트 이미지인 경우 : true, 수정해서 디폴트 이미지가 아닌경우 false")
        };
        RequestPartDescriptor[] requestPart = new RequestPartDescriptor[]{
                partWithName("image").description("이미지").optional()
        };

        return document("user/updateUser",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(requestUser),
                requestParts(requestPart)
        );
    }
}
