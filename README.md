<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/next-step/atdd-subway-admin">
</p>

<br>

# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```
<br>

## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/next-step/atdd-subway-service/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-service/blob/master/LICENSE.md) licensed.

***
# 🚀 1단계 - 인수 테스트 기반 리팩터링

## 📄 요구사항
- LineService 리팩터링
- (선택) LineSectionAcceptanceTest 리팩터링

## 📄 요구사항 설명
- 인수 테스트 기반 리팩터링
    + LineService의 비즈니스 로직을 도메인으로 옮기기
    + 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링하기
    + 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링하기
1. Domain으로 옮길 로직을 찾기
    + 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
    + 객체지향 생활체조를 참고
2. Domain의 단위 테스트를 작성하기
    + 서비스 레이어에서 옮겨 올 로직의 기능을 테스트
    + SectionsTest나 LineTest 클래스가 생성될 수 있음
3. 로직을 옮기기
    + 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
    + 정상 동작 확인 후 기존 로직 제거

- (선택) 인수 테스트 통합
    + API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
    + 반드시 하나의 시나리오로 통합할 필요는 없음, 기능의 인수 조건을 설명할 때 하나 이상의 시나리오가 필요한 경우 여러개의 시나리오를 만들어 인수 테스트를 작성할 수 있음

***
# 🚀 2단계 - 경로 조회 기능

## 📄 요구사항
- 최단 경로 조회 인수 테스트 만들기
- 최단 경로 조회 기능 구현하기

## 힌트
- 최단 경로 라이브러리
    + jgrapht 라이브러리를 활용하면 간편하게 최단거리를 조회할 수 있음
        * 정점(vertext)과 간선(edge), 그리고 가중치 개념을 이용
        * 정점: 지하철역(Station)
        * 간선: 지하철역 연결정보(Section)
        * 가중치: 거리
    + 최단 거리 기준 조회 시 가중치를 거리로 설정
-외부 라이브러리 테스트
    + 외부 라이브러리의 구현을 수정할 수 없기 때문에 단위 테스트를 하지 않음
    + 외부 라이브러리를 사용하는 직접 구현하는 로직을 검증해야 함
    + 직접 구현하는 로직 검증 시 외부 라이브러리 부분은 실제 객체를 활용
- 예외 상황 예시
    + 출발역과 도착역이 같은 경우
    + 출발역과 도착역이 연결이 되어 있지 않은 경우
    + 존재하지 않은 출발역이나 도착역을 조회 할 경우
- 미션 수행 순서
    + 인수 테스트 성공 시키기
        * mock 서버와 dto를 정의하여 인수 테스트 성공 시키기
    + 기능 구현
        * TDD의 방향보다 테스트를 통해 구현할 기능을 명세하는것과 리팩터링이 더 중요합니다!
    + Outside In 경우
        * 컨트롤러 레이어 구현 이후 서비스 레이어 구현 시 서비스 테스트 우선 작성 후 기능 구현
        * 서비스 테스트 내부에서 도메인들간의 로직의 흐름을 검증, 이 때 사용되는 도메인은 mock 객체를 활용
        * 외부 라이브러리를 활용한 로직을 검증할 때는 가급적 실제 객체를 활용
        * Happy 케이스에 대한 부분만 구현( Side 케이스에 대한 구현은 다음 단계에서 진행)
    + Inside Out 경우
        * 도메인 설계 후 도메인 테스트를 시작으로 기능 구현 시작
        * 해당 도메인의 단위 테스트를 통해 도메인의 역할과 경계를 설계
        * 도메인의 구현이 끝나면 해당 도메인과 관계를 맺는 객체에 대해 기능 구현 시작
        ```
        ex) 경로 조회를 수행하는 도메인 구현 예시
        - 1. PathFinder 라는 클래스 작성 후 경로 조회를 위한 테스트를 작성
        - 2. 경로 조회 메서드에서 Line을 인자로 받고 그 결과로 원하는 응답을 리턴하도록 테스트 완성
        - 3. 테스트를 성공시키기 위해 JGraph의 실제 객체를 활용(테스트에서는 알 필요가 없음)
        ```

***
# 🚀 3단계 - 구간 추가 기능

## 요구사항
- 토큰 발급 기능 (로그인) 인수 테스트 만들기
- 인증 - 내 정보 조회 기능 완성하기
- 인증 - 즐겨 찾기 기능 완성하기

## 요구사항 설명
- 토큰 발급 인수 테스트
    + 토큰 발급(로그인)을 검증하는 인수 테스트 만들기
    + `AuthAcceptanceTest` 인수 테스트 만들기
    + 이메일과 패스워드를 이용하여 요청 시 access token을 응답하는 기능을 구현하기
        * AuthAcceptanceTest을 만족하도록 구현하면 됨
        * AuthAcceptanceTest에서 제시하는 예외 케이스도 함께 고려하여 구현하기
    + Bearer Auth 유효하지 않은 토큰 인수 테스트
        * 유효하지 않은 토큰으로 /members/me 요청을 보낼 경우에 대한 예외 처리
- 내 정보 조회 기능
    + 인수 테스트
        * MemberAcceptanceTest 클래스의 manageMyInfo메서드에 인수 테스트를 추가하기
        * 내 정보 조회, 수정, 삭제 기능을 /members/me 라는 URI 요청으로 동작하도록 검증
        * 로그인 후 발급 받은 토큰을 포함해서 요청 하기
    + 토큰을 통한 인증
        * /members/me 요청 시 토큰을 확인하여 로그인 정보를 받아올 수 있도록 하기
        * @AuthenticationPrincipal과 AuthenticationPrincipalArgumentResolver을 활용하기
        * 아래의 기능이 제대로 동작하도록 구현하기
- 즐겨 찾기 기능 구현하기
    + 즐겨찾기 기능을 완성하기
    + 인증을 포함하여 전체 ATDD 사이클을 경험할 수 있도록 기능을 구현하기

## 힌트
- 인증 기반 인수 테스트
    + 사용자 정보를 인수 테스트 메서드의 첫번째 파라미터로 넘겨줄 수 있음 
    + 참고로 코틀린에서는 확장 함수를 활용하여 작성할 수 있음

***
# 🚀 4단계 - 요금 조회

## 요구사항
- 경로 조회 시 거리 기준 요금 정보 포함하기
- 노선별 추가 요금 정책 추가
- 연령별 할인 정책 추가

## 요구사항 설명
- 거리별 요금 정책
    + 기본운임(10㎞ 이내) : 기본운임 1,250원
    + 이용 거리초과 시 추가운임 부과
        * 10km초과∼50km까지(5km마다 100원)
        * 50km초과 시 (8km마다 100원)
    ```
    지하철 운임은 거리비례제로 책정됩니다. (실제 이동한 경로가 아닌 최단거리 기준으로 계산)
    ```
- 노선별 추가 요금 정책
    + 노선에 추가 요금 필드를 추가
    + 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
        * ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
        * ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
    + 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
        * ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원
- 로그인 사용자의 경우 연령별 요금 할인 적용
    + 청소년: 운임에서 350원을 공제한 금액의 20%할인
    + 어린이: 운임에서 350원을 공제한 금액의 50%할인
    ```
    - 청소년: 13세 이상~19세 미만
    - 어린이: 6세 이상~ 13세 미만
    ```

## 힌트
- /paths 요청 시 LoginMember 객체 처리
    + 로그인 시 LoginMember 객체 를 활용하여 연령별 요금 할인을 적용할 수 있음
    + 비 로그인 시 LoginMember는 비어있는 객체가 넘어가므로 별도의 처리가 필요함
    + 필요 시 아래 구문에서 null object를 리턴해주는 부분을 예외를 던지도록 수정해도 무방함
    ```
    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
        return new LoginMember(); // <--- 이 부분 변경 가능
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }
    ```
- 5km 마다 100원 추가 로직
    ```
    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
    ```
