package com.challengers.challenge.controller;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.RequestPartDescriptor;

import java.io.FileDescriptor;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;

public class ChallengeDocumentation {

    public static RestDocumentationResultHandler createChallenge() {
        ParameterDescriptor[] requestParam = new ParameterDescriptor[]{
                parameterWithName("challengeName").description("챌린지 이름"),
                parameterWithName("challengeRule").description("도전 규칙"),
                parameterWithName("checkFrequency").description("인증 빈도"),
                parameterWithName("category").description("카테고리 [EXERCISE, EATING_HABIT, LIFE, EMOTION, HOBBY, SURROUNDINGS, OTHER]"),
                parameterWithName("startDate").description("챌린지 시작일 [yyyy-MM-ddT0:0:0]"),
                parameterWithName("endDate").description("챌린지 종료일 [yyyy-MM-ddT0:0:0]"),
                parameterWithName("depositPoint").description("예치 포인트"),
                parameterWithName("introduction").description("챌린지 소개글"),
                parameterWithName("tags").description("챌린지 태그들 [tag1,tag2 ...]")
        };
        RequestPartDescriptor[] requestPart = {
                partWithName("image").description("챌린지 대표 이미지"),
                partWithName("examplePhotos").description("챌린지 예시 사진들")
        };
        return document("challenge/createChallenge",
                requestParameters(requestParam),
                requestParts(requestPart)
        );
    }

    public static RestDocumentationResultHandler deleteChallenge() {
        return document("challenge/deleteChallenge",
                pathParameters(parameterWithName("id").description("삭제할 챌린지 id"))
        );
    }

    public static RestDocumentationResultHandler findChallenge() {
        FieldDescriptor[] responseUser= new FieldDescriptor[]{
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("조회할 챌린지의 아이디"),
                fieldWithPath("hostId").type(JsonFieldType.NUMBER).description("챌린지를 생성한 유저의 아이디"),
                fieldWithPath("hostProfileImageUrl").type(JsonFieldType.STRING).description("챌린지를 생성한 유저의 프로필 사진 URL"),
                fieldWithPath("hostName").type(JsonFieldType.STRING).description("챌린지를 생성한 유저의 닉네임"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("챌린지 이름"),
                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("챌린지 대표 이미지 URL"),
                fieldWithPath("challengeRule").type(JsonFieldType.STRING).description("챌린지 규칙 설명 글"),
                fieldWithPath("checkFrequency").type(JsonFieldType.STRING).description("챌린지 인증 빈도 [...]"),
                fieldWithPath("category").type(JsonFieldType.STRING).description("챌린지 카테고리 [EXERCISE, EATING_HABIT, LIFE, EMOTION, HOBBY, SURROUNDINGS, OTHER]"),
                fieldWithPath("startDate").type(JsonFieldType.STRING).description("챌린지 시작일 [yyyy-MM-dd]"),
                fieldWithPath("endDate").type(JsonFieldType.STRING).description("챌린지 종료일 [yyyy-MM-dd]"),
                fieldWithPath("depositPoint").type(JsonFieldType.NUMBER).description("예치 포인트"),
                fieldWithPath("starRating").type(JsonFieldType.NUMBER).description("챌린지 별점 [0.0~5.0]"),
                fieldWithPath("userCount").type(JsonFieldType.NUMBER).description("챌린지 참여자 수"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("챌린지 상태 [READY, PROCEEDING, DONE]"),
                fieldWithPath("tags.[]").type(JsonFieldType.ARRAY).description("챌린지 태그"),
                fieldWithPath("tags.[].id").type(JsonFieldType.NUMBER).description("챌린지 태그 아이디"),
                fieldWithPath("tags.[].name").type(JsonFieldType.STRING).description("챌린지 태그 이름"),
                fieldWithPath("examplePhotos").type(JsonFieldType.ARRAY).description("예시 사진"),
        };
        return document("challenge/findChallenge",
                responseFields(responseUser),
                pathParameters(parameterWithName("id").description("조회할 챌린지 id"))
        );
    }

    public static RestDocumentationResultHandler joinChallenge() {
        return document("challenge/joinChallenge",
                pathParameters(parameterWithName("id").description("참여할 챌린지 id"))
        );
    }

}
