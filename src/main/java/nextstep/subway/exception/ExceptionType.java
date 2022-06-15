package nextstep.subway.exception;

import java.util.Objects;

public enum ExceptionType {
    IS_NOT_OVER_ORIGIN_DISTANCE("등록할 구간이 기존 구간보다 길거나 같을 수 없습니다."),
    IS_EXIST_BOTH_STATIONS("이미 등록된 구간 입니다."),
    CAN_NOT_REGISTER_SECTION("등록할 수 없는 구간 입니다."),
    MUST_BE_AT_LEAST_LENGTH_ONE("길이가 최소 1 이상 이어야 합니다."),
    IS_EMPTY_LINE_NAME("지하철 노선명이 없습니다."),
    IS_EMPTY_LINE_COLOR("지하철 노선 색상이 없습니다."),
    IS_EMPTY_LINE_DISTANCE("노선사이의 거리가 없습니다."),
    IS_ADD_BETWEEN_STATION("역 사이에만 노선을 추가 할 수 있습니다."),
    NOT_FOUND_LINE("노선을 찾을 수 없습니다."),
    INVALID_STATION_ID("잘못된 지하철역 ID 입니다."),
    NOT_FOUND_LINE_STATION("해당 노선의 역을 찾을 수 없습니다."),
    NOT_FOUND_STATION("해당 지하철역을 찾을 수 없습니다."),
    CAN_NOT_DELETE_LINE_STATION("해당 노선의 지하철역을 삭제할 수 없습니다."),
    IS_NOT_NULL_LINE_NAME("노선의 이름은 필수값입니다."),
    IS_NOT_NULL_LINE_COLOR("노선의 색상은 필수값입니다."),
    CAN_NOT_SAME_STATION("두 역이 같을 수 없습니다."),
    IS_NOT_CONNECTED_STATION("출발역과 도착역이 연결되어있지 않습니다."),
    INVALID_TOKEN("토큰정보가 유효하지 않습니다."),
    NOT_FOUND_MEMBER("존재하지 않는 회원입니다."),
    NOT_FOUND_FAVORITE("즐겨찾기 항목을 찾을 수 없습니다."),
    NOT_THE_MEMBER_FAVORITE("즐겨찾기를 등록한 회원이 아닙니다."),
    ALREADY_REGISTERED_FAVORITE("이미 등록된 즐겨찾기입니다."),
    INVALID_DISTANCE("잘못된 거리값입니다."),
    INVALID_AGE("잘못된 나이입니다.");


    private final String message;

    ExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(Long id) {
        if (Objects.nonNull(id)) {
            return message + " [" + id + "]";
        }

        return message;
    }
}
