package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
public class Line extends BaseEntity {
    private static final int MIN_SIZE = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private final Sections sections = new Sections();

    @Embedded
    private Price extraCharge;

    public Line() {
        extraCharge = new Price();
    }

    public Line(String name, String color, long extraCharge) {
        this(name, color);
        this.extraCharge = new Price(extraCharge);
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.extraCharge = new Price();
    }


    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color);
        addSection(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
        this.extraCharge = line.getExtraCharge();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Sections getSections() {
        return this.sections;
    }

    public Price getExtraCharge() {
        return extraCharge;
    }

    public Optional<Station> getStartStation() {
        return sections.getStartStation();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int isSize() {
        return sections.isSize();
    }

    public boolean isContains(final Section section) {
        return this.sections.isContains(section);
    }

    public void addSection(final Section section) {
        if (!sections.isContains(section)) {
            sections.addSection(section);
        }
        if (!Objects.equals(this, section.getLine())) {
            section.updateLineBy(this);
        }
    }

    public void removeSection(final Station station) {
        if (this.sections.isSize() <= MIN_SIZE) {
            throw new IllegalStateException("구간이 한개 뿐이거나 없는 경우에 삭제할수 없습니다.");
        }
        removeSectionBy(station);
    }

    public void removeSection(final Section section) {
        if (this.sections.isContains(section)) {
            this.sections.removeSection(section);
        }
    }

    private void removeSectionBy(Station station) {
        final Optional<Section> isSectionMatchesUpStation =
                sections.getSections().stream()
                        .filter(it -> it.isMatchUpStation(station))
                        .findFirst();

        final Optional<Section> isSectionMatchesDownStation =
                sections.getSections().stream()
                        .filter(it -> it.isMatchDownStation(station))
                        .findFirst();

        isSectionMatchesUpStation.ifPresent(this::removeSection);
        isSectionMatchesDownStation.ifPresent(this::removeSection);

        if (isSectionMatchesUpStation.isPresent() && isSectionMatchesDownStation.isPresent()) {
            final Section sectionMatchesDownStation = isSectionMatchesDownStation.orElseThrow(EntityNotFoundException::new);
            final Section sectionMatchesUpStation = isSectionMatchesUpStation.orElseThrow(EntityNotFoundException::new);
            this.addSection(
                    new Section(
                            this, sectionMatchesDownStation.getUpStation(),
                            sectionMatchesUpStation.getDownStation(),
                            sectionMatchesUpStation.getDistance().plus(sectionMatchesDownStation.getDistance())));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
