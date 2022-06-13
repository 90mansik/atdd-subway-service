package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.ExceptionType;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;
import org.springframework.util.StringUtils;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    private int additionalFare;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    private Line(String name, String color) {
        validateLine(name, color);
        this.name = name;
        this.color = color;
    }

    private Line(String name, String color, Station upStation, Station downStation, int distance, int additionalFare) {
        validateLine(name, color);
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
        this.additionalFare = additionalFare;
    }

    public static Line of(String name, String color) {
        return new Line(name, color);
    }

    public static Line of(String name, String color, Station upStation, Station downStation, int distance, int additionalFare) {
        return new Line(name, color, upStation, downStation, distance, additionalFare);
    }

    private void validateLine(String name, String color) {
        if (!StringUtils.hasText(name)) {
            throw new BadRequestException(ExceptionType.IS_NOT_NULL_LINE_NAME);
        }

        if (!StringUtils.hasText(color)) {
            throw new BadRequestException(ExceptionType.IS_NOT_NULL_LINE_COLOR);
        }
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void registerSection(Section section) {
        this.sections.add(section);
    }

    public Section createSection(Station upStation, Station downStation, int distance) {
        Section section = new Section(this, upStation, downStation, distance);
        this.sections.validate(section);
        return section;
    }

    public void removeStation(Station station) {
        this.sections.removeStation(station);
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

    public List<Station> getStations() {
        return sections.getOrderedStations();
    }

    public List<Section> getSections() {
        return sections.getItems();
    }

    public int getAdditionalFare() {
        return additionalFare;
    }
}
