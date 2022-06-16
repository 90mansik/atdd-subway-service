package nextstep.subway.sections;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.sections.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

public class LineTest {

    @Test
    public void getAllStation() {
        //given
        Line line = new Line("1호선", "red", new Section(new Station("강남역"), new Station("판교역"), 10));
        Section section = new Section(new Station("판교역"), new Station("광교역"), 10);
        line.updateSection(section);
        //when
        List<Station> stations = line.orderedStations();
        //then
        assertThat(stations).containsExactly(new Station("강남역"), new Station("판교역"),
            new Station("광교역"));
    }

    @Test
    public void removeStation() {
        //given
        Station removedStation = new Station("양재역");
        Line line = new Line("1호선", "red", new Section(new Station("강남역"), removedStation, 10));
        line.updateSection(new Section(removedStation, new Station("광교역"), 5));
        //when
        line.removeSectionByStation(removedStation);
        List<Station> stations = line.orderedStations();
        //then
        assertThat(stations).containsExactly(new Station("강남역"), new Station("광교역"));
    }
}
