package com.challengers.feed;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class CommentDocumentation {
    public static RestDocumentationResultHandler getComment() {
        FieldDescriptor[] responseComment= new FieldDescriptor[]{
                fieldWithPath("comments[].id").type(JsonFieldType.NUMBER).description("아이디"),
                fieldWithPath("comments[].auth.id").type(JsonFieldType.NUMBER).description("유저 아이디"),
                fieldWithPath("comments[].auth.name").type(JsonFieldType.STRING).description("유저 이름"),
                fieldWithPath("comments[].auth.image").type(JsonFieldType.STRING).description("유저 이미지"),
                fieldWithPath("comments[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                fieldWithPath("commentCnt").type(JsonFieldType.NUMBER).description("댓글 수"),
        };
        return document("feed/comment/getComment",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(responseComment),
                pathParameters(parameterWithName("challengePhotoId").description("조회할 사진 id"))
        );
    }

    public static RestDocumentationResultHandler createComment() {
        FieldDescriptor[] requestComment= new FieldDescriptor[]{
                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용")
        };
        return document("feed/comment/createComment",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(requestComment),
                pathParameters(parameterWithName("challengePhotoId").description("댓글 생성할 사진 id"))
        );
    }

    public static RestDocumentationResultHandler updateComment() {
        FieldDescriptor[] requestComment= new FieldDescriptor[]{
                fieldWithPath("content").type(JsonFieldType.STRING).description("수정할 댓글 내용")
        };
        return document("feed/comment/updateComment",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(requestComment),
                pathParameters(parameterWithName("commentId").description("수정할 댓글 id"))
        );
    }

    public static RestDocumentationResultHandler deleteComment() {
        return document("feed/comment/deleteComment",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(parameterWithName("commentId").description("삭제할 댓글 id"))
        );
    }
}
