# khumu-community 설계

커뮤니티라는 쿠뮤의 중심이 되는 서비스를 만들고자한다.

기존에는 Django를 이용해서 `khumu-command-center` 라는 이름으로 커뮤니티 및 경희대 관련 혹은 계정 관련 기능들을 수행했지만 이제는 Java Spring Boot를 바탕으로 이용하려한다.

# khumu-community의 주요 기능

- 유저에 대한 CRUD
- ✅ 1️⃣ 유저에 대한 차단 기능 (게시글 작성자 차단하기)
- 게시판에 대한 CRUD
- 게시판에 대한 Follow / Unfollow
- ✅ 1️⃣ 게시글에 대한 CRUD
  - ✅ 1️⃣ 내가 작성한, 댓글 단, 좋아요한 등 각각의 다양한 기준으로 게시글을 조회하기
  - ✅ 1️⃣ comment 서버에서 comment 개수 가져오기
  - ✅ 1️⃣ alimi 서버에서 게시글에 대한 알림 구독 정보 가져오기
- 1️⃣ 게시글/게시판에 대한 검색 기능
- 1️⃣ 게시글 작성 시각 표현
- ✅ 1️⃣ 게시글에 대한 Like / Unlike
- ✅ 1️⃣ 게시글에 대한 Bookmark / Unbookomark
- 1️⃣ 다양한 엔티티에 대한 신고 기능
- 학사 일정에 대한 Read - 파이썬으로 할 지 고민
    - 근데 파이썬으로 하는 것 자체가 불필요한 microservice화가 될 것 같다.
- 앱에 대한 피드백 Create
- 학교 강의, 학과 정보 크롤링, Read

일단은 1️⃣로 표시한 유저, 게시글 관련 기능들을 개발하고

게시판에 대한 기능도 개발하고

나머지 학사일정이나 학교 관련 기능들은 khumu-command-center에 있던 코드로 버텨보는 게 좋을 것 같다.

# 저장소 설계

- 기본적으로는 **RDB + JPA**의 조합
- **Redis cache를 쓰기 좋은 부분이 있는가?**
    - 내가 팔로우 중인 게시판 목록
        - 간단하게 팔로우 작업과 Synchronous하게 작업할 수 있다.
    - 내 피드에 보여질 최근 게시글들
        - 누군가가 글을 썼다는 이벤트를 구독 후 처리
            - 해당 글의 게시판을 구독하는 유저들에 대한 피드 게시글 ID 캐시에 해당 글의 ID를 추가한다.
        - 내가 게시판을 팔로우 했을 때
            - 해당 게시판의 최근 글들 중 N개 정도의 ID를 자신의 피드 게시글 ID 캐시에 추가한다.

      내 피드에 보여질 최근 게시글 ID들을 Redis에 캐시하면 좋은 점은? (뭐가 있을까..?)
      - 보고자하는 ID를 미리 캐시해놓으면 JOIN이나 다른 테이블 조회 없이 PK에 대한 in-query만으로 조회할 수 있다.
      - ❓ 근데 실제로 체감될만한 효과가 있을 지는 모르겠음.

    - General한 내용들
        - 핫 게시글 N개 ← is_hot에 대한 index는 만들기 싫기 때문에 핫 게시글에 대한 조회는 Redis에 저장하면 편하고 빠르긴 할 듯. 근데 체감될 정도일 지는 잘 모르겠음.
- DynamoDB를 쓰기 좋은 부분이 있는가?
    - 투 머치인 듯.

# 이벤트 설계

- 유저 생성
    - slack 알림
    - alimi가 알림 설정을 만들기 위함
- 유저 삭제
    - slack 알림
- 유저 차단
- 게시글 생성
    - slack 알림
    - 댓글 서버가 게시글 작성자 정보를 기록하기 위함.
    - 게시글의 게시판을 Follow 중인 user들의 Feed Article ID list를 갱신하기 위함.
- 게시글 좋아요
    - 일정 개수가 넘으면 Hot 게시글로 선정하기 위함.
    - ~~일정 개수가 넘으면 작성자에게 알림을 보내기 위함.~~ ← 별로 필요 없을 듯
- 게시글 신고
- 핫 게시글 선정
    - 작성자에게 알림을 보내기 위함.
    - 새로운 핫 게시글 선정 관련 알림을 보내기 위함. ← 이것도 별로 필요 없을 것 같긴 함.
- ~~댓글, 대댓글 생성~~ → 이건 댓글 서버가 알아서 하겠다.

# 애플리케이션 스트럭쳐

- 먼저 Context를 나누는 건 투머치같다. 몇 가지 사이드 프로젝트들의 구조를 참고했지만 Context를 나눠서 장점을 잘 이끌어내는 케이스는 드물었던 것 같다.
- 헥사거널은 Application(내부)와 Infrastructure(외부)로 나뉜다.
    - Inbound Adapter는 Inbound Port를 호출함으로써 Inbound port가 실제 외부에서 들어오는 input을 잘 받아들이고 동작한 뒤 Inbound port가 응답하는 output을 실제 외부가 잘 이해할 수 있게끔 해준다.
    - 애플리케이션(내부)는 자신이 호출할 Outbound port를 정의하고 Outbound port를 호출한다. Outbound Adapter는 내부가 정의한 Outbound port를 구현함으로써 내부가 정의한 방식으로 실제 외부에 작업할 수 있도록 해준다.

```console
├── config   // config 관련 Class들 정의
│
├── infra   // 외부 영역
│   ├── controller   // 게시글, 게시판, 유저에 대한 Controller
│   │   db    // JPA를 이용해 application.port.repository 구현
│   └── cache   // cache 관련 내용
│
├── application   // 내부 영역
│   ├── execption   // 내부가 catch하거나 throw해줄 Exception
│   │   domain   // 도메인 객체
│   │   vo    // VO로 쓸만한 거 있으면 정의
│   │   dto   // 내부가 인풋으로 받거나 아웃풋으로 떨궈줄 DTO
│   │   port
│   │   ├── in
│   │   │   └── service // inbound port로서 service를 정의
│   │   └── out
│   │       ├── repository
│   │       │   cache
│   │       └── messaging`
│   └── mapper
│
└── common
    └── util
```