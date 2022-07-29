package com.challengers.point.controller;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class PointDocumentation {

    public static RestDocumentationResultHandler getMyPoint() {
        FieldDescriptor[] response= new FieldDescriptor[]{
                fieldWithPath("point").type(JsonFieldType.NUMBER).description("나의 현재 포인트")
        };

        return document("point/getMyPoint",
                preprocessResponse(prettyPrint()),
                responseFields(response)
        );
    }

    public static RestDocumentationResultHandler getMyPointHistory() {
        FieldDescriptor[] response= new FieldDescriptor[]{
                fieldWithPath("content").type(JsonFieldType.ARRAY).description("포인트 변동 내역들"),
                fieldWithPath("content.[].pointHistory").type(JsonFieldType.NUMBER).description("변경된 포인트"),
                fieldWithPath("content.[].createdAt").type(JsonFieldType.STRING).description("포인트가 변경된 시각"),
                fieldWithPath("content.[].type").type(JsonFieldType.STRING).description("포인트가 변경된 원인"),

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

        return document("point/getMyPointHistory",
                preprocessResponse(prettyPrint()),
                responseFields(response)
        );
    }

}
