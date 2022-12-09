package nextstep.subway.fixture;

import nextstep.subway.line.domain.Line;

public class LineFixture {

    public static final Line 이호선 = 이호선_역추가();
    public static final Line 신분당선 = 신분당선_역추가();
    public static final Line 구호선 = 구호선_역추가();

    private static Line 이호선_역추가() {
        Line 이호선 = new Line("이호선", "color");
        이호선.addSection(StationFixture.강남역, StationFixture.교대역, 5);
        이호선.addSection(StationFixture.교대역, StationFixture.삼성역, 5);
        return 이호선;
    }

    private static Line 신분당선_역추가() {
        Line 신분당선 = new Line("신분당선", "color");
        신분당선.addSection(StationFixture.논현역, StationFixture.신논현역, 5);
        신분당선.addSection(StationFixture.신논현역, StationFixture.삼성역, 5);
        return 신분당선;
    }

    private static Line 구호선_역추가() {
        Line 구호선 = new Line("구호선", "color");
        구호선.addSection(StationFixture.학동역, StationFixture.여의도역, 5);
        return 구호선;
    }
}
