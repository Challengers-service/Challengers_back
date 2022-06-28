package com.challengers.feed;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class LikeDocumentation {
    public static RestDocumentationResultHandler createLike() {
        return document("feed/like/createLike",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(parameterWithName("challengePhotoId").description("좋아요 할 사진 id"))
        );
    }

    public static RestDocumentationResultHandler deleteLike() {
        return document("feed/like/deleteLike",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(parameterWithName("challengePhotoId").description("좋아요 취소할 사진 id"))
        );
    }
}
