package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    private Station 강남역;
    private Station 광교역;
    private Member 사용자;
    private Member 다른_사용자;
    private LoginMember 로그인_사용자;
    private Favorite 즐겨찾기;
    private Favorite 다른_즐겨찾기;
    private List<Favorite> 즐겨찾기_목록;
    @Mock
    private MemberService memberService;
    @Mock
    private StationService stationService;
    @Mock
    private FavoriteRepository favoriteRepository;
    @InjectMocks
    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        광교역 = new Station(2L, "광교역");
        사용자 = new Member("email@email.com", "password", 10);
        다른_사용자 = new Member("other@email.com", "password", 20);
        로그인_사용자 = new LoginMember(1L, "email@email.com", 10);
        즐겨찾기 = new Favorite(강남역, 광교역, 사용자);
        다른_즐겨찾기 = new Favorite(강남역, 광교역, 다른_사용자);
        즐겨찾기_목록 = Arrays.asList(즐겨찾기);
    }

    @Test
    void createFavorite() {
        when(memberService.findById(로그인_사용자.getId())).thenReturn(사용자);
        when(stationService.findById(강남역.getId())).thenReturn(강남역);
        when(stationService.findById(광교역.getId())).thenReturn(광교역);

        FavoriteResponse response = favoriteService.createFavorite(로그인_사용자.getId(), new FavoriteRequest(강남역.getId(), 광교역.getId()));
        assertAll(
                () -> assertThat(response.getSource().getId()).isEqualTo(강남역.getId()),
                () -> assertThat(response.getTarget().getId()).isEqualTo(광교역.getId())
        );
    }

    @Test
    void validateAlreadyExist() {
        when(memberService.findById(로그인_사용자.getId())).thenReturn(사용자);
        when(stationService.findById(강남역.getId())).thenReturn(강남역);
        when(stationService.findById(광교역.getId())).thenReturn(광교역);
        when(favoriteRepository.findBySourceAndTargetAndMember(강남역, 광교역, 사용자)).thenReturn(Optional.of(즐겨찾기));

        assertThatThrownBy(
                () -> favoriteService.createFavorite(로그인_사용자.getId(), new FavoriteRequest(강남역.getId(), 광교역.getId()))
        )
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("이미 등록하였습니다.");
    }

    @Test
    void getFavorites() {
        when(favoriteRepository.findAllByMemberId(로그인_사용자.getId())).thenReturn(즐겨찾기_목록);

        List<FavoriteResponse> responses = favoriteService.getFavorites(로그인_사용자.getId());

        assertAll(
                () -> assertThat(responses.get(0).getSource().getId()).isEqualTo(강남역.getId()),
                () -> assertThat(responses.get(0).getTarget().getId()).isEqualTo(광교역.getId())
        );
    }

    @Test
    void deleteFavorite() {
        when(favoriteRepository.findByIdAndMemberId(즐겨찾기.getId(), 로그인_사용자.getId())).thenReturn(Optional.of(즐겨찾기));

        favoriteService.deleteFavorite(로그인_사용자.getId(), 즐겨찾기.getId());

        verify(favoriteRepository, times(1)).delete(any());
    }
}
