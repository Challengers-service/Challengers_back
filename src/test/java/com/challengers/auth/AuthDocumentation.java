package com.challengers.auth;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class AuthDocumentation {
    public static RestDocumentationResultHandler signUp() {
        FieldDescriptor[] requestAuth= new FieldDescriptor[]{
                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                fieldWithPath("passwordConfirm").type(JsonFieldType.STRING).description("비밀번호 확인"),
        };
        return document("auth/signup",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(requestAuth)
        );
    }

    public static RestDocumentationResultHandler signIn() {
        FieldDescriptor[] requestAuth = new FieldDescriptor[]{
                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
        };
        FieldDescriptor[] responseAuth= new FieldDescriptor[]{
                fieldWithPath("token").type(JsonFieldType.STRING).description("Bearer 토큰"),
        };
        return document("auth/signin",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(requestAuth),
                responseFields(responseAuth)
        );
    }
}
