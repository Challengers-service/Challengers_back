package com.challengers.photocheck.controller;

import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.RequestPartDescriptor;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public class PhotoCheckDocumentation {

    public static RestDocumentationResultHandler getPhotoCheck() {
        HeaderDescriptor[] requestHeaders = new HeaderDescriptor[]{
                headerWithName("Authorization").description("JWT 토큰")
        };

        FieldDescriptor[] response= new FieldDescriptor[]{
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("인증 사진 ID"),
                fieldWithPath("userChallengeId").type(JsonFieldType.NUMBER).description("user_challenge ID"),
                fieldWithPath("challengePhotoId").type(JsonFieldType.NUMBER).description("사진 ID"),
                fieldWithPath("round").type(JsonFieldType.NUMBER).description("챌린지 회차"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("인증 사진 처리 상태.\n" +
                        "처음 인증 사진을 올리면 WAITING 상태가 되고, 호스트가 인증 사진을 통과시키면 PASS, 실패시키면 FAIL 상태가 된다.\n" +
                        "[FAIL,SUCCESS,WAITING]")
        };

        return document("photo_check/getPhotoCheck",
                preprocessResponse(prettyPrint()),
                responseFields(response),
                requestHeaders(requestHeaders),
                pathParameters(parameterWithName("photo_check_id").description("조회할 photo_check ID"))
        );
    }

    public static RestDocumentationResultHandler addPhotoCheck() {
        HeaderDescriptor[] requestHeaders = new HeaderDescriptor[]{
                headerWithName("Authorization").description("JWT 토큰")
        };

        ParameterDescriptor[] requestParam = new ParameterDescriptor[]{
                parameterWithName("challengeId").description("인증할 챌린지의 ID")
        };

        RequestPartDescriptor[] requestPart = {
                partWithName("photo").description("인증 사진 파일")
        };

        return document("photo_check/addPhotoCheck",
                preprocessRequest(prettyPrint()),
                requestParameters(requestParam),
                requestHeaders(requestHeaders),
                requestParts(requestPart)
        );
    }

    public static RestDocumentationResultHandler pass() {
        HeaderDescriptor[] requestHeaders = new HeaderDescriptor[]{
                headerWithName("Authorization").description("JWT 토큰")
        };

        FieldDescriptor[] requestField= new FieldDescriptor[]{
                fieldWithPath("photoCheckIds").type(JsonFieldType.ARRAY).description("통과시킬 인증 사진들의 ID Array")
        };

        return document("photo_check/pass",
                preprocessRequest(prettyPrint()),
                requestFields(requestField),
                requestHeaders(requestHeaders)
        );
    }

    public static RestDocumentationResultHandler fail() {
        HeaderDescriptor[] requestHeaders = new HeaderDescriptor[]{
                headerWithName("Authorization").description("JWT 토큰")
        };

        FieldDescriptor[] requestField= new FieldDescriptor[]{
                fieldWithPath("photoCheckIds").type(JsonFieldType.ARRAY).description("실패시킬 인증 사진들의 ID Array")
        };
        return document("photo_check/fail",
                preprocessRequest(prettyPrint()),
                requestFields(requestField),
                requestHeaders(requestHeaders)
        );
    }

}
