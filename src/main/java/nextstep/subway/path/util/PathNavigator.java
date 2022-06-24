package nextstep.subway.path.util;

import java.util.List;
import nextstep.subway.exceptions.SourceAndTargetSameException;
import nextstep.subway.exceptions.SourceNotConnectedWithTargetException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.SectionWeightedEdge;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class PathNavigator {

    private final WeightedMultigraph<Station, SectionWeightedEdge> graph;
    private final DijkstraShortestPath dijkstraShortestPath;

    private PathNavigator(final List<Line> lines) {
        this.graph = new WeightedMultigraph<>(SectionWeightedEdge.class);
        for (final Line line : lines) {
            setLineToGraph(line);
        }
        this.dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    private void setLineToGraph(final Line line) {
        line.getSections().forEach(section -> {
            final Station upStation = section.getUpStation();
            final Station downStation = section.getDownStation();
            graph.addVertex(upStation);
            graph.addVertex(downStation);
            final SectionWeightedEdge sectionWeightedEdge = new SectionWeightedEdge(section);
            graph.addEdge(upStation, downStation, sectionWeightedEdge);
            graph.setEdgeWeight(sectionWeightedEdge, section.getDistance());
        });
    }

    public Path getPath(final Station sourceStation, final Station targetStation) {
        final GraphPath<Station, SectionWeightedEdge> graphPath = dijkstraShortestPath.getPath(sourceStation,
                targetStation);
        checkSourceEqualToTarget(sourceStation, targetStation);
        checkSourceNotConnectedWithTarget(graphPath);
        final int maxSurcharge = ChargeCalculator.calculateMaxSurcharge(graphPath.getEdgeList());
        return new Path(graphPath.getVertexList(), (int) graphPath.getWeight(), maxSurcharge);
    }

    private void checkSourceNotConnectedWithTarget(final GraphPath<Station, SectionWeightedEdge> graphPath) {
        if (graphPath == null) {
            throw new SourceNotConnectedWithTargetException();
        }
    }

    private void checkSourceEqualToTarget(final Station sourceStation, final Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new SourceAndTargetSameException();
        }
    }

    public static PathNavigator of(final List<Line> lines) {
        return new PathNavigator(lines);
    }
}
