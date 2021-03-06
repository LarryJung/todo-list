# 할 일 목록 웹 어플리케이션 만들기
---

## 문제 해결 전략

### 셀프 엔티티 연관관계 맵핑
- 최초 접근 방식은 `List<Task> masterTasks` 와 같이 Task가 직접 목록을 들고 있는 관계로 고려. 단 하나의 클래스로도 연관관계를 맵핑 할 수 있지만, Task 클래스 내부의 코드 복잡도 증가와 Json 맵핑 시 스택오버플로우 이슈에 자주 부딪히게 되어 다른 방법 모색.

- MasterTask, SubTask 간의 관계를 표현하는 Relation 클래스를 만들어 다대다 관계를 일대다 - 다대일 관계로 풀었으며, 
RelationRepository가 있음으로 인해서 참조관계의 탐색이 용이해짐.   
 
### 수정 기능 구현 전략
- jQuery 라이브러이인 x-editable 사용. 값의 수정 이벤트를 읽어 ajax로 요청 처리. 라이브러리의 ajax요청 포멧에 맞추어 Dto로 요청 바디 전달 
 
### 삭제 기능 구현 전략
- 연관관계의 주인(외래키 관리)은 MasterTask로 설정하였으며, 따라서 데이터 삭제 시 참조 무결성 관계에 따라 SubTask들을 먼저 삭제할 수 없음. 하위 할일 부터 삭제 가능
- 엔티티 삭제 시 영속성을 보장하는 CASCADE REMOVE로 설정
 
### 예외 처리 부문
- 예외 상황에 대해서 일관된 형태로 응답을 가능하게 할 목적으로 클래스 생성
- Advice를 통해 예외를 캐치하고 `ErrorMsg`오브젝트 형태로 일괄 변경

### 페이징 처리
- 할일, 완료 목록에 대한 테이블을 각각 보여주기 위해서 요청 파라미터로 출력 내용 기입
- 만들어진 Pageable 객체를 활용했으며 View에 필요한 파라미터는 재가공하여 응답


개발 환경
- Java1.8, Spring boot, Gradle

빌드 방법, 실행 방법
1. Git 저장소를 다운로드 혹은 clone한다.
2. `gradlew` 파일이 있는 경로에서 `./gradlew build` 로 빌드
3. `java -jar build/libs/todolist-0.0.1-SNAPSHOT.jar` 실행 후 
4. 브라우저에서 `localhost:8080`로 접속

5. DB url : `http://localhost:8080/h2-console`
6. JDBC : `jdbc:h2:mem:todolist` 

7. 세부 동작 방식은 페이지 내 가이드 있음