package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 양재시민의숲;
    private StationResponse 판교역;
    private StationResponse 교대역;
    private StationResponse 수원역;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        양재시민의숲 = StationAcceptanceTest.지하철역_등록되어_있음("양재시민의숲").as(StationResponse.class);
        판교역 = StationAcceptanceTest.지하철역_등록되어_있음("판교역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        수원역 = StationAcceptanceTest.지하철역_등록되어_있음("수원역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
    }

    @DisplayName("교대역 -> 양재역 -> 양재시민의숲 -> 판교역 (5 + 6 + 12)")
    @Test
    void getShortestPath() {
        // given
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 양재시민의숲, 6);
        지하철_노선에_지하철역_등록_요청(신분당선, 양재시민의숲, 판교역, 12);

        // when
        ExtractableResponse<Response> response = 지하철역_경로_조회(교대역.getId(), 판교역.getId());

        // then
        최단_경로가_조회됨(response, Arrays.asList(교대역, 양재역, 양재시민의숲, 판교역), Distance.from(23));
    }

    /**
     * Feature: 지하철 경로 조회
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *   Scenario: 출발역과 도착역 사이 최단 경로 조회
     *     When 지하철 경로 조회 요청
     *     Then 최단 경로 조회됨
     *     When 출발역과 도착역이 연결 안된 경우
     *     Then 경로 조회 실패됨
     *     When 출발역과 도착역이 같은 경우
     *     Then 경로 조회 실패됨
     */
    @DisplayName("지하철 경로 조회 관련")
    @TestFactory
    Stream<DynamicTest> getPath() {
        return Stream.of(
                dynamicTest("지하철 경로 조회 요청", 경로_조회_성공(강남역.getId(), 양재역.getId())),
                dynamicTest("출발역과 도착역이 연결 안된 경우", 경로_조회_실패(강남역.getId(), 수원역.getId())),
                dynamicTest("출발역과 도착역이 같은 경우", 경로_조회_실패(강남역.getId(), 강남역.getId()))
        );
    }

    public static ExtractableResponse<Response> 지하철역_경로_조회(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={source}&target={target}", source, target)
                .then().log().all()
                .extract();
    }

    public static void 최단_경로가_조회됨(ExtractableResponse<Response> response, List<StationResponse> stationResponses, Distance distance) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance.value());

        List<String> expected = response.jsonPath().getList("stations", StationResponse.class)
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        List<String> result = stationResponses.stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertThat(expected).containsExactlyElementsOf(result);
    }

    private Executable 경로_조회_성공(Long source, Long target) {
        return () -> {
            ExtractableResponse<Response> response = 지하철역_경로_조회(source, target);
            최단_경로가_조회됨(response, Arrays.asList(강남역, 양재역), Distance.from(10));
        };
    }

    private Executable 경로_조회_실패(Long source, Long target) {
        return () -> {
            ExtractableResponse<Response> response = 지하철역_경로_조회(source, target);
            assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        };
    }
}
