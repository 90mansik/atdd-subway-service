package nextstep.subway.line.domain;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

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
    private final Sections sections = new Sections();

    public Line() {
    }

    private Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    private Line(
            String name, String color,
            Station upStation, Station downStation,
            Distance distance, int additionalFare
    ) {
        this(name, color);
        this.additionalFare = additionalFare;
        sections.addSection(this, upStation, downStation, distance);
    }

    public static Line of(String name, String color){
        return new Line(name, color);
    }

    public static Line of(
            String name, String color,
            Station upStation, Station downStation,
            Distance distance, int additionalFare
    ){
        return new Line(name, color, upStation, downStation, distance, additionalFare);
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Station upStation, Station downStation, Distance distance) {
        sections.addSection(this, upStation, downStation, distance);
    }

    public void removeStation(Station station) {
        sections.removeStation(this, station);
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
        return sections;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int getAdditionalFare() {
        return additionalFare;
    }

    public void setAdditionalFare(int additionalFare) {
        this.additionalFare = additionalFare;
    }
}
