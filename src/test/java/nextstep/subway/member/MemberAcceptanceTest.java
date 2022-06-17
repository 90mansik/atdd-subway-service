package nextstep.subway.member;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTest.즐겨찾기_목록_조회_안됨;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTest.즐겨찾기_목록_조회_요청;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTest.즐겨찾기_목록_조회됨;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTest.즐겨찾기_생성_요청;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTest.즐겨찾기_생성됨;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);
        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        // then
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);
        // then
        String 사용자 = 로그인_되어_있음(loginResponse);

        // when
        ExtractableResponse<Response> findResponse = 나의_정보_조회_요청(사용자);
        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = 나의_정보_수정_요청(사용자, EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 나의_정보_삭제_요청(사용자);
        // then
        회원_삭제됨(deleteResponse);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return doPostNoAuth("/members", memberRequest);
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return doGetNoAuth(response.header("Location"));
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email,
                                                            String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return doPutNoAuth(response.header("Location"), memberRequest);
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        return doDeleteNoAuth(response.header("Location"));
    }

    public static ExtractableResponse<Response> 나의_정보_조회_요청(String accessToken) {
        return doGet(accessToken, "/members/me");
    }

    public static ExtractableResponse<Response> 나의_정보_수정_요청(String accessToken, String email, String password,
                                                            Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return doPut(accessToken, "/members/me", memberRequest);
    }

    public static ExtractableResponse<Response> 나의_정보_삭제_요청(String accessToken) {
        return doDelete(accessToken, "/members/me");
    }

    public static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static String 로그인_되어_있음(ExtractableResponse<Response> loginResponse) {
        return loginResponse.as(TokenResponse.class).getAccessToken();
    }
}
