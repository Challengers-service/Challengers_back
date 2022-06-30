package com.challengers.photocheck.controller;

import com.challengers.challengephoto.domain.ChallengePhoto;
import com.challengers.common.WithMockCustomUser;
import com.challengers.common.documentation.DocumentationWithSecurity;
import com.challengers.photocheck.domain.PhotoCheck;
import com.challengers.photocheck.domain.PhotoCheckStatus;
import com.challengers.photocheck.dto.CheckRequest;
import com.challengers.photocheck.dto.PhotoCheckRequest;
import com.challengers.photocheck.dto.PhotoCheckResponse;
import com.challengers.photocheck.service.PhotoCheckService;
import com.challengers.testtool.StringToken;
import com.challengers.userchallenge.domain.UserChallenge;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import static com.challengers.testtool.UploadSupporter.uploadMockSupport;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = PhotoCheckController.class)
class PhotoCheckControllerTest extends DocumentationWithSecurity {
    @MockBean PhotoCheckService photoCheckService;

    UserChallenge userChallenge;
    ChallengePhoto challengePhoto;
    PhotoCheck photoCheck;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        userChallenge = UserChallenge.builder()
                .id(1L)
                .build();

        challengePhoto = ChallengePhoto.builder()
                .id(1L)
                .build();

        photoCheck = PhotoCheck.builder()
                .id(1L)
                .userChallenge(userChallenge)
                .challengePhoto(challengePhoto)
                .round(1)
                .status(PhotoCheckStatus.WAITING)
                .build();
    }

    @Test
    @WithMockCustomUser
    void getPhotoCheck() throws Exception {
        when(photoCheckService.findPhotoCheck(any())).thenReturn(PhotoCheckResponse.of(photoCheck));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/photo_check/{photo_check_id}",1L)
                .header("Authorization", StringToken.getTokenString()))
                .andExpect(status().isOk())
                .andDo(PhotoCheckDocumentation.getPhotoCheck());
    }

    @Test
    @WithMockCustomUser
    void addPhotoCheck() throws Exception {
        PhotoCheckRequest photoCheckRequest = new PhotoCheckRequest(1L,
                new MockMultipartFile("photo","photo".getBytes(StandardCharsets.UTF_8)));
        when(photoCheckService.addPhotoCheck(any(),any())).thenReturn(1L);

        mockMvc.perform(uploadMockSupport(multipart("/api/photo_check"),photoCheckRequest)
                .header("Authorization", StringToken.getTokenString())
                .with(requestPostProcessor -> {
                    requestPostProcessor.setMethod("POST");
                    return requestPostProcessor;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andDo(PhotoCheckDocumentation.addPhotoCheck());
    }

    @Test
    @WithMockCustomUser
    void pass() throws Exception {
        CheckRequest checkRequest = new CheckRequest(new ArrayList<>(Arrays.asList(1L,2L,3L)));

        mockMvc.perform(post("/api/photo_check/pass")
                .header("Authorization", StringToken.getTokenString())
                .content(mapper.writeValueAsString(checkRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(PhotoCheckDocumentation.pass());
    }

    @Test
    @WithMockCustomUser
    void fail() throws Exception {
        CheckRequest checkRequest = new CheckRequest(new ArrayList<>(Arrays.asList(1L)));

        mockMvc.perform(post("/api/photo_check/fail")
                .header("Authorization", StringToken.getTokenString())
                .content(mapper.writeValueAsString(checkRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(PhotoCheckDocumentation.fail());
    }
}