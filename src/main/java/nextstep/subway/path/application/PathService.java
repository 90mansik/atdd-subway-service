package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.exceptions.StationNotExistException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.util.ChargeCalculator;
import nextstep.subway.path.util.PathNavigator;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse getShortestPaths(final Long source, final Long target) {

        final Station sourceStation = stationRepository.findById(source).orElseThrow(StationNotExistException::new);
        final Station targetStation = stationRepository.findById(target).orElseThrow(StationNotExistException::new);

        final List<Line> lines = lineRepository.findAll();
        final PathNavigator pathNavigator = PathNavigator.of(lines);

        final Path path = pathNavigator.getPath(sourceStation, targetStation);
        final int charge = ChargeCalculator.calculate(path.getDistance());

        return new PathResponse(path, charge);
    }
}
