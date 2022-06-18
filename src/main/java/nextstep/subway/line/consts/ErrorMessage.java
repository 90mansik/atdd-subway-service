package nextstep.subway.line.consts;

import java.util.Locale;

public class ErrorMessage {
    public static final String ERROR_LINE_NOT_FOUND = "[ERROR] 노선이 존재하지 않습니다. (id: %d)";
    public static final String ERROR_DISTANCE_TOO_SHORT = "[ERROR] 역과 역 사이의 거리는 %d 미만일 수 없습니다. (입력 거리: %d)";
    public static final String ERROR_SECTION_ADD_ALREADY_REGISTERED = "[ERROR] 이미 등록된 구간입니다.";
    public static final String ERROR_SECTION_ADD_UNKNOWN_STATIONS = "[ERROR] 등록할 수 없는 구간 입니다.";
    public static final String ERROR_SECTION_DELETE_MINIMUM_LENGTH = "[ERROR] 구간이 %d개 이하일 때는 제거할 수 없습니다.";
    public static final String ERROR_SECTION_DELETE_UNKNOWN_STATION = "[ERROR] 노선에 존재하지 않는 Station %s은 제거할 수 없습니다.";
    public static final String ERROR_STATION_NOT_FOUND = "[ERROR] 지하철역이 존재하지 않습니다. (id: %d)";
    public static final String ERROR_PATH_NOT_FOUND = "[ERROR] 경로를 찾을 수 없습니다.";
    public static final String ERROR_PATH_SAME_SOURCE_TARGET = "[ERROR] 경로탐색 시 출발역과 도착역이 같을 수 없습니다.";
    public static final String ERROR_MEMBER_NOT_FOUND = "[ERROR] 회원이 존재하지 않습니다. (id: %d)";
    public static final String ERROR_FAVORITE_NOT_FOUND = "[ERROR] 즐겨찾기가 존재하지 않습니다. (member id: %d, 즐겨찾기 id: %d)";
}
