package com.challengers.cart.controller;

import com.challengers.cart.service.CartService;
import com.challengers.common.WithMockCustomUser;
import com.challengers.common.documentation.DocumentationWithSecurity;
import com.challengers.testtool.StringToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CartController.class)
class CartControllerTest extends DocumentationWithSecurity {
    @MockBean CartService cartService;

    @Test
    @WithMockCustomUser
    @DisplayName("챌린지를 찜 한다.")
    void addCart() throws Exception {
        doNothing().when(cartService).put(any(),any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/cart/{challenge_id}", 1L)
                .header("Authorization", StringToken.getToken()))
                .andExpect(status().isOk())
                .andDo(CartDocumentation.addCart());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("챌린지 찜하기 실패 - 이미 찜한 챌린지인 경우")
    void addCart_fail_hasAdded() throws Exception {
        doThrow(new IllegalStateException("이미 찜한 챌린지입니다.")).when(cartService).put(any(),any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/cart/{challenge_id}", 1L)
                        .header("Authorization", StringToken.getToken()))
                .andExpect(status().isBadRequest())
                .andDo(document("cart/addCart/errors/hasAdded",
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("챌린지 찜을 취소한다.")
    void deleteCart() throws Exception {
        doNothing().when(cartService).takeOut(any(),any());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/cart/{challenge_id}", 1L)
                .header("Authorization", StringToken.getToken()))
                .andExpect(status().isOk())
                .andDo(CartDocumentation.deleteCart());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("챌린지 찜하기 취소 실패 - 찜하기가 안되어 있는 경우")
    void deleteCart_fail_hasNotAdded() throws Exception {
        doThrow(new IllegalStateException("찜하기가 안되어 있습니다.")).when(cartService).takeOut(any(),any());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/cart/{challenge_id}", 1L)
                        .header("Authorization", StringToken.getToken()))
                .andExpect(status().isBadRequest())
                .andDo(document("cart/deleteCart/errors/hasNotAdded",
                        preprocessResponse(prettyPrint())));
    }
}