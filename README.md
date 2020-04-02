# eco-trip 시스템

## 개발 프레임워크 및 라이브러리

- Java 14
- Gradle 6.3
- Spring Boot 2.2.6
- Hibernate 5.4.12
- QueryDsl 4.2.2
- KOMORAN 3.3.4
- JUnit 5



## 모듈 구조

추후 관리자/사용자 시스템이 분리될 수 있다고 가정하고, 공통-도메인-응용 모듈을 구분해서 멀티 모듈 프로젝트로 구성

- _global: 공통 모듈 라이브러리
- domain: 도메인 모델 라이브러리
- api-admin: 생태 여행 정보 시스템 웹 애플리케이션



## 주요 정책 및 전략

### 멀티 row 처리 기준

- 숫자로 시작하는 행을 데이터의 실제 시작 부분으로 간주하고, 이후 숫자로 시작하는 다른 행이 나올 때까지 모두 한 행으로 병합

### 데이터의 컬럼 구분 기준

- `"`로 둘러싸인 부분을 하나의 컬럼으로 간주하고, `,` 기준으로 구분 
- 쌍따옴표 외부에 있는 `,,` 는 내용 없는 컬럼으로 간주
- 주요 로직: EcoProgramParser 참고

### 서비스 지역 분류 기준

- `시도 시군구`를 표준 형식으로 하되 시군구 정보가 없는 경우 `시도`만으로 서비스 지역 분류
- 행정 구역 데이터는 data.go.kr 에서 가져와서 정제한 파일을 애플리케이션 구동 시 미리 입력
- 실사용자는 서비스 지역 검색 시 시군구 단위까지만 입력한다고 가정
- 행정 단위는 공백으로 구분
- 여러 서비스 지역에 걸치더라도 하나의 `시도`에 속한 `시군구`사이에서만 걸친다고 가정
  - ex) `전라남도 완도군 및 해남군 일대`의 완도군과 해남군은 모두 전라남도 소속으로 가정
- `시도`만 있는 경우와, `시도 시군구`로 추출되는 경우는 정규 케이스로 보고 입력 내용 그대로 DB 검색 만으로 처리
- DB 검색 만으로 처리되지 않는 시군은 공백으로 구분한 검색어를 추출하고 이를 통해 시군구 지역 추출
- 공백 구분 검색어 추출까지 해도 추출되는 지역 정보가 없는 경우 형태소 분석을 통해 시군구 지역 추출
  - ex) `전라남도 완도군~여수시`는 형태소 분석을 통해 `~`를 걸러내고 완도군과 여수시 추출
- 주요 로직: RegionServiceImpl 참고


### 테스트 전략

- 지나친 Mock 처리를 수반하는 Layer 별 테스트는 초기에만 사용하고, 이후에는 컨트롤러 계층의 통합 테스트 위주로 TDD로 개발
- 조회와 명령 테스트를 구분하고 조회 테스트에는 문제에 주어진 데이터를 테스트 시작 시 로딩해서 사용

### 기타

- 글로벌 예외 처리 및 메시지 구조는 `_global` 모듈에 만들어두었으나 시간 부족으로 실제 코드에서는 `RuntimeException`으로 간단하게 처리


## 빌드 및 실행 방법

### 빌드 및 실행

- `api-domain` 디렉터리에서 `./gradlew bootRun` 실행
- 서버는 8090 포트에서 구동

### signup

```
curl -i --request POST \
  --url http://localhost:8090/admin/members/signup \
  --header 'content-type: application/json' \
  --data '{
    "username": "testuser",
    "password": "asdf!@#$"
}'
```

### signin

```
curl -i --request POST \
  --url http://localhost:8090/admin/members/signin \
  --header 'content-type: application/json' \
  --data '{
    "username": "testuser",
    "password": "asdf!@#$"
}'
```

### token refresh

```
curl --request GET \
  --url http://localhost:8090/admin/members/refresh-token \
  --header 'authorization: Bearer <<<TOKEN>>>'
```

### 파일 업로드

```
curl -i -X POST -H "Content-Type: multipart/form-data" \
  -F "file=@사전과제2.csv" \
  http://localhost:8090/admin/eco-programs/upload-programs-file \
  --header 'authorization: Bearer <<<TOKEN>>>'
```

### 단 건 등록

```
curl --request POST \
  --url http://localhost:8090/admin/eco-programs \
  --header 'content-type: application/json' \
  --data '{"name":"내장산과 함께하는 즐거운 여행(숙박형)","theme":"자연생태체험","regionCode":192,"regionName":"전라북도 정읍시","description":"내장산 자연 속 트레킹과 함께 미션체험도 즐기시고 100년 전 초가마을에서 하룻밤을 보낼 수  있는 숙박형 프로그램입니다.","detail":"- 내장산 탐방안내소 관람 - 자연놀이 및 나뭇잎티셔츠 만들기 체험 - 숲 트레킹, 미션수행  - 초가집에서 보내는 밤   (여치집만들기 체험 등) - 강정 만들기 체험"}' \
  --header 'authorization: Bearer <<<TOKEN>>>'
```

### 단 건 수정

```
curl --request PATCH \
  --url http://localhost:8090/admin/eco-programs/1 \
  --header 'content-type: application/json' \
  --data '{"id": 1, "name":"관악산과 함께하는 즐거운 여행(숙박형)","theme":"자연생태체험","regionCode":192,"regionName":"전라북도 정읍시","description":"내장산 자연 속 트레킹과 함께 미션체험도 즐기시고 100년 전 초가마을에서 하룻밤을 보낼 수  있는 숙박형 프로그램입니다.","detail":"- 내장산 탐방안내소 관람 - 자연놀이 및 나뭇잎티셔츠 만들기 체험 - 숲 트레킹, 미션수행  - 초가집에서 보내는 밤   (여치집만들기 체험 등) - 강정 만들기 체험"}' \
  --header 'authorization: Bearer <<<TOKEN>>>'
```

### 단 건 조회

```
curl --request GET \
  --url http://localhost:8090/admin/eco-programs/1 \
  --header 'authorization: Bearer <<<TOKEN>>>'
```

### 지역코드로 프로그램 조회

```
curl --request GET \
  --url 'http://localhost:8090/admin/eco-programs?regionCode=192' \
  --header 'authorization: Bearer <<<TOKEN>>>'
```

### 지역이름으로 프로그램 조회

```
curl --request GET \
  --url http://localhost:8090/admin/eco-programs/by-region-name \
  --header 'content-type: application/json' \
  --data '{
    "region": "정읍"
}' \
  --header 'authorization: Bearer <<<TOKEN>>>'

```

### 소개내용으로 지역별 프로그램 카운트 조회

```
curl --request GET \
  --url http://localhost:8090/admin/eco-programs/by-desc-keyword \
  --header 'content-type: application/json' \
  --data '{
    "keyword": "내장산"
}' \
  --header 'authorization: Bearer <<<TOKEN>>>'

```

### 상세내용으로 키워드 출현 빈도 조회

```
curl --request GET \
  --url http://localhost:8090/admin/eco-programs/by-detail-keyword \
  --header 'content-type: application/json' \
  --data '{
    "keyword": "체험"
}' \
  --header 'authorization: Bearer <<<TOKEN>>>'

```



