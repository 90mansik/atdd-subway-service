package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    private FavoriteResponse() {}

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId(),
                StationResponse.of(favorite.getSource()),
                StationResponse.of(favorite.getTarget())
        );
    }

    public static List<FavoriteResponse> toResponses(List<Favorite> favorite) {
        return favorite.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return this.id;
    }

    public StationResponse getSource() {
        return this.source;
    }

    public StationResponse getTarget() {
        return this.target;
    }
}
