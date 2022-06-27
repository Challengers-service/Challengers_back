package com.challengers.cart.controller;

import com.challengers.cart.service.CartService;
import com.challengers.common.WithMockCustomUser;
import com.challengers.common.documentation.DocumentationWithSecurity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
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
                .header("Authorization", "Bearer yJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A"))
                .andExpect(status().isOk())
                .andDo(CartDocumentation.addCart());

    }

    @Test
    @WithMockCustomUser
    @DisplayName("챌린지 찜을 취소한다.")
    void deleteCart() throws Exception {
        doNothing().when(cartService).takeOut(any(),any());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/cart/{challenge_id}", 1L)
                .header("Authorization", "Bearer yJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A"))
                .andExpect(status().isOk())
                .andDo(CartDocumentation.deleteCart());
    }
}