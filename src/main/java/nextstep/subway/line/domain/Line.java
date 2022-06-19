package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.addLineStation(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addLineStation(Section section) {
        sections.addLineStation(section);
    }

    public void addUpStationExisted(Section section) {
        sections.addUpStationExisted(section);
    }

    public void addDownStationExisted(Section section) {
        sections.addDownStationExisted(section);
    }

    public void removeLineStation(Section section) {
        sections.removeLineStation(section);
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

    public boolean emptySections() {
        return sections.getSections().isEmpty();
    }

    public int sizeSections() {
        return sections.getSections().size();
    }
    public Section getSection(int index) {
        return sections.getSections().get(index);
    }

    public Sections getSections() {
        return sections;
    }
}
