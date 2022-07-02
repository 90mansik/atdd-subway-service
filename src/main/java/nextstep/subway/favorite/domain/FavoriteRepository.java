package nextstep.subway.favorite.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByMember_id(Long memberId);

    Optional<Favorite> findByMember_idAndId(Long memberId, Long id);
}
