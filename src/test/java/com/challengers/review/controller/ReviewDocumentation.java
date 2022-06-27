package com.challengers.review.controller;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.RequestPartDescriptor;
import org.springframework.test.web.servlet.ResultHandler;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public class ReviewDocumentation {

    public static RestDocumentationResultHandler addReview() {
        FieldDescriptor[] request = new FieldDescriptor[]{
                fieldWithPath("challengeId").type(JsonFieldType.NUMBER).description("챌린지 ID"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("리뷰 제목"),
                fieldWithPath("content").type(JsonFieldType.STRING).description("리뷰 내용"),
                fieldWithPath("starRating").type(JsonFieldType.NUMBER).description("별점 [0.0~5.0]"),
        };
        return document("reviews/addReview",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(request)
        );
    }

    public static RestDocumentationResultHandler deleteReview() {
        return document("reviews/deleteReview",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(parameterWithName("reviewId").description("삭제할 리뷰 ID"))
        );
    }

    public static RestDocumentationResultHandler updateReview() {
        FieldDescriptor[] request = new FieldDescriptor[]{
                fieldWithPath("title").type(JsonFieldType.STRING).description("수정한 리뷰 제목"),
                fieldWithPath("content").type(JsonFieldType.STRING).description("수정한 리뷰 내용"),
                fieldWithPath("starRating").type(JsonFieldType.NUMBER).description("수정한 별점 [0.0~5.0]"),
        };
        return document("reviews/updateReview",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(request),
                pathParameters(parameterWithName("reviewId").description("수정할 리뷰 ID"))
        );
    }

    public static RestDocumentationResultHandler showReviewsList() {
        FieldDescriptor[] response= new FieldDescriptor[]{
                fieldWithPath("[].reviewId").type(JsonFieldType.NUMBER).description("리뷰 ID"),
                fieldWithPath("[].title").type(JsonFieldType.STRING).description("리뷰 제목"),
                fieldWithPath("[].content").type(JsonFieldType.STRING).description("리뷰 내용"),
                fieldWithPath("[].starRating").type(JsonFieldType.NUMBER).description("평점 [0.0~5.0]"),
                fieldWithPath("[].createdDate").type(JsonFieldType.STRING).description("리뷰 생성일 [yyyy-MM-dd]"),
                fieldWithPath("[].userId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                fieldWithPath("[].userName").type(JsonFieldType.STRING).description("작성자 이름"),
                fieldWithPath("[].profileImageUrl").type(JsonFieldType.STRING).description("작성자 프로필 사진 URL"),
        };
        return document("reviews/showReviewsList",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(response),
                pathParameters(parameterWithName("challengeId").description("챌린지 ID"))
        );
    }
}
