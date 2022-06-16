package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {
    private static final int MIN_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        if (Objects.isNull(sections)) {
            throw new IllegalArgumentException("null 을 입력할수 없습니다.");
        }
        this.sections.addAll(sections);
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }
        return insertStationBySorted();
    }

    public Optional<Station> getStartStation() {
        if (sections.isEmpty()) {
            return Optional.empty();
        }
        if (sections.size() <= MIN_SIZE) {
            return this.sections.stream()
                    .map(Section::getUpStation)
                    .findAny();
        }

        return sections.stream()
                .filter(this::isStartStation)
                .map(Section::getUpStation)
                .findAny();
    }

    public List<Section> getSections() {
        return sections;
    }

    public int isSize() {
        return this.sections.size();
    }

    public boolean isContains(final Section section) {
        return sections.contains(section);
    }

    public void addSection(final Section section) {
        if (!sections.contains(section)) {
            insertSection(section);
            sections.add(section);
        }
    }

    public void removeSection(final Section section) {
        this.sections.remove(section);
        section.remove();
    }

    private void insertSection(final Section section) {
        if (sections.isEmpty()) {
            return;
        }
        final Match match = findInsertSomePlace(section);
        if (match.isUp()) {
            findPreSectionBy(section.getUpStation())
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
        }
        if (match.isDown()) {
            findDownSectionBy(section.getDownStation())
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
        }
    }

    private Match findInsertSomePlace(final Section section) {
        final List<Station> stations = getStations();
        final boolean isUpStationExisted = stations.stream().anyMatch(it -> Objects.equals(it,section.getUpStation()));
        final boolean isDownStationExisted = stations.stream().anyMatch(it -> Objects.equals(it,section.getDownStation()));
        if (Objects.equals(isUpStationExisted, true) && Objects.equals(isDownStationExisted, true)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
        if (Objects.equals(isUpStationExisted, false) && Objects.equals(isDownStationExisted, false)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
        return isUpStationExisted ? Match.UP : Match.DOWN;
    }

    private List<Station> insertStationBySorted() {
        List<Station> result = new ArrayList<>();
        Optional<Station> isStartStation = getStartStation();
        while (isStartStation.isPresent()) {
            Station station = isStartStation.get();
            result.add(station);
            isStartStation = findNextStation(station);
        }
        return result;
    }

    private Optional<Station> findNextStation(final Station station) {
        return sections.stream()
                .filter(section -> section.isMatchUpStation(station))
                .map(Section::getDownStation)
                .findAny();
    }

    private Optional<Section> findPreSectionBy(final Station station) {
        return sections.stream()
                .filter(it -> it.isMatchUpStation(station)).findFirst();
    }

    private Optional<Section> findDownSectionBy(final Station station) {
        return sections.stream()
                .filter(it -> it.isMatchDownStation(station)).findFirst();
    }

    private boolean isStartStation(final Section section) {
        return sections.stream()
                .noneMatch(it -> it.isMatchDownStation(section.getUpStation()))
                && sections.stream()
                .anyMatch(it -> it.isMatchUpStation(section.getDownStation()));
    }
}
