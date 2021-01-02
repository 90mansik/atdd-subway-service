package nextstep.subway.line.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Line extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String name;
	private String color;
	private int extraFare;

	@Embedded
	private Sections sections = new Sections();

	public Line(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public Line(String name, String color, Station upStation, Station downStation, int distance, int extraFare) {
		this.name = name;
		this.color = color;
		this.extraFare = extraFare;
		this.sections.initSection(new Section(this, upStation, downStation, distance));
	}

	public void update(Line line) {
		this.name = line.getName();
		this.color = line.getColor();
	}

	public List<Station> getStations() {
		return this.sections.getStations();
	}

	public void addSection(Section section) {
		this.sections.addSection(section);
	}

	public void removeLineStation(Station targetStation) {
		this.sections.removeLineStation(this, targetStation);
	}

	public List<Section> getSectionsByLine() {
		return this.sections.getSections();
	}
}
