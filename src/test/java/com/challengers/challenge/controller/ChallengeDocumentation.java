package com.challengers.challenge.controller;

import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.RequestPartDescriptor;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;

public class ChallengeDocumentation {

    public static RestDocumentationResultHandler createChallenge() {
        HeaderDescriptor[] requestHeaders = new HeaderDescriptor[]{
                headerWithName("Authorization").description("JWT 토큰"),
                headerWithName("Content-Type").description("multipart/form-data; boundary=<6o2knFse3p53ty9dmcQvWAIx1zInP11uCfbm>  " +
                        "참고로 boundary는 메시지 파트를 구분하는 역할을 하며 이 값은 client가 선택할 수 있습니다. " +
                        "일반적으로 메시지의 본문과 충돌되지 않도록 UUID와 같은 무작위 문자를 보냅니다." +
                        " 앞의 예시를 그대로 사용해도 됩니다.")
        };

        ParameterDescriptor[] requestParam = new ParameterDescriptor[]{
                parameterWithName("name").description("생성할 챌린지 이름"),
                parameterWithName("challengeRule").description("도전 규칙"),
                parameterWithName("checkFrequencyType").description("인증 빈도 타입. 매일이면 EVERY_DAY를, 매주이면 EVERY_WEEK를, 직접 입력이면 OTHERS를 보내주시면 됩니다. [EVERY_DAY, EVERY_WEEK, OTHERS]"),
                parameterWithName("checkTimesPerRound").description("회차 마다 인증해야 하는 횟수. 인증 빈도 타입이 EVERY_DAY나 EVERY_WEEK 이면 1을, OTHERS 이면 사용자가 입력한 값을 보내주시면 됩니다."),
                parameterWithName("category").description("카테고리. [LIFE, STUDY, WORK_OUT, SELF_DEVELOPMENT] 중 하나"),
                parameterWithName("startDate").description("챌린지 시작일. [yyyy-MM-dd] 형식으로 보내주시면 됩니다."),
                parameterWithName("endDate").description("챌린지 종료일 [yyyy-MM-dd]"),
                parameterWithName("depositPoint").description("예치 포인트"),
                parameterWithName("introduction").description("챌린지 소개글"),
                parameterWithName("userCountLimit").description("참여 인원"),
                parameterWithName("tags").description("챌린지 태그들. 태그는 쉼표로 구분해 보내주시면 됩니다. [tag1,tag2 ...]").optional()
        };

        RequestPartDescriptor[] requestPart = {
                partWithName("examplePhotos").description("인증예시 사진 파일들. " +
                        "최소 한장은 필수로 등록되어야 하며 최대 세장까지 등록가능합니다. ")
        };

        return document("challenge/createChallenge",
                preprocessRequest(prettyPrint()),
                requestHeaders(requestHeaders),
                requestParameters(requestParam),
                requestParts(requestPart)
        );
    }

    public static RestDocumentationResultHandler deleteChallenge() {
        HeaderDescriptor[] requestHeaders = new HeaderDescriptor[]{
                headerWithName("Authorization").description("JWT 토큰")
        };
        return document("challenge/deleteChallenge",
                requestHeaders(requestHeaders),
                pathParameters(parameterWithName("id").description("삭제할 챌린지 ID"))
        );
    }

    public static RestDocumentationResultHandler findChallenge() {
        HeaderDescriptor[] requestHeaders = new HeaderDescriptor[]{
                headerWithName("Authorization").description("JWT 토큰").optional()
        };

        FieldDescriptor[] responseUser= new FieldDescriptor[]{
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("조회할 챌린지의 아이디"),
                fieldWithPath("hostId").type(JsonFieldType.NUMBER).description("챌린지를 생성한 유저의 아이디"),
                fieldWithPath("hostProfileImageUrl").type(JsonFieldType.STRING).description("챌린지를 생성한 유저의 프로필 사진 URL"),
                fieldWithPath("hostName").type(JsonFieldType.STRING).description("챌린지를 생성한 유저의 닉네임"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("챌린지 이름"),
                fieldWithPath("challengeRule").type(JsonFieldType.STRING).description("챌린지 규칙 설명 글"),
                fieldWithPath("checkFrequencyType").type(JsonFieldType.STRING).description("챌린지 인증 빈도 타입 [EVERY_DAY, EVERY_WEEK, OTHERS]"),
                fieldWithPath("checkTimesPerRound").type(JsonFieldType.NUMBER).description("회차별로 인증해야 하는 횟수. " +
                        "\"인증을 1주 1회해야한다\"에서 \"1회\"에 해당합니다. 인증빈도가 매일이나 매주이면 1을, 직접 입력인 경우 직접 입력한 횟수를 반환합니다. "),
                fieldWithPath("category").type(JsonFieldType.STRING).description("챌린지 카테고리 [LIFE, STUDY, WORK_OUT, SELF_DEVELOPMENT]"),
                fieldWithPath("startDate").type(JsonFieldType.STRING).description("챌린지 시작일 [yyyy-MM-dd]"),
                fieldWithPath("endDate").type(JsonFieldType.STRING).description("챌린지 종료일 [yyyy-MM-dd]"),
                fieldWithPath("depositPoint").type(JsonFieldType.NUMBER).description("예치 포인트"),
                fieldWithPath("introduction").type(JsonFieldType.STRING).description("챌린지 소개글"),
                fieldWithPath("starRating").type(JsonFieldType.NUMBER).description("챌린지 평균 별점 [0.0~5.0]"),
                fieldWithPath("reviewCount").type(JsonFieldType.NUMBER).description("챌린지에 달린 리뷰 갯수"),
                fieldWithPath("userCount").type(JsonFieldType.NUMBER).description("챌린지에 참여하고 있는 인원수"),
                fieldWithPath("userCountLimit").type(JsonFieldType.NUMBER).description("참가 가능한 최대 인원수"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("챌린지 상태. 아직 시작 전이면 READY, " +
                        "진행 중이면 IN_PROGRESS, 챌린지 종료일로부터 일주일동안은 인증샷 검증 기간인데 이 상태이면 VALIDATE, " +
                        "챌린지 종료일로부터 일주일이 지나 검증 기간이 끝나면 FINISH 를 반환합니다." +
                        "[READY, IN_PROGRESS, VALIDATE, FINISH]"),
                fieldWithPath("tags.[]").type(JsonFieldType.ARRAY).description("챌린지 태그"),
                fieldWithPath("tags.[].id").type(JsonFieldType.NUMBER).description("챌린지 태그 아이디"),
                fieldWithPath("tags.[].name").type(JsonFieldType.STRING).description("챌린지 태그 이름"),
                fieldWithPath("examplePhotos").type(JsonFieldType.ARRAY).description("예시 사진들의 URL"),
                fieldWithPath("createdDate").type(JsonFieldType.STRING).description("챌린지 생성 일 [yyyy-MM-dd]"),
                fieldWithPath("cart").type(JsonFieldType.BOOLEAN).description("찜하기 여부"),
                fieldWithPath("expectedReward").type(JsonFieldType.NUMBER).description("예상 리워드 포인트")
        };

        return document("challenge/findChallenge",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(responseUser),
                requestHeaders(requestHeaders),
                pathParameters(parameterWithName("id").description("조회할 챌린지 ID"))
        );
    }

    public static RestDocumentationResultHandler joinChallenge() {
        HeaderDescriptor[] requestHeaders = new HeaderDescriptor[]{
                headerWithName("Authorization").description("JWT 토큰")
        };

        return document("challenge/joinChallenge",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(requestHeaders),
                pathParameters(parameterWithName("id").description("참여할 챌린지 ID"))
        );
    }

    public static RestDocumentationResultHandler updateChallenge() {
        HeaderDescriptor[] requestHeaders = new HeaderDescriptor[]{
                headerWithName("Authorization").description("JWT 토큰"),
                headerWithName("Content-Type").description("application/json")
        };

        FieldDescriptor[] requestField = new FieldDescriptor[]{
                fieldWithPath("introduction").type(JsonFieldType.STRING).description("수정할 소개글")
        };

        return document("challenge/updateChallenge",
                preprocessRequest(prettyPrint()),
                requestHeaders(requestHeaders),
                requestFields(requestField),
                pathParameters(parameterWithName("id").description("수정할 챌린지 ID"))
        );
    }

    public static RestDocumentationResultHandler searchHot() {
        HeaderDescriptor[] requestHeaders = new HeaderDescriptor[]{
                headerWithName("Authorization").description("JWT 토큰").optional()
        };

        ParameterDescriptor[] requestParams = new ParameterDescriptor[]{
                parameterWithName("page").description("페이지 번호. 번호는 0부터 시작하고 생략시 0입니다.").optional(),
                parameterWithName("size").description("한번에 가져올 콘텐츠 갯수. 생략시 9개를 가져옵니다.").optional(),
                parameterWithName("sort").description("페이지 정렬 조건. &sort=userCount,desc")
        };

        return document("challenge/searchHot",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(requestParams),
                requestHeaders(requestHeaders),
                responseFields(ChallengeDescriptors.searchResponse)
        );
    }

    public static RestDocumentationResultHandler searchNew() {
        HeaderDescriptor[] requestHeaders = new HeaderDescriptor[]{
                headerWithName("Authorization").description("JWT 토큰").optional()
        };

        ParameterDescriptor[] requestParams = new ParameterDescriptor[]{
                parameterWithName("page").description("페이지 번호. 번호는 0부터 시작하고 생략시 0입니다.").optional(),
                parameterWithName("size").description("한번에 가져올 콘텐츠 갯수. 생략시 9개를 가져옵니다.").optional(),
                parameterWithName("sort").description("페이지 정렬 조건. &sort=id,desc")
        };

        return document("challenge/searchNew",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(requestParams),
                requestHeaders(requestHeaders),
                responseFields(ChallengeDescriptors.searchResponse)
        );
    }

    public static RestDocumentationResultHandler searchCategory() {
        HeaderDescriptor[] requestHeaders = new HeaderDescriptor[]{
                headerWithName("Authorization").description("JWT 토큰").optional()
        };

        ParameterDescriptor[] requestParams = new ParameterDescriptor[]{
                parameterWithName("page").description("페이지 번호. 번호는 0부터 시작하고 생략시 0입니다.").optional(),
                parameterWithName("size").description("한번에 가져올 콘텐츠 갯수. 생략시 9개를 가져옵니다.").optional(),
                parameterWithName("category").description("검색할 카테고리. [LIFE, STUDY, WORK_OUT, SELF_DEVELOPMENT] 중 하나")
        };

        return document("challenge/searchCategory",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(requestParams),
                requestHeaders(requestHeaders),
                responseFields(ChallengeDescriptors.searchResponse)
        );
    }

    public static RestDocumentationResultHandler searchName() {
        HeaderDescriptor[] requestHeaders = new HeaderDescriptor[]{
                headerWithName("Authorization").description("JWT 토큰").optional()
        };

        ParameterDescriptor[] requestParams = new ParameterDescriptor[]{
                parameterWithName("page").description("페이지 번호. 번호는 0부터 시작하고 생략시 0입니다.").optional(),
                parameterWithName("size").description("한번에 가져올 콘텐츠 갯수. 생략시 9개를 가져옵니다.").optional(),
                parameterWithName("challengeName").description("검색할 챌린지 이름")
        };

        return document("challenge/searchName",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(requestParams),
                requestHeaders(requestHeaders),
                responseFields(ChallengeDescriptors.searchResponse)
        );
    }

    public static RestDocumentationResultHandler searchTag() {
        HeaderDescriptor[] requestHeaders = new HeaderDescriptor[]{
                headerWithName("Authorization").description("JWT 토큰").optional()
        };

        ParameterDescriptor[] requestParams = new ParameterDescriptor[]{
                parameterWithName("page").description("페이지 번호. 번호는 0부터 시작하고 생략시 0입니다.").optional(),
                parameterWithName("size").description("한번에 가져올 콘텐츠 갯수. 생략시 9개를 가져옵니다.").optional(),
                parameterWithName("tagName").description("검색할 태그")
        };

        return document("challenge/searchTag",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(requestParams),
                requestHeaders(requestHeaders),
                responseFields(ChallengeDescriptors.searchResponse)
        );
    }

    public static RestDocumentationResultHandler searchFilter() {
        HeaderDescriptor[] requestHeaders = new HeaderDescriptor[]{
                headerWithName("Authorization").description("JWT 토큰").optional()
        };

        ParameterDescriptor[] requestParams = new ParameterDescriptor[]{
                parameterWithName("page").description("페이지 번호. 번호는 0부터 시작하고 생략시 0입니다.").optional(),
                parameterWithName("size").description("한번에 가져올 콘텐츠 갯수. 생략시 9개를 가져옵니다.").optional(),
                parameterWithName("challengeName").description("검색할 챌린지 이름").optional(),
                parameterWithName("tagName").description("검색할 태그 이름").optional(),
                parameterWithName("sort").description("페이지 정렬 조건. 최신순은 '&sort=id,desc'를 인기순은 '&sort=userCount,desc'").optional()
        };

        return document("challenge/searchFilter",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(requestParams),
                requestHeaders(requestHeaders),
                responseFields(ChallengeDescriptors.searchResponse)
        );
    }
}
