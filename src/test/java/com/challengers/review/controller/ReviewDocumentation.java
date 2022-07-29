package com.challengers.review.controller;

import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public class ReviewDocumentation {

    public static RestDocumentationResultHandler addReview() {
        HeaderDescriptor[] requestHeaders = new HeaderDescriptor[]{
                headerWithName("Authorization").description("JWT 토큰")
        };

        FieldDescriptor[] request = new FieldDescriptor[]{
                fieldWithPath("challengeId").type(JsonFieldType.NUMBER).description("챌린지 ID"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("리뷰 제목"),
                fieldWithPath("content").type(JsonFieldType.STRING).description("리뷰 내용"),
                fieldWithPath("starRating").type(JsonFieldType.NUMBER).description("별점 [0.0~5.0]"),
        };
        return document("reviews/addReview",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(request),
                requestHeaders(requestHeaders)
        );
    }

    public static RestDocumentationResultHandler deleteReview() {
        HeaderDescriptor[] requestHeaders = new HeaderDescriptor[]{
                headerWithName("Authorization").description("JWT 토큰")
        };

        return document("reviews/deleteReview",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(requestHeaders),
                pathParameters(parameterWithName("reviewId").description("삭제할 리뷰 ID"))
        );
    }

    public static RestDocumentationResultHandler updateReview() {
        HeaderDescriptor[] requestHeaders = new HeaderDescriptor[]{
                headerWithName("Authorization").description("JWT 토큰")
        };

        FieldDescriptor[] request = new FieldDescriptor[]{
                fieldWithPath("title").type(JsonFieldType.STRING).description("수정한 리뷰 제목").optional(),
                fieldWithPath("content").type(JsonFieldType.STRING).description("수정한 리뷰 내용").optional(),
                fieldWithPath("starRating").type(JsonFieldType.NUMBER).description("수정한 별점 [0.0~5.0]").optional(),
        };
        return document("reviews/updateReview",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(request),
                requestHeaders(requestHeaders),
                pathParameters(parameterWithName("reviewId").description("수정할 리뷰 ID"))
        );
    }

    public static RestDocumentationResultHandler showReviews() {
        HeaderDescriptor[] requestHeaders = new HeaderDescriptor[]{
                headerWithName("Authorization").description("JWT 토큰").optional()
        };

        ParameterDescriptor[] requestParams = new ParameterDescriptor[]{
                parameterWithName("page").description("페이지 번호. 번호는 0부터 시작하고 생략시 0입니다.").optional(),
                parameterWithName("size").description("한번에 가져올 content 갯수. 생략시 6개를 가져옵니다.").optional()
        };

        FieldDescriptor[] response= new FieldDescriptor[]{
                fieldWithPath("content.[].reviewId").type(JsonFieldType.NUMBER).description("리뷰 ID"),
                fieldWithPath("content.[].title").type(JsonFieldType.STRING).description("리뷰 제목"),
                fieldWithPath("content.[].content").type(JsonFieldType.STRING).description("리뷰 내용"),
                fieldWithPath("content.[].starRating").type(JsonFieldType.NUMBER).description("평점 [0.0~5.0]"),
                fieldWithPath("content.[].createdDate").type(JsonFieldType.STRING).description("리뷰 생성일 [yyyy-MM-dd]"),
                fieldWithPath("content.[].userId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                fieldWithPath("content.[].userName").type(JsonFieldType.STRING).description("작성자 이름"),
                fieldWithPath("content.[].profileImageUrl").type(JsonFieldType.STRING).description("작성자 프로필 사진 URL"),

                fieldWithPath("pageable").type(JsonFieldType.OBJECT).description("페이징 정보"),
                fieldWithPath("pageable.sort").type(JsonFieldType.OBJECT).description("페이징 정렬 정보"),
                fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("페이지 정렬 조건 입력 여부"),
                fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("페이징 정렬 사용 여부"),
                fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("페이징 정렬 사용 여부. 항상 sorted와 반대이다."),
                fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER).description("이전 페이지까지의 content 총 갯수"),
                fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호"),
                fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER).description("페이징 사이즈. default=6"),
                fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN).description("페이지 번호 입력 여부"),
                fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이지 번호 입력 여부. 항상 paged와 반대이다."),

                fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지인지"),
                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("총 페이지 갯수"),
                fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("총 페이지 컨텐트 갯수"),
                fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이징 사이즈"),
                fieldWithPath("number").type(JsonFieldType.NUMBER).description(""),
                fieldWithPath("sort").type(JsonFieldType.OBJECT).description("페이징 정렬 정보"),
                fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("페이지 정렬 조건 입력 여부"),
                fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("페이징 정렬 사용 여부"),
                fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("페이징 정렬 사용 여부. 항상 sorted와 반대이다."),
                fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지인지"),
                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("해당 페이지에 담긴 content 갯수"),
                fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("content 존재 여부")
        };

        return document("reviews/showReviews",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(requestParams),
                responseFields(response),
                pathParameters(parameterWithName("challengeId").description("챌린지 ID")),
                requestHeaders(requestHeaders)
        );
    }
}
