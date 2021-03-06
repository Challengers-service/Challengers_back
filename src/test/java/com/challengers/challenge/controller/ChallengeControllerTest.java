package com.challengers.challenge.controller;

import com.challengers.challenge.domain.ChallengeStatus;
import com.challengers.challenge.domain.CheckFrequencyType;
import com.challengers.challenge.dto.ChallengeDetailResponse;
import com.challengers.challenge.dto.ChallengeRequest;
import com.challengers.challenge.dto.ChallengeResponse;
import com.challengers.challenge.dto.ChallengeUpdateRequest;
import com.challengers.challenge.service.ChallengeService;
import com.challengers.common.WithMockCustomUser;
import com.challengers.common.documentation.DocumentationWithSecurity;
import com.challengers.tag.dto.TagResponse;
import com.challengers.testtool.StringToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static com.challengers.testtool.UploadSupporter.uploadMockSupport;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ChallengeController.class)
class ChallengeControllerTest extends DocumentationWithSecurity {
    @MockBean ChallengeService challengeService;

    private ChallengeRequest challengeRequest;


    @BeforeEach
    void setUp() {
        challengeRequest = ChallengeRequest.builder()
                .name("????????? ?????? - ?????? 7??? ??????")
                .image(new MockMultipartFile("???????????????.png","???????????????.png","image/png","image file".getBytes()))
                .photoDescription("7?????? ???????????? ????????? ????????? ?????? ????????? ????????? ???????????? ?????????.")
                .challengeRule("????????? ????????? ????????? ????????????.")
                .checkFrequencyType("EVERY_DAY")
                .checkTimesPerRound(1)
                .category("LIFE")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .depositPoint(1000)
                .introduction("?????? ?????? 7?????? ???????????? ????????? ???????????????.")
                .userCountLimit(2000)
                .examplePhotos(new ArrayList<>(Arrays.asList(
                        new MockMultipartFile("????????????1.png","????????????1.png","image/png","photo file1".getBytes()),
                        new MockMultipartFile("????????????2.png","????????????2.png","image/png","photo file2".getBytes())
                )))
                .tags(new ArrayList<>(Arrays.asList("???????????????", "??????")))
                .build();
    }

    @Test
    @WithMockCustomUser
    @DisplayName("????????? ??????")
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
                .param("tags", "???????????????")
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
                .header("Authorization", StringToken.getToken())
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
    @DisplayName("????????? ??????")
    void deleteChallenge() throws Exception{
        doNothing().when(challengeService).delete(any(),any());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/challenge/{id}",1)
                .header("Authorization", StringToken.getToken()))
                .andExpect(status().isNoContent())
                .andDo(ChallengeDocumentation.deleteChallenge());
    }

    @Test
    @DisplayName("????????? ?????? ?????? ??????")
    void findChallenge() throws Exception{
        ChallengeDetailResponse challengeDetailResponse = new ChallengeDetailResponse(1L, 1L,"https://hostProfileImageUrl.png",
                "????????? ????????? ??????", "????????? ??????", "https://challengeImageUrl.png", "?????? ?????? ??????","????????? ??????", CheckFrequencyType.EVERY_DAY, 1,
                "EXERCISE","2022-06-21","2022-07-21",1000,"????????? ?????????",3.5f,0,32,2000, ChallengeStatus.IN_PROGRESS.toString(),
                new ArrayList<>(Arrays.asList(new TagResponse(1L,"???????????????"), new TagResponse(2L, "??????"))),
                new ArrayList<>(Arrays.asList("https://examplePhotoUrl1.png","https://examplePhotoUrl2.png")), "2022-01-01", false, 100);
        when(challengeService.findChallenge(any(),any())).thenReturn(challengeDetailResponse);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/challenge/{id}",1)
                .header("Authorization", StringToken.getToken())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(ChallengeDocumentation.findChallenge());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("????????? ??????")
    void joinChallenge() throws Exception{
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/challenge/join/{id}",1)
                .header("Authorization", StringToken.getToken()))
                .andExpect(status().isOk())
                .andDo(ChallengeDocumentation.joinChallenge());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("????????? ??????")
    void updateChallenge() throws Exception{
        ChallengeUpdateRequest challengeUpdateRequest = new ChallengeUpdateRequest(
                new MockMultipartFile("image file","image file".getBytes()),
                "????????? ????????? ????????? ?????????.");

        mockMvc.perform(uploadMockSupport(RestDocumentationRequestBuilders.multipart("/api/challenge/{id}",1L), challengeUpdateRequest)
                .header("Authorization", StringToken.getToken())
                .with(requestPostProcessor -> {
                    requestPostProcessor.setMethod("PUT");
                    return requestPostProcessor;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andDo(ChallengeDocumentation.updateChallenge());

        verify(challengeService).update(any(),any(),any());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("?????? ????????? ????????? ??????")
    void findCanJoinChallenges() throws Exception{
        PageImpl<ChallengeResponse> page = new PageImpl<>(Arrays.asList(new ChallengeResponse(1L, "?????? ?????? 7?????? ????????????!", "LIFE",
                        new ArrayList<>(Arrays.asList("????????? ??????", "??????")), "2022.07.02", 10, false,
                        new ArrayList<>(Arrays.asList(1L, 2L, 3L))),
                new ChallengeResponse(2L, "?????? ??? 2L ?????????", "LIFE",
                        new ArrayList<>(Arrays.asList("?????? ??????", "??????")), "2022.07.03", 14, true,
                        new ArrayList<>(Arrays.asList(1L, 2L)))),PageRequest.of(0,6, Sort.by("created_date")),2);

        when(challengeService.findReadyOrInProgressChallenges(any(),any())).thenReturn(page);
        mockMvc.perform(get("/api/challenge")
                .header("Authorization", StringToken.getToken()))
                .andExpect(status().isOk())
                .andDo(ChallengeDocumentation.findCanJoinChallenges());
    }

}