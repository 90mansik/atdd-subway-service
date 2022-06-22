package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.PathTest.*;

import java.util.Arrays;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Dijkstra 라이브러리를 활용한 최단경로조회 관련 기능")
class DijkstraShortestPathFinderTest {

    private DijkstraShortestPathFinder dijkstraShortestPathFinder;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재역
     */
    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "green", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "orange", 교대역, 양재역, 5);
        삼호선.addSection(교대역, 남부터미널역, 3);

        dijkstraShortestPathFinder = new DijkstraShortestPathFinder(Arrays.asList(신분당선, 이호선, 삼호선));
    }

    @DisplayName("교대역에서 양재역까지 최단 경로 조회를 요청하면, 최단 경로가 조회된다.")
    @Test
    void getPath() {
        //when
        Path path = 최단_경로_조회함(dijkstraShortestPathFinder, 교대역, 양재역);

        //then
        경유지_확인(path, Arrays.asList(교대역, 남부터미널역, 양재역));
        경유거리_확인(path, 5);
    }

    public static Path 최단_경로_조회함(DijkstraShortestPathFinder dijkstraShortestPathFinder, Station source, Station target) {
        return dijkstraShortestPathFinder.getPath(source, target);
    }

}


