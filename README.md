# 무신사 백엔드 과제

## 구현범위
### 사용자 API : [CustomerController.java](src%2Fmain%2Fjava%2Fcom%2Fmusinsa%2Fcontroller%2Fcustomer%2FCustomerController.java)
1. 카테고리별 최저가
    - 카테고리별 최저 가격인 브랜드, 가격과 총액을 제공
    - 가격이 동일한 케이스가 있는 경우 브랜드명 역순으로 우선순위
2. 가장 저렴한 브랜드 카테고리
    - 전체 상품 카테고리 상품중 단일브랜드로 최저가격인 브랜드와 총액
    - 샘플데이터는 카테고리, 브랜드에 상품이 1개씩만 있지만 카테고리, 브랜드에 상품이 여러개 있을 경우 고려하여 작업
3. 카테고리 최저가, 최고가 브랜드
    - 특정 카테고리에서 최저가격 브랜드와 최고가격 브랜드

### 운영자 API : [AdminController.java](src%2Fmain%2Fjava%2Fcom%2Fmusinsa%2Fcontroller%2Fadmin%2FAdminController.java)
1. 브랜드 관리(등록, 수정, 삭제)
    - 브랜드의 삭제
        - 대상 브랜드가 이미 판매 되거나 참조 될수 있으므로 soft delete로 구현
        - 브랜드의 수정은 현재 브랜드의 정보가 많지 않아 비활성화만 가능 합니다.
2. 상품 관리(등록, 수정, 삭제)
    - 상품은 기존 브랜드를 사용하거나, 신규 브랜드인 경우 브랜드를 먼저 생성해야 등록 할 수 있습니다.
    - 삭제 대상 상품이 이미 판매 되거나 참조 될수 있으므로 soft delete로 구현

### 공통사항
1. 사용자 API 영역은 트래픽이 높을 수 있으므로 caffeine cache 적용
    - 갱신시간 10분
    - 브랜드, 상품의 변경이 있는 경우 생성되었던 캐시는 삭제되고 새로운 요청에서 변경된 내용이 새롭게 반영됩니다.
    - 갱신 주기나, 정보 변경에 의한 삭제 정책은 운영을 하면서 적절한 값을 설정 하는게 맞는것 같습니다.
2. 조회가 빈번 할 수 있는 사용자 API의 경우는 slave db를 조회 하도록 구성
   - 설정만 나눠져 있고, 동일한 h2 db를 사용하고 있음을 양해 부탁 드립니다.

## 기술 스택
- Java 17, Spring Boot 3.3.3
- Spring Data JPA, H2
- JUnit 5, Mockito

## 코드 빌드, 실행
```shell
./gradlew clean build
./gradlew bootRun
```

## 테스트 방법
```shell
./gradlew test
```

## 실행 방법
- front 페이지 구현 : 시간되면 하자.. ㅠㅠ

## API 문서
- http://localhost/swagger-ui/index.html

# DB
- schema : [db-schema.sql](src%2Fmain%2Fresources%2Fdb-schema.sql)
- data : [db-data.sql](src%2Fmain%2Fresources%2Fdb-data.sql)
