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
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
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
                .header("Authorization", StringToken.getToken()))
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
                .header("Authorization", StringToken.getToken())
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
    @DisplayName("인증 사진 올리기 실패 - 진행중인 챌린지가 아닌 경우")
    void addPhotoCheck_fail_invalidChallengeStatus() throws Exception {
        doThrow(new IllegalStateException("진행중인 챌린지가 아닙니다.")).when(photoCheckService).addPhotoCheck(any(),any());

        mockMvc.perform(uploadMockSupport(multipart("/api/photo_check"),new PhotoCheckRequest())
                        .header("Authorization", StringToken.getToken())
                        .with(requestPostProcessor -> {
                            requestPostProcessor.setMethod("POST");
                            return requestPostProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andDo(document("photo_check/addPhotoCheck/errors/invalidChallengeStatus",
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("인증 사진 올리기 실패 - 참여중인 챌린지가 아닌경우")
    void addPhotoCheck_fail_hasNotJoined() throws Exception {
        doThrow(new IllegalStateException("해당 챌린지를 참여하고 있지 않습니다."))
                .when(photoCheckService).addPhotoCheck(any(),any());

        mockMvc.perform(uploadMockSupport(multipart("/api/photo_check"),new PhotoCheckRequest())
                        .header("Authorization", StringToken.getToken())
                        .with(requestPostProcessor -> {
                            requestPostProcessor.setMethod("POST");
                            return requestPostProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andDo(document("photo_check/addPhotoCheck/errors/hasNotJoined",
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("인증 사진 올리기 실패 - 챌린지에 성공하거나 실패해서 진행상태가 아닌 경우")
    void addPhotoCheck_fail_notInProgress() throws Exception {
        doThrow(new IllegalStateException("해당 챌린지에 성공하거나 실패하여 현재 진행하고 있는 상태가 아닙니다."))
                .when(photoCheckService).addPhotoCheck(any(),any());

        mockMvc.perform(uploadMockSupport(multipart("/api/photo_check"),new PhotoCheckRequest())
                        .header("Authorization", StringToken.getToken())
                        .with(requestPostProcessor -> {
                            requestPostProcessor.setMethod("POST");
                            return requestPostProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andDo(document("photo_check/addPhotoCheck/errors/notInProgress",
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("인증 사진 올리기 실패 - 이미 해당 회차에 인증 사진을 전부 올린 경우")
    void addPhotoCheck_fail_full() throws Exception {
        doThrow(new IllegalStateException("이미 해당 회차에 인증 사진을 전부 올렸습니다."))
                .when(photoCheckService).addPhotoCheck(any(),any());

        mockMvc.perform(uploadMockSupport(multipart("/api/photo_check"),new PhotoCheckRequest())
                        .header("Authorization", StringToken.getToken())
                        .with(requestPostProcessor -> {
                            requestPostProcessor.setMethod("POST");
                            return requestPostProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andDo(document("photo_check/addPhotoCheck/errors/full",
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @WithMockCustomUser
    void pass() throws Exception {
        CheckRequest checkRequest = new CheckRequest(new ArrayList<>(Arrays.asList(1L,2L,3L)));

        mockMvc.perform(post("/api/photo_check/pass")
                .header("Authorization", StringToken.getToken())
                .content(mapper.writeValueAsString(checkRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(PhotoCheckDocumentation.pass());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("인증 사진들 상태를 Pass로 변경 실패 - 종료된 챌린지인 경우")
    void pass_fail_finished() throws Exception {
        CheckRequest checkRequest = new CheckRequest(new ArrayList<>(Arrays.asList(1L,2L,3L)));
        doThrow(new IllegalStateException("종료된 챌린지 입니다."))
                .when(photoCheckService).updatePhotoCheckStatus(any(),any(),any());

        mockMvc.perform(post("/api/photo_check/pass")
                        .header("Authorization", StringToken.getToken())
                        .content(mapper.writeValueAsString(checkRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("photo_check/pass/errors/finished",
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @WithMockCustomUser
    void fail() throws Exception {
        CheckRequest checkRequest = new CheckRequest(new ArrayList<>(Arrays.asList(1L)));

        mockMvc.perform(post("/api/photo_check/fail")
                .header("Authorization", StringToken.getToken())
                .content(mapper.writeValueAsString(checkRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(PhotoCheckDocumentation.fail());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("인증 사진들 상태를 fail로 변경 실패 - 종료된 챌린지인 경우")
    void fail_fail_finished() throws Exception {
        CheckRequest checkRequest = new CheckRequest(new ArrayList<>(Arrays.asList(1L,2L,3L)));
        doThrow(new IllegalStateException("종료된 챌린지 입니다."))
                .when(photoCheckService).updatePhotoCheckStatus(any(),any(),any());

        mockMvc.perform(post("/api/photo_check/fail")
                        .header("Authorization", StringToken.getToken())
                        .content(mapper.writeValueAsString(checkRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("photo_check/fail/errors/finished",
                        preprocessResponse(prettyPrint())));
    }
}