package nextstep.subway.navigation.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.navigation.dto.NavigationResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

import static nextstep.subway.common.Messages.NOT_CONNECTED_SOURCE_TARGET_STATION;

public class Navigation {

    private final WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
    private GraphPath<Station, SectionEdge> path;

    public static Navigation of(List<Line> persistLines) {
        return new Navigation(persistLines);
    }

    private Navigation(List<Line> persistLines) {
        persistLines.forEach(this::settingGraph);
    }

    private void settingGraph(Line line) {
        line.getSections().getSections().forEach(section -> {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            graph.addVertex(upStation);
            graph.addVertex(downStation);

            SectionEdge sectionEdge = graph.addEdge(upStation, downStation);
            sectionEdge.addSection(section);
            graph.setEdgeWeight(sectionEdge, section.getDistance());
        });
    }

    public NavigationResponse findShortest(Station sourceStation, Station targetStation) {
        path = new DijkstraShortestPath<>(graph).getPath(sourceStation, targetStation);
        checkFindShortestPath(path);

        List<Station> vertexList = path.getVertexList();
        int distance = (int) path.getWeight();

        return NavigationResponse.of(vertexList, distance);
    }

    private void checkFindShortestPath(GraphPath<Station, SectionEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException(NOT_CONNECTED_SOURCE_TARGET_STATION);
        }
    }

    public int maxLineExtraFare() {
        return path.getEdgeList().stream()
                .map(SectionEdge::getLine)
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }
}
