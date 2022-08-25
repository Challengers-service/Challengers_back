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
                headerWithName("Authorization").description("JWT 토큰"),
                headerWithName("Content-Type").description("multipart/form-data; boundary=<6o2knFse3p53ty9dmcQvWAIx1zInP11uCfbm>  " +
                        "참고로 boundary는 메시지 파트를 구분하는 역할을 하며 이 값은 client가 선택할 수 있습니다. " +
                        "일반적으로 메시지의 본문과 충돌되지 않도록 UUID와 같은 무작위 문자를 보냅니다." +
                        " 앞의 예시를 그대로 사용해도 됩니다.")
        };

        ParameterDescriptor[] requestParam = new ParameterDescriptor[]{
                parameterWithName("challengeId").description("인증할 챌린지의 ID")
        };

        RequestPartDescriptor[] requestPart = {
                partWithName("photo").description("인증 사진 파일. 필수값입니다.")
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
                headerWithName("Authorization").description("JWT 토큰"),
                headerWithName("Content-Type").description("application/json")
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
                headerWithName("Authorization").description("JWT 토큰"),
                headerWithName("Content-Type").description("application/json")
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
