package nextstep.subway.line.domain;

import javax.persistence.criteria.CriteriaBuilder.In;
import nextstep.subway.BaseEntity;
import nextstep.subway.line.domain.collections.Sections;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    private Integer extraCharge;
    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Integer extraCharge) {
        this.name = name;
        this.color = color;
        this.extraCharge = extraCharge;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
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

    public Sections getSections() {
        return sections;
    }

    public void addSection(Section section) {
        sections.addSection(section);
        section.setLine(this);
    }

    public void removeSection(Station station) {
        sections.removeSection(this, station);
    }

    public void addNewSection(Station upStation, Station downStation, int distance) {
        sections.addNewSection(this, upStation, downStation, distance);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void makeVertexByStationsTo(WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap) {
        sections.addVertexByStations(subwayMap);
    }

    public void makedgeBySectionsTo(WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap) {
        sections.addEdgeBySections(subwayMap);
    }
}
