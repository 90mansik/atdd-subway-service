package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private Station source;
    private Station target;
    private Line line;
    private int weight;

    private SectionEdge(Station source, Station target, Line line, int weight) {
        this.source = source;
        this.target = target;
        this.line = line;
        this.weight = weight;
    }

    public static SectionEdge of(Section section) {
        return new SectionEdge(section.getUpStation(), section.getDownStation(), section.getLine(),
                section.getDistance());
    }

    @Override
    public Station getSource() {
        return source;
    }

    @Override
    public Station getTarget() {
        return target;
    }

    public Line getLine() {
        return line;
    }

    @Override
    public double getWeight() {
        return weight;
    }
}
