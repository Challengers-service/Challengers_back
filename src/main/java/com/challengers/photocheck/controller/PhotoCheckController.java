package com.challengers.photocheck.controller;

import com.challengers.photocheck.domain.PhotoCheckStatus;
import com.challengers.photocheck.dto.CheckRequest;
import com.challengers.photocheck.dto.PhotoCheckRequest;
import com.challengers.photocheck.dto.PhotoCheckResponse;
import com.challengers.photocheck.service.PhotoCheckService;
import com.challengers.security.CurrentUser;
import com.challengers.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/photo_check")
@RequiredArgsConstructor
public class PhotoCheckController {
    private final PhotoCheckService photoCheckService;

    @GetMapping("/{photo_check_id}")
    public ResponseEntity<PhotoCheckResponse> getPhotoCheck(@PathVariable(name = "photo_check_id")Long photoCheckId) {
        return ResponseEntity.ok(photoCheckService.findPhotoCheck(photoCheckId));
    }

    @PostMapping
    public ResponseEntity<Void> addPhotoCheck(@ModelAttribute PhotoCheckRequest photoCheckRequest,
                                          @CurrentUser UserPrincipal user) {
        Long photoCheckId = photoCheckService.addPhotoCheck(photoCheckRequest, user.getId());
        return ResponseEntity.created(URI.create("/api/photo_check/"+photoCheckId)).build();
    }

    @PostMapping("/pass")
    public ResponseEntity<Void> pass(@RequestBody CheckRequest checkRequest,
                                 @CurrentUser UserPrincipal user) {

        photoCheckService.updatePhotoCheckStatus(checkRequest, user.getId(), PhotoCheckStatus.PASS);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/fail")
    public ResponseEntity<Void> fail(@RequestBody CheckRequest checkRequest,
                                     @CurrentUser UserPrincipal user) {

        photoCheckService.updatePhotoCheckStatus(checkRequest, user.getId(), PhotoCheckStatus.FAIL);

        return ResponseEntity.ok().build();
    }

}
