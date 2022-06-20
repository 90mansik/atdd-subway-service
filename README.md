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

## 1단계 - 인수 테스트 기반 리팩터링
### 요구사항 설명
- LineService의 비즈니스 로직을 도메인으로 옮기기
- 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링하기
- 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링하기
1. Domain으로 옮길 로직을 찾기
  - 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
  - 객체지향 생활체조를 참고
2. Domain의 단위 테스트를 작성하기
   - 서비스 레이어에서 옮겨 올 로직의 기능을 테스트
   - ㄴectionsTest나 LineTest 클래스가 생성될 수 있음
3. 로직을 옮기기
   - 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
   - 정상 동작 확인 후 기존 로직 제거
4. 인수테스트 통합

### 피드백 (리뷰어: [오경태](https://github.com/ohtaeg) 님)
- [1단계 코드리뷰](https://github.com/next-step/atdd-subway-service/pull/591)

## 2단계 - 경로 조회 기능
### 요구사항
- 최단 경로 조회 인수 테스트 만들기
- 최단 경로 조회 기능 구현하기

### 구현
- 다익스트라 라이브러리 jgrapht 사용 
  - 전략패턴 적용
- 인수테스트 및 도메인 TDD 작성
- 리팩토링
  - Transactional readOnly 옵션을 클래스 기본 레벨로 승격
  - ExceptionHandler 를 ControllerAdvice 에서 처리
    - @ControllerAdvice -> @RestControllerAdvice 변경
  - 객체지향 생활체조 Depth 1 준수

### 피드백 (리뷰어: [오경태](https://github.com/ohtaeg) 님)
- [2단계 코드리뷰](https://github.com/next-step/atdd-subway-service/pull/618)

## 3단계 - 인증을 통한 기능 구현
### 요구사항 
- 토큰 발급 기능 (로그인) 인수 테스트 만들기
- 인증 - 내 정보 조회 기능 완성하기
- 인증 - 즐겨 찾기 기능 완성하기

### 구현목록
- 토큰 발급 인수테스트 작성
- 인증 - 내 정보 조회 기능 완성하기
  - 인수 테스트 작성
- 즐겨찾기 기능 구현 및 인수테스트 작성

### 피드백 (리뷰어: [오경태](https://github.com/ohtaeg) 님)
- [3단계 코드리뷰](https://github.com/next-step/atdd-subway-service/pull/634)

## 4단계 - 요금조회
### 요구사항
#### 거리별 요금 정책
- 기본운임(10㎞ 이내) : 기본운임 1,250원
- 이용 거리초과 시 추가운임 부과
  - 10km초과∼50km까지(5km마다 100원)
  - 50km초과 시 (8km마다 100원)

#### 노선별 추가 요금 정책
- 노선에 추가 요금 필드를 추가
- 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
  - ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
  - ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
- 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
  - ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원

#### 로그인 사용자의 경우 연령별 요금 할인 적용
- 청소년: 운임에서 350원을 공제한 금액의 20%할인
- 어린이: 운임에서 350원을 공제한 금액의 50%할인
```
- 청소년: 13세 이상~19세 미만
- 어린이: 6세 이상~ 13세 미만
```

### 구현
- 라인 추가요금 필드 추가
- 계산 로직
  1. 기본요금과 추가요금을 합한다
  2. 거리요금을 구하여 1을 합한다 -> DistanceType enum 으로 구현
  3. 연령별 할인율을 구하여 2와 합한다 -> AgeDiscountType enum 으로 구현
- 인수 테스트 및 도메인 테스트 추가

### 피드백 (리뷰어: [오경태](https://github.com/ohtaeg) 님)
- [4단계 코드리뷰](https://github.com/next-step/atdd-subway-service/pull/652)
