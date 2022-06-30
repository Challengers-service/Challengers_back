package com.challengers.photocheck.controller;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.RequestPartDescriptor;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public class PhotoCheckDocumentation {

    public static RestDocumentationResultHandler getPhotoCheck() {
        FieldDescriptor[] response= new FieldDescriptor[]{
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("photo_check ID"),
                fieldWithPath("userChallengeId").type(JsonFieldType.NUMBER).description("user_challenge ID"),
                fieldWithPath("challengePhotoId").type(JsonFieldType.NUMBER).description("challenge_photo ID"),
                fieldWithPath("round").type(JsonFieldType.NUMBER).description("챌린지 회차"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("인증 사진 처리 상태.\n" +
                        "처음 인증 사진을 올리면 WAITING 상태가 되고, 호스트가 인증 사진을 통과시키면 PASS, 실패시키면 FAIL 상태가 된다.\n" +
                        "[FAIL,SUCCESS,WAITING]")
        };

        return document("photo_check/getPhotoCheck",
                preprocessResponse(prettyPrint()),
                responseFields(response),
                pathParameters(parameterWithName("photo_check_id").description("조회할 photo_check ID"))
        );
    }

    public static RestDocumentationResultHandler addPhotoCheck() {
        ParameterDescriptor[] requestParam = new ParameterDescriptor[]{
                parameterWithName("challengeId").description("인증할 챌린지 ID")
        };
        RequestPartDescriptor[] requestPart = {
                partWithName("photo").description("인증 사진 파일")
        };
        return document("photo_check/addPhotoCheck",
                preprocessRequest(prettyPrint()),
                requestParameters(requestParam),
                requestParts(requestPart)
        );
    }

    public static RestDocumentationResultHandler pass() {
        FieldDescriptor[] requestField= new FieldDescriptor[]{
                fieldWithPath("photoCheckIds").type(JsonFieldType.ARRAY).description("통과시킬 photo_check IDs")
        };
        return document("photo_check/pass",
                preprocessRequest(prettyPrint()),
                requestFields(requestField)
        );
    }

    public static RestDocumentationResultHandler fail() {
        FieldDescriptor[] requestField= new FieldDescriptor[]{
                fieldWithPath("photoCheckIds").type(JsonFieldType.ARRAY).description("실패시킬 photo_check IDs")
        };
        return document("photo_check/fail",
                preprocessRequest(prettyPrint()),
                requestFields(requestField)
        );
    }

}
