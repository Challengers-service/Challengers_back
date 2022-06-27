package com.challengers.cart.controller;

import com.challengers.cart.service.CartService;
import com.challengers.security.CurrentUser;
import com.challengers.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping("/{challenge_id}")
    public ResponseEntity<Void> addCart(@PathVariable(name = "challenge_id") Long challengeId,
                                    @CurrentUser UserPrincipal user) {
        cartService.put(challengeId, user.getId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{challenge_id}")
    public ResponseEntity<Void> deleteCart(@PathVariable(name = "challenge_id") Long challengeId,
                                           @CurrentUser UserPrincipal user) {

        cartService.takeOut(challengeId, user.getId());

        return ResponseEntity.ok().build();
    }

}
