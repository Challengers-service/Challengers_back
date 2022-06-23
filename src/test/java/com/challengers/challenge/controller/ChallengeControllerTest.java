package com.challengers.challenge.controller;

import com.challengers.challenge.dto.ChallengeDetailResponse;
import com.challengers.challenge.dto.ChallengeRequest;
import com.challengers.challenge.service.ChallengeService;
import com.challengers.common.WithMockCustomUser;
import com.challengers.common.documentation.DocumentationWithSecurity;
import com.challengers.tag.dto.TagResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static com.challengers.testtool.UploadSupporter.uploadMockSupport;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ChallengeController.class)
class ChallengeControllerTest extends DocumentationWithSecurity {
    @MockBean ChallengeService challengeService;

    private ChallengeRequest challengeRequest;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        challengeRequest = ChallengeRequest.builder()
                .challengeName("미라클 모닝 - 아침 7시 기상")
                .image(new MockMultipartFile("테스트사진.png","테스트사진.png","image/png","saf".getBytes()))
                .challengeRule("7시를 가르키는 시계와 본인이 같이 나오게 사진을 찍으시면 됩니다.")
                .checkFrequency("EVERY_DAY")
                .category("LIFE")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .depositPoint(1000)
                .introduction("매일 아침 7시에 일어나면 하루가 개운합니다.")
                .examplePhotos(new ArrayList<>(Arrays.asList(
                        new MockMultipartFile("예시사진1.png","예시사진1.png","image/png","asgas".getBytes()),
                        new MockMultipartFile("예시사진2.png","예시사진2.png","image/png","asgasagagas".getBytes())
                )))
                .tags(new ArrayList<>(Arrays.asList("미라클모닝", "기상")))
                .build();
    }

    @Test
    @WithMockCustomUser
    @DisplayName("챌린지 생성")
    void createChallenge() throws Exception{
        when(challengeService.create(any(),any())).thenReturn(1L);
/*
        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/api/challenge")
                .file("image",challengeRequest.getImage().getBytes())
                .file("examplePhotos",challengeRequest.getExamplePhotos().get(0).getBytes())
                .file("examplePhotos",challengeRequest.getExamplePhotos().get(1).getBytes())
                .param("challengeName",challengeRequest.getChallengeName())
                .param("challengeRule", challengeRequest.getChallengeRule())
                .param("checkFrequency", challengeRequest.getCheckFrequency())
                .param("category", challengeRequest.getCategory())
                .param("startDate", challengeRequest.getStartDate().toString())
                .param("endDate", challengeRequest.getEndDate().toString())
                .param("depositPoint", String.valueOf(challengeRequest.getDepositPoint()))
                .param("introduction", challengeRequest.getIntroduction())
                .param("tags", "미라클모닝")
                .header("Authorization", "Bearer ADMIN_TOKEN")
                .with(requestPostProcessor -> {
                    requestPostProcessor.setMethod("POST");
                    return requestPostProcessor;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andDo(ChallengeDocumentation.createChallenge());
*/

        mockMvc.perform(uploadMockSupport(multipart("/api/challenge"),challengeRequest)
                .header("Authorization", "Bearer yJzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A")
                .with(requestPostProcessor -> {
                    requestPostProcessor.setMethod("POST");
                    return requestPostProcessor;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andDo(ChallengeDocumentation.createChallenge());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("챌린지 삭제")
    void deleteChallenge() throws Exception{
        doNothing().when(challengeService).delete(any(),any());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/challenge/{id}",1)
                .header("Authorization", "Bearer JzdWIiOiIxIiwiaWF0IjoxNjU1NzExODAyLCJleHAiOjE2NTY1NzU4MDJ9.pWHz8VTj21DA1fmfxPlrmoE_eKw_tYFTzVmVdRmof9mIe9y2OIJQ7ndThLQfwiiCbU0d0SDGgb6Oshs5R-R99A"))
                .andExpect(status().isNoContent())
                .andDo(ChallengeDocumentation.deleteChallenge());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("챌린지 상세 정보 조회")
    void findChallenge() throws Exception{
        ChallengeDetailResponse challengeDetailResponse = new ChallengeDetailResponse(1L, 1L,"https://hostProfileImageUrl.png",
                "챌린지 호스트 이름", "챌린지 이름", "https://challengeImageUrl.png", "챌린지 규칙","EVERY_DAY",
                "EXERCISE","2022-06-21","2022-07-21",1000,3.5f,32,"PROCEEDING",
                new ArrayList<>(Arrays.asList(new TagResponse(1L,"미라클모닝"), new TagResponse(2L, "기상"))),
                new ArrayList<>(Arrays.asList("https://examplePhotoUrl1.png","https://examplePhotoUrl2.png")));
        when(challengeService.findChallenge(any())).thenReturn(challengeDetailResponse);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/challenge/{id}",1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(ChallengeDocumentation.findChallenge());
    }

}