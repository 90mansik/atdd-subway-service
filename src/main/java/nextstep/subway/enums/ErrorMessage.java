package nextstep.subway.enums;

public enum ErrorMessage {
    CANNOT_ADD_SECTION("구간을 추가할 수 없습니다."),
    CANNOT_REMOVE_SECTION("구간을 제거할 수 없습니다."),
    DUPLICATED_SECTION("이미 등록된 구간 입니다."),
    NOT_EXIST_SECTION("추가하려는 구간이 존재하지 않습니다."),
    NOT_FOUND("해당 리소스를 찾을 수 없습니다."),
    INVALID_DISTANCE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요."),
    DUPLICATED_STATION("출발역과 도착역이 중복될 수 없습니다."),
    NOT_FOUND_PATH("경로가 존재하지 않습니다."),
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
