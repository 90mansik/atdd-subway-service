package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    private int fare;

    @Embedded
    private final Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int fare) {
        this.name = name;
        this.color = color;
        this.fare = fare;
        sections.addSection(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
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

    public List<Section> getSections() {
        return sections.getSections();
    }
    public void addSection(Station upStation, Station downStation, int distance) {
        this.sections.addSection(new Section(this, upStation, downStation, distance));
    }

    public List<Station> getStations() {
        return sections.getOrderStations();
    }

    public void removeSection(Station station) {
        sections.removeSection(station);
    }

    public boolean hasStation(Station station) {
        return sections.hasStation(station);
    }

    public int getFare() {
        return fare;
    }
}
