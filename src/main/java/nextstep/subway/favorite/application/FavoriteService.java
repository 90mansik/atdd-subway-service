package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.ErrorMessage;
import nextstep.subway.common.exception.DuplicationException;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public FavoriteResponse register(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberService.findById(loginMember.getId());
        Station source = stationService.findById(request.getSourceId());
        Station target = stationService.findById(request.getTargetId());

        validateDuplication(member, source, target);

        Favorite favorite = new Favorite(member.getId(), source, target);
        favoriteRepository.save(favorite);
        return FavoriteResponse.of(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAll(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());

        return FavoriteResponse.of(favorites);
    }

    public void remove(LoginMember loginMember, Long id) {
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.FAVORITE_NOT_FOUND));

        Member member = memberService.findById(loginMember.getId());
        favorite.validateMember(loginMember.getId());

        favoriteRepository.delete(favorite);
    }

    private void validateDuplication(Member member, Station source, Station target) {
        if (favoriteRepository.existsByMemberIdAndSourceAndTarget(member.getId(), source, target)) {
            throw new DuplicationException(ErrorMessage.FAVORITE_DUPLICATION);
        }
    }
}
