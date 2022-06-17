package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void addLineStation(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        List<Station> stations = getStations();
        boolean isUpStationExisted = isUpStationExisted(stations, section.getUpStation());
        boolean isDownStationExisted = isDownStationExisted(stations, section.getDownStation());

        validateAlreadyExists(isUpStationExisted, isDownStationExisted);
        validateNotExists(stations, section);

        if (isUpStationExisted) {
            findSectionByUpStation(section.getUpStation())
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
            sections.add(section);
            return;
        }

        if (isDownStationExisted) {
            findSectionByDownStation(section.getDownStation())
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
            sections.add(section);
            return;
        }

        throw new RuntimeException();
    }

    private void validateAlreadyExists(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void validateNotExists(List<Station> stations, Section section) {
        if (!stations.isEmpty()
                && stations.stream().noneMatch(it -> it.equals(section.getDownStation()))
                && stations.stream().noneMatch(it -> it.equals(section.getUpStation()))) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        return getStationsInOrder();
    }

    private boolean isUpStationExisted(List<Station> stations, Station station) {
        return stations.stream().anyMatch(it -> it.equals(station));
    }

    private boolean isDownStationExisted(List<Station> stations, Station station) {
        return stations.stream().anyMatch(it -> it.equals(station));
    }

    private List<Station> getStationsInOrder() {
        List<Station> stations = new ArrayList<>();
        Station station = findUpFinalStation();

        while (station != null) {
            stations.add(station);
            station = findNextStation(station);
        }
        return stations;
    }

    private Station findUpFinalStation() {
        List<Station> upStations = sections.stream()
                .map(Section::getUpStation).collect(Collectors.toList());
        List<Station> downStations = sections.stream()
                .map(Section::getDownStation).collect(Collectors.toList());
        upStations.removeAll(downStations);

        if (upStations.size() != 1) {
            throw new NoSuchElementException("상행종점역을 찾을 수 없습니다.");
        }
        return upStations.get(0);
    }

    private Station findNextStation(Station station) {
        return sections.stream()
                .filter(section -> section.getUpStation().equals(station))
                .findFirst()
                .orElse(new Section())
                .getDownStation();
    }

    private Optional<Section> findSectionByUpStation(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation().equals(station))
                .findFirst();
    }

    private Optional<Section> findSectionByDownStation(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation().equals(station))
                .findFirst();
    }
}
