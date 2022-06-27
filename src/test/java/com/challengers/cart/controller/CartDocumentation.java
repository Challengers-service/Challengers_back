package com.challengers.cart.controller;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.RequestPartDescriptor;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public class CartDocumentation {

    public static RestDocumentationResultHandler addCart() {

        return document("cart/addCart",
                pathParameters(parameterWithName("challenge_id").description("찜할 챌린지의 ID"))
        );
    }

    public static RestDocumentationResultHandler deleteCart() {
        return document("cart/deleteCart",
                pathParameters(parameterWithName("challenge_id").description("찜을 취소할 챌린지의 ID"))
        );
    }


}
