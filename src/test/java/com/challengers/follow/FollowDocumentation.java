package com.challengers.follow;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class FollowDocumentation {

    public static RestDocumentationResultHandler findAllFollowers() {
        FieldDescriptor[] responseUser= new FieldDescriptor[]{
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("아이디"),
                fieldWithPath("[].name").type(JsonFieldType.STRING).description("이름"),
                fieldWithPath("[].image").type(JsonFieldType.STRING).description("이미지"),
        };
        return document("follow/findAllFollowers",
                responseFields(responseUser)
        );
    }

    public static RestDocumentationResultHandler findAllFollowing() {
        FieldDescriptor[] responseUser= new FieldDescriptor[]{
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("아이디"),
                fieldWithPath("[].name").type(JsonFieldType.STRING).description("이름"),
                fieldWithPath("[].image").type(JsonFieldType.STRING).description("이미지"),
        };
        return document("follow/findAllFollowing",
                responseFields(responseUser)
        );
    }

    public static RestDocumentationResultHandler addFollow() {
        return document("follow/addFollow",
                pathParameters(parameterWithName("followId").description("팔로우할 유저 id"))
        );
    }

    public static RestDocumentationResultHandler unFollow() {
        return document("follow/unFollow",
                pathParameters(parameterWithName("followId").description("언팔로우할 유저 id"))
        );
    }
}
