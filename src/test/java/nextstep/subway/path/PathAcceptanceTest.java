package nextstep.subway.path;

import static nextstep.subway.behaviors.SubwayBehaviors.도착역으로가는_최단경로를_조회한다;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선_등록되어_있음;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;



    /**
     * 교대역    --- *2호선* ---   강남역 |                        | *3호선*                   *신분당선* |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    void 최단경로찾기() {
        StationResponse 출발역 = 교대역;
        StationResponse 도착역 = 양재역;

        ExtractableResponse<Response> response = 도착역으로가는_최단경로를_조회한다(출발역, 도착역);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        PathResponse pathResponse = response.as(PathResponse.class);
        List<StationResponse> stations = pathResponse.getStations();
        assertThat(stations)
                .hasSize(3)
                .containsExactly(교대역, 남부터미널역, 양재역);
    }
}
