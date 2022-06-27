package com.challengers.challenge.controller;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.RequestPartDescriptor;

import java.io.FileDescriptor;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;

public class ChallengeDocumentation {

    public static RestDocumentationResultHandler createChallenge() {
        ParameterDescriptor[] requestParam = new ParameterDescriptor[]{
                parameterWithName("name").description("생성할 챌린지 이름"),
                parameterWithName("photoDescription").description("인증샷 찍는 방법 설명글"),
                parameterWithName("challengeRule").description("도전 규칙"),
                parameterWithName("checkFrequencyType").description("인증 빈도 타입. 매일이면 EVERY_DAY를, 매주이면 EVERY_WEEK를, 직접 입력이면 OTHERS를 보내주시면 됩니다. [EVERY_DAY, EVERY_WEEK, OTHERS]"),
                parameterWithName("checkTimesPerWeek").description("일주일에 인증해야 하는 횟수. 인증 빈도 타입이 EVERY_DAY 이면 7, EVERY_WEEK 이면 1, OTHERS 이면 사용자가 입력한 값"),
                parameterWithName("category").description("카테고리. [EXERCISE, EATING_HABIT, LIFE, EMOTION, HOBBY, SURROUNDINGS, OTHER]"),
                parameterWithName("startDate").description("챌린지 시작일. [yyyy-MM-dd] 형식으로 보내주시면 됩니다."),
                parameterWithName("endDate").description("챌린지 종료일 [yyyy-MM-dd]"),
                parameterWithName("depositPoint").description("예치 포인트"),
                parameterWithName("introduction").description("챌린지 소개글"),
                parameterWithName("userCountLimit").description("참여 인원"),
                parameterWithName("tags").description("챌린지 태그들. 태그는 쉼표로 구분해 보내주시면 됩니다. [tag1,tag2 ...]")
        };
        RequestPartDescriptor[] requestPart = {
                partWithName("image").description("챌린지 대표 이미지"),
                partWithName("examplePhotos").description("챌린지 예시 사진들")
        };
        return document("challenge/createChallenge",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
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
                fieldWithPath("photoDescription").type(JsonFieldType.STRING).description("인증샷 찍는 방법 설명글"),
                fieldWithPath("challengeRule").type(JsonFieldType.STRING).description("챌린지 규칙 설명 글"),
                fieldWithPath("checkFrequencyType").type(JsonFieldType.STRING).description("챌린지 인증 빈도 타입 [EVERY_DAY, EVERY_WEEK, OTHERS]"),
                fieldWithPath("checkTimesPerWeek").type(JsonFieldType.NUMBER).description("일주일에 인증해야 하는 횟수"),
                fieldWithPath("category").type(JsonFieldType.STRING).description("챌린지 카테고리 [EXERCISE, EATING_HABIT, LIFE, EMOTION, HOBBY, SURROUNDINGS, OTHER]"),
                fieldWithPath("startDate").type(JsonFieldType.STRING).description("챌린지 시작일 [yyyy-MM-dd]"),
                fieldWithPath("endDate").type(JsonFieldType.STRING).description("챌린지 종료일 [yyyy-MM-dd]"),
                fieldWithPath("depositPoint").type(JsonFieldType.NUMBER).description("예치 포인트"),
                fieldWithPath("introduction").type(JsonFieldType.STRING).description("챌린지 소개글"),
                fieldWithPath("starRating").type(JsonFieldType.NUMBER).description("챌린지 평균 별점 [0.0~5.0]"),
                fieldWithPath("reviewCount").type(JsonFieldType.NUMBER).description("챌린지에 달린 리뷰 갯수"),
                fieldWithPath("userCount").type(JsonFieldType.NUMBER).description("챌린지에 참여하고 있는 인원수"),
                fieldWithPath("userCountLimit").type(JsonFieldType.NUMBER).description("챌린지 참여 인원(참가 가능한 최대 인원수)"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("챌린지 상태 [READY, PROCEEDING, DONE]"),
                fieldWithPath("tags.[]").type(JsonFieldType.ARRAY).description("챌린지 태그"),
                fieldWithPath("tags.[].id").type(JsonFieldType.NUMBER).description("챌린지 태그 아이디"),
                fieldWithPath("tags.[].name").type(JsonFieldType.STRING).description("챌린지 태그 이름"),
                fieldWithPath("examplePhotos").type(JsonFieldType.ARRAY).description("예시 사진"),
                fieldWithPath("createdDate").type(JsonFieldType.STRING).description("챌린지 생성 일 [yyyy-MM-dd]")
        };
        return document("challenge/findChallenge",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(responseUser),
                pathParameters(parameterWithName("id").description("조회할 챌린지 id"))
        );
    }

    public static RestDocumentationResultHandler joinChallenge() {
        return document("challenge/joinChallenge",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(parameterWithName("id").description("참여할 챌린지 id"))
        );
    }

}
