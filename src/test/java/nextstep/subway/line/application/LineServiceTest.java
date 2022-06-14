package nextstep.subway.line.application;

import static nextstep.subway.DomainFixtureFactory.createLine;
import static nextstep.subway.DomainFixtureFactory.createLineRequest;
import static nextstep.subway.DomainFixtureFactory.createSectionRequest;
import static nextstep.subway.DomainFixtureFactory.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {
    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @InjectMocks
    private LineService lineService;

    @DisplayName("노선 저장 테스트")
    @Test
    void saveLine() {
        Station 지하철역 = createStation(1L, "지하철역");
        Station 새로운지하철역 = createStation(2L, "새로운지하철역");
        LineRequest lineRequest = createLineRequest("신분당선", "bg-red-600", 지하철역.id(), 새로운지하철역.id(), 10);
        when(stationService.findById(lineRequest.getUpStationId())).thenReturn(지하철역);
        when(stationService.findById(lineRequest.getDownStationId())).thenReturn(새로운지하철역);
        when(lineRepository.save(lineRequest.toLine(지하철역, 새로운지하철역))).thenReturn(
                createLine(1L, "신분당선", "bg-red-600", 지하철역, 새로운지하철역, Distance.valueOf(10)));

        LineResponse lineResponse = lineService.saveLine(lineRequest);
        assertAll(
                () -> assertThat(lineResponse.getId()).isNotNull(),
                () -> assertThat(lineResponse.getName()).isEqualTo("신분당선")
        );
    }

    @DisplayName("노선에 등록하려는 지하철이 없는 경우 예외가 발생한다.")
    @Test
    void saveLineNotFoundException() {
        Station 지하철역 = createStation(1L, "지하철역");
        Station 새로운지하철역 = createStation(2L, "새로운지하철역");
        LineRequest lineRequest = createLineRequest("신분당선", "bg-red-600", 지하철역.id(), 새로운지하철역.id(), 10);

        when(stationService.findById(lineRequest.getUpStationId())).thenReturn(지하철역);
        when(stationService.findById(lineRequest.getDownStationId())).thenReturn(null);

        assertThatThrownBy(() -> lineService.saveLine(lineRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("하행역 정보가 없습니다.");
    }

    @DisplayName("노선 변경 테스트")
    @Test
    void updateLine() {
        Station 지하철역 = createStation(1L, "지하철역");
        Station 새로운지하철역 = createStation(2L, "새로운지하철역");
        LineRequest lineRequest = createLineRequest("경희선", "bg-blue-600", 지하철역.id(), 새로운지하철역.id(), 10);
        Line 신분당선 = createLine(1L, "신분당선", "bg-red-600", 지하철역, 새로운지하철역, Distance.valueOf(10));
        Line 경희선 = createLine(1L, "경희선", "bg-blue-600", 지하철역, 새로운지하철역, Distance.valueOf(10));

        when(lineRepository.findById(신분당선.id())).thenReturn(Optional.of(신분당선));
        when(stationService.findById(lineRequest.getUpStationId())).thenReturn(지하철역);
        when(stationService.findById(lineRequest.getDownStationId())).thenReturn(새로운지하철역);
        lineService.updateLine(신분당선.id(), lineRequest);

        verify(lineRepository).save(경희선);
    }

    @DisplayName("노선 조회시 없을 경우 예외 테스트")
    @Test
    void findLineByIdException() {
        when(lineRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lineService.findLineResponseById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("등록된 노선이 없습니다.");
    }

    @DisplayName("노선 삭제 테스트")
    @Test
    void deleteLineById() {
        lineService.deleteLineById(1L);
        verify(lineRepository).deleteById(1L);
    }

    @DisplayName("노선에 구간 추가 테스트")
    @Test
    void addLineStation() {
        Station 지하철역 = createStation(1L, "지하철역");
        Station 새로운지하철역 = createStation(2L, "새로운지하철역");
        Station 더새로운지하철역 = createStation(3L, "더새로운지하철역");
        Line 신분당선 = createLine(1L, "신분당선", "bg-red-600", 지하철역, 새로운지하철역, Distance.valueOf(10));
        SectionRequest sectionRequest = createSectionRequest(지하철역.id(), 더새로운지하철역.id(), 5);

        when(lineRepository.findById(신분당선.id())).thenReturn(Optional.of(신분당선));
        when(stationService.findById(sectionRequest.getUpStationId())).thenReturn(지하철역);
        when(stationService.findById(sectionRequest.getDownStationId())).thenReturn(더새로운지하철역);
        lineService.addLineStation(신분당선.id(), sectionRequest);

        verify(lineRepository).save(신분당선);
    }

    @DisplayName("노선에서 역(구간) 제거 테스트")
    @Test
    void removeLineStation() {
        Station 지하철역 = createStation(1L, "지하철역");
        Station 새로운지하철역 = createStation(2L, "새로운지하철역");
        Station 더새로운지하철역 = createStation(3L, "더새로운지하철역");
        Line 신분당선 = createLine(1L, "신분당선", "bg-red-600", 지하철역, 새로운지하철역, Distance.valueOf(10));
        SectionRequest sectionRequest = createSectionRequest(지하철역.id(), 더새로운지하철역.id(), 5);

        when(lineRepository.findById(신분당선.id())).thenReturn(Optional.of(신분당선));
        when(stationService.findById(sectionRequest.getUpStationId())).thenReturn(지하철역);
        when(stationService.findById(sectionRequest.getDownStationId())).thenReturn(더새로운지하철역);
        lineService.addLineStation(신분당선.id(), sectionRequest);

        when(lineRepository.findById(신분당선.id())).thenReturn(Optional.of(신분당선));
        when(stationService.findById(새로운지하철역.id())).thenReturn(새로운지하철역);
        lineService.removeLineStation(신분당선.id(), 새로운지하철역.id());

        verify(lineRepository, times(2)).save(신분당선);
    }
}
