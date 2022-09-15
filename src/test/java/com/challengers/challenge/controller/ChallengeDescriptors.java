package com.challengers.challenge.controller;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class ChallengeDescriptors {
    public static FieldDescriptor[] searchResponse= new FieldDescriptor[]{
            fieldWithPath("[].challengeId").type(JsonFieldType.NUMBER).description("챌린지 ID"),
            fieldWithPath("[].name").type(JsonFieldType.STRING).description("챌린지 이름"),
            fieldWithPath("[].category").type(JsonFieldType.STRING).description("챌린지 카테고리"),
            fieldWithPath("[].tags").type(JsonFieldType.ARRAY).description("챌린지 태그 Array"),
            fieldWithPath("[].createdDate").type(JsonFieldType.STRING).description("챌린지 생성일"),
            fieldWithPath("[].remainingDays").type(JsonFieldType.NUMBER).description("챌린지 종료까지 남은 일 수"),
            fieldWithPath("[].cart").type(JsonFieldType.BOOLEAN).description("챌린지 찜 여부"),
            fieldWithPath("[].profileImgUrls").type(JsonFieldType.ARRAY).description("챌린지 참여자 중 랜덤한 두명의 프로필 사진 url"),
            fieldWithPath("[].hasJoined").type(JsonFieldType.BOOLEAN).description("챌린지 참여 여부"),

//            fieldWithPath("pageable").type(JsonFieldType.OBJECT).description("페이징 정보"),
//            fieldWithPath("pageable.sort").type(JsonFieldType.OBJECT).description("페이징 정렬 정보"),
//            fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("페이지 정렬 조건 입력 여부"),
//            fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("페이징 정렬 사용 여부"),
//            fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("페이징 정렬 사용 여부. 항상 sorted와 반대이다."),
//            fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER).description("이전 페이지까지의 content 총 갯수"),
//            fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호"),
//            fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER).description("페이징 사이즈. default=6"),
//            fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN).description("페이지 번호 입력 여부"),
//            fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이지 번호 입력 여부. 항상 paged와 반대이다."),
//
//            fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지인지"),
//            fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("총 페이지 갯수"),
//            fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("총 페이지 컨텐트 갯수"),
//            fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이징 사이즈"),
//            fieldWithPath("number").type(JsonFieldType.NUMBER).description(""),
//            fieldWithPath("sort").type(JsonFieldType.OBJECT).description("페이징 정렬 정보"),
//            fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("페이지 정렬 조건 입력 여부"),
//            fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("페이징 정렬 사용 여부"),
//            fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("페이징 정렬 사용 여부. 항상 sorted와 반대이다."),
//            fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지인지"),
//            fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("해당 페이지에 담긴 content 갯수"),
//            fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("content 존재 여부")
    };
}
