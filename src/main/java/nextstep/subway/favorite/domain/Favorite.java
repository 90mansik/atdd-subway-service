package nextstep.subway.favorite.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Station sourceStation;
    @OneToOne
    private Station targetStation;
    private Long memberId;

    protected Favorite() {
    }

    public Favorite(Station sourceStation, Station targetStation, Long memberId) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Favorite favorite = (Favorite) o;
        return Objects.equals(sourceStation, favorite.sourceStation) && Objects.equals(targetStation,
                favorite.targetStation) && Objects.equals(memberId, favorite.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceStation, targetStation, memberId);
    }
}
