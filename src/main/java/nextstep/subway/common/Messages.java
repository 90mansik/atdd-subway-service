package nextstep.subway.common;

public class Messages {
    public static final String ALREADY_REGISTERED_STATION = "[ERROR] 이미 등록된 구간 입니다.";
    public static final String UNREGISTERED_STATION = "[ERROR] 등록할 수 없는 구간 입니다.";

    public static final String LEAST_ONE_MUST_EXIST_REMOVE_STATION = "[ERROR] 노선 삭제할 노선은 최소 1개이상 존재해야합니다.";
    public static final String NOT_MATCH_REMOVE_STATION = "[ERROR] 삭제할 노선의 정보가 없습니다.";

    public static final String DISTANCE_BETWEEN_STATION = "[ERROR] 역과 역 사이의 거리보다 좁은 거리를 입력해주세요";

    public static final String SAME_SOURCE_TARGET_STATION = "[ERROR] 출발역과 도착역은 같을 수 없습니다.";
    public static final String NOT_CONNECTED_SOURCE_TARGET_STATION = "[ERROR] 출발역과 도착역이 연결이 되어있지 않습니다.";

    public static final String INVALID_TOKEN = "[ERROR] 유효하지 않은 토큰 정보입니다.";
    public static final String REQUIRED_MEMBER = "[ERROR] 로그인 정보는 필수입니다.";
    public static final String NOT_EQUALS_MEMBER = "[ERROR] 동일한 회원의 정보가 아닙니다.";
}
