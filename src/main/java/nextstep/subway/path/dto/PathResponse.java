package nextstep.subway.path.dto;

import java.util.List;
import java.util.Objects;
import nextstep.subway.path.domain.PathFindResult;
import nextstep.subway.path.domain.SubwayFare;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> stations;

    private int distance;

    private SubwayFare fare;

    protected PathResponse() {

    }

    public PathResponse(List<StationResponse> stations, int distance, SubwayFare fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(PathFindResult findResult) {
        return new PathResponse(StationResponse.of(findResult.getStations()), findResult.getDistance(), findResult.getFare());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public SubwayFare getFare() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PathResponse that = (PathResponse) o;
        return distance == that.distance && Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }
}
