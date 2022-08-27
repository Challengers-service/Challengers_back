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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

    private ObjectMapper mapper;


    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();

        challengeRequest = ChallengeRequest.builder()
                .name("미라클 모닝 - 아침 7시 기상")
                .challengeRule("중복된 사진을 올리면 안됩니다.")
                .checkFrequencyType("EVERY_DAY")
                .checkTimesPerRound(1)
                .category("LIFE")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .depositPoint(1000)
                .introduction("매일 아침 7시에 일어나면 하루가 개운합니다.")
                .userCountLimit(2000)
                .examplePhotos(new ArrayList<>(Arrays.asList(
                        new MockMultipartFile("예시사진1.png","예시사진1.png","image/png","photo file1".getBytes()),
                        new MockMultipartFile("예시사진2.png","예시사진2.png","image/png","photo file2".getBytes())
                )))
                .tags(new ArrayList<>(Arrays.asList("미라클모닝", "기상")))
                .build();
    }

    @Test
    @WithMockCustomUser
    @DisplayName("챌린지 생성")
    void createChallenge() throws Exception{
        when(challengeService.create(any(),any())).thenReturn(1L);

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
    @DisplayName("챌린지 삭제")
    void deleteChallenge() throws Exception{
        doNothing().when(challengeService).delete(any(),any());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/challenge/{id}",1)
                .header("Authorization", StringToken.getToken()))
                .andExpect(status().isNoContent())
                .andDo(ChallengeDocumentation.deleteChallenge());
    }

    @Test
    @DisplayName("챌린지 상세 정보 조회")
    void findChallenge() throws Exception{
        ChallengeDetailResponse challengeDetailResponse = new ChallengeDetailResponse(1L, 1L,"https://hostProfileImageUrl.png",
                "챌린지 호스트 이름", "챌린지 이름","챌린지 규칙", CheckFrequencyType.EVERY_DAY, 1,
                "EXERCISE","2022-06-21","2022-07-21",1000,"챌린지 소개글",1000, ChallengeStatus.IN_PROGRESS.toString(),
                new ArrayList<>(Arrays.asList(new TagResponse(1L,"미라클모닝"), new TagResponse(2L, "기상"))),
                new ArrayList<>(Arrays.asList("https://examplePhotoUrl1.png","https://examplePhotoUrl2.png")), "2022-01-01", 32, 3.5f,3,
                false, false, 100);
        when(challengeService.findChallenge(any(),any())).thenReturn(challengeDetailResponse);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/challenge/{id}",1)
                .header("Authorization", StringToken.getToken())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(ChallengeDocumentation.findChallenge());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("챌린지 참여")
    void joinChallenge() throws Exception{
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/challenge/join/{id}",1)
                .header("Authorization", StringToken.getToken()))
                .andExpect(status().isOk())
                .andDo(ChallengeDocumentation.joinChallenge());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("챌린지 수정")
    void updateChallenge() throws Exception{
        ChallengeUpdateRequest challengeUpdateRequest = new ChallengeUpdateRequest(
                "수정된 챌린지 소개글 입니다.");

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/challenge/{id}",1L)
                        .header("Authorization", StringToken.getToken())
                        .content(mapper.writeValueAsString(challengeUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(ChallengeDocumentation.updateChallenge());

        verify(challengeService).update(any(),any(),any());
    }


    @Test
    @WithMockCustomUser
    @DisplayName("인기 챌린지 조회")
    void search_hot() throws Exception{
        PageImpl<ChallengeResponse> page = new PageImpl<>(Arrays.asList(new ChallengeResponse(1L, "매일 2시간 운동하기!", "LIFE",
                        new ArrayList<>(Arrays.asList("건강", "운동")), "2022.07.02", 10, false,false,
                        new ArrayList<>(Arrays.asList("https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile1.png",
                                "https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/profile2.png"))),
                new ChallengeResponse(2L, "매일 2시간 뛰기!", "LIFE",
                        new ArrayList<>(Arrays.asList("운동", "유산소")), "2022.07.03", 14, false, true,
                        new ArrayList<>(Arrays.asList("https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile1.png",
                                "https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/profile3.png")))),PageRequest.of(0,6),2);

        when(challengeService.search(any(),any(),any())).thenReturn(page);
        mockMvc.perform(get("/api/challenge?sort=userCount,desc")
                .header("Authorization", StringToken.getToken()))
                .andExpect(status().isOk())
                .andDo(ChallengeDocumentation.searchHot());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("신규 챌린지 조회")
    void search_new() throws Exception{
        PageImpl<ChallengeResponse> page = new PageImpl<>(Arrays.asList(new ChallengeResponse(2L, "매일 2시간 뛰기!", "LIFE",
                        new ArrayList<>(Arrays.asList("운동", "유산소")), "2022.07.03", 14, false, false,
                        new ArrayList<>(Arrays.asList("https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile1.png"))),
                new ChallengeResponse(1L, "매일 2시간 운동하기!", "LIFE",
                        new ArrayList<>(Arrays.asList("건강", "운동")), "2022.07.02", 10, false, true,
                        new ArrayList<>(Arrays.asList("https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile1.png")))
        ),PageRequest.of(0,6),2);

        when(challengeService.search(any(),any(),any())).thenReturn(page);
        mockMvc.perform(get("/api/challenge?sort=id,desc")
                .header("Authorization", StringToken.getToken()))
                .andExpect(status().isOk())
                .andDo(ChallengeDocumentation.searchNew());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("카테고리별 챌린지 조회")
    void search_category() throws Exception{
        PageImpl<ChallengeResponse> page = new PageImpl<>(Arrays.asList(new ChallengeResponse(1L, "매일 2시간 운동하기!", "LIFE",
                        new ArrayList<>(Arrays.asList("건강", "운동")), "2022.07.02", 10, false, false,
                        new ArrayList<>(Arrays.asList("https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile1.png"))),
                new ChallengeResponse(2L, "매일 2시간 뛰기!", "LIFE",
                        new ArrayList<>(Arrays.asList("운동", "유산소")), "2022.07.03", 14, false, true,
                        new ArrayList<>(Arrays.asList("https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile1.png",
                                "https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile2.png")))),PageRequest.of(0,6),2);

        when(challengeService.search(any(),any(),any())).thenReturn(page);
        mockMvc.perform(get("/api/challenge?category=LIFE")
                .header("Authorization", StringToken.getToken()))
                .andExpect(status().isOk())
                .andDo(ChallengeDocumentation.searchCategory());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("챌린지 이름으로 챌린지 검색")
    void search_name() throws Exception{
        PageImpl<ChallengeResponse> page = new PageImpl<>(Arrays.asList(new ChallengeResponse(1L, "매일 2시간 운동하기!", "LIFE",
                        new ArrayList<>(Arrays.asList("건강", "운동")), "2022.07.02", 10, false, false,
                        new ArrayList<>(Arrays.asList("https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile1.png",
                                "https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile2.png"))),
                new ChallengeResponse(2L, "매일 2시간 뛰기!", "LIFE",
                        new ArrayList<>(Arrays.asList("운동", "유산소")), "2022.07.03", 14, false, true,
                        new ArrayList<>(Arrays.asList("https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile1.png",
                                "https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile2.png")))),PageRequest.of(0,6),2);

        when(challengeService.search(any(),any(),any())).thenReturn(page);

        mockMvc.perform(get("/api/challenge?challengeName=매일")
                .header("Authorization", StringToken.getToken()))
                .andExpect(status().isOk())
                .andDo(ChallengeDocumentation.searchName());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("태그로 챌린지 검색")
    void search_tag() throws Exception{
        PageImpl<ChallengeResponse> page = new PageImpl<>(Arrays.asList(new ChallengeResponse(1L, "매일 2시간 운동하기!", "LIFE",
                        new ArrayList<>(Arrays.asList("건강", "운동")), "2022.07.02", 10, false, false,
                        new ArrayList<>(Arrays.asList("https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile1.png",
                                "https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile2.png"))),
                new ChallengeResponse(2L, "매일 2시간 뛰기!", "LIFE",
                        new ArrayList<>(Arrays.asList("운동", "유산소")), "2022.07.03", 14, false, true,
                        new ArrayList<>(Arrays.asList("https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile1.png")))),
                PageRequest.of(0,6),2);

        when(challengeService.search(any(),any(),any())).thenReturn(page);

        mockMvc.perform(get("/api/challenge?tagName=운동")
                .header("Authorization", StringToken.getToken()))
                .andExpect(status().isOk())
                .andDo(ChallengeDocumentation.searchTag());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("챌린지 검색 결과에 필터 적용")
    void search_filter() throws Exception{
        PageImpl<ChallengeResponse> page = new PageImpl<>(Arrays.asList(new ChallengeResponse(1L, "매일 2시간 운동하기!", "LIFE",
                        new ArrayList<>(Arrays.asList("건강", "운동")), "2022.07.02", 10, false, false,
                        new ArrayList<>(Arrays.asList("https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile1.png",
                                "https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile2.png"))),
                new ChallengeResponse(2L, "매일 2시간 뛰기!", "LIFE",
                        new ArrayList<>(Arrays.asList("운동", "유산소")), "2022.07.03", 14, false, true,
                        new ArrayList<>(Arrays.asList("https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile1.png",
                                "https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/defaultProfile2.png")))),PageRequest.of(0,6),2);

        when(challengeService.search(any(),any(),any())).thenReturn(page);
        mockMvc.perform(get("/api/challenge?challengeName=매일 2시간&sort=userCount,desc")
                .header("Authorization", StringToken.getToken()))
                .andExpect(status().isOk())
                .andDo(ChallengeDocumentation.searchFilter());
    }

}