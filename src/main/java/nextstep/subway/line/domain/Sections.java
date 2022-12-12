package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return this.sections;
    }

    public List<Station> getStations() {
        if (isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.getSections().stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private boolean isEmpty() {
        return this.sections.isEmpty();
    }

    private Station findUpStation() {
        Station downStation = this.sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.getSections().stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public boolean isStationExisted(Station station) {
        return this.getStations().stream().anyMatch(it -> it == station);
    }

    public boolean isStationNotExisted(Station station) {
        return this.getStations().stream().noneMatch(it -> it == station);
    }

    public void addSections(Section section) {
        isValidDuplicate(section);
        isaValidNotExist(section);

        updateUpStation(section);
        updateDownStation(section);

        this.sections.add(section);
    }

    private void updateUpStation(Section section) {
        if (isStationExisted(section.getUpStation())) {
            this.getSections().stream()
                    .filter(it -> it.getUpStation() == section.getUpStation())
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
        }
    }

    private void updateDownStation(Section section) {
        if (isStationExisted(section.getUpStation())) {
            this.getSections().stream()
                    .filter(it -> it.getDownStation() == section.getDownStation())
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
        }
    }

    private void isaValidNotExist(Section section) {
        if (!this.sections.isEmpty()
                && isStationNotExisted(section.getUpStation())
                && isStationNotExisted(section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
        ;
    }

    private void isValidDuplicate(Section section) {
        if (isStationExisted(section.getUpStation()) && isStationExisted(section.getDownStation())) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }
}
