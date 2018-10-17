# point-service
전체/개인에 대한 포인트 부여 히스토리와 개인별 누적 포인트를 관리

## Requirements
* 이 서비스를 위한 SQL(MySQL 5.7) 스키마를 설계해 주세요. 테이블과 인덱스에 대한 DDL이 필요합니다.
* 아래에 대한 pseudo code를 작성해 주세요.
  * 포인트 적립 API
  * 포인트 조회 API
  
### Remarks
* 포인트 증감이 있을 때마다 이력이 남아야 합니다.
* 사용자마다 현재 시점의 포인트 총점을 조회하거나 계산할 수 있어야 합니다.
* 포인트 부여 API 구현에 필요한 SQL 수행 시, 전체 테이블 스캔이 일어나지 않는 인덱스가 필요합니다.
* 리뷰를 작성했다가 삭제하면 해당 리뷰로 부여한 내용 점수와 보너스 점수는 회수합니다.
* 리뷰를 수정하면 수정한 내용에 맞는 내용 점수를 계산하여 점수를 부여하거나 회수합니다.
    * 글만 작성한 리뷰에 사진을 추가하면 1점을 부여합니다.
    * 글과 사진이 있는 리뷰에서 사진을 모두 삭제하면 1점을 회수합니다.
* 사용자 입장에서 본 '첫 리뷰'일 때 보너스 점수를 부여합니다.
    * 어떤 장소에 사용자 A가 리뷰를 남겼다가 삭제하고, 삭제된 이후 사용자 B가 리뷰를 남기면 사용자 B에게 보너스 점수를 부여합니다.
    * 어떤 장소에 사용자 A가 리뷰를 남겼다가 삭제하는데, 삭제되기 이전 사용자 B가 리뷰를 남기면 사용자 B에게 보너스 점수를 부여하지 않습니다.
* 한 사용자는 장소마다 리뷰를 1개만 작성할 수 있고, 리뷰는 수정 또는 삭제할 수 있습니다. 리뷰 작성 보상 점수는 아래와 같습니다.
    * 내용 점수
        * 1자 이상 텍스트 작성: 1점
        * 1장 이상 사진 첨부: 1점
    * 보너스 점수
        * 특정 장소에 첫 리뷰 작성: 1점
    
## 문제 해결
* 사용자마다 현재 시점의 포인트 총점을 조회할 수 있어야 한다.
    1. 사용자의 모든 Point 기록을 더해 총점을 구한다.
        * 사용자가 요구할 때마다 총합을 구한다면, DB에 부하를 줄 수 있다.
    2. 사용자 테이블에 현재 포인트 총점을 넣는다.
        * 사용자가 요구할 때마다 해당 총점을 주면 되기 떄문에 부하를 줄일 수 있다.
* 포인트는 리뷰만을 위한 테이블이 아니다.
    * type, action을 통해 포인트를 분류할 수 있다.
    * 해당 문제에서는 리뷰 관련 포인트를 다루고 있다. 즉, type에 "REVIEW"를 넣어 분류 가능하다.
* 포인트 API 구현에 필요한 SQL 수행 시, 전체 테이블 스캔이 일어나지 않는 인덱스가 필요하다.
    * 리뷰 작성
        * 해당 장소의 리뷰가 존재하는지 확인한다.
            * Review 테이블의 placeId 컬럼에 인덱스 추가
    * 리뷰 수정, 삭제
        * 해당 리뷰의 포인트를 계산한다.
            * Point 테이블의 type, relatedId 컬럼에 복합 인덱스 추가
    * 포인트 조회
        * 해당 회원의 포인트 이력을 조회한다.
            * Point 테이블의 userId 컬럼에 인덱스 추가
            
## DDL
```
create table USER (
    ID char(36),
    NAME varchar(255) not null,
    EMAIL varchar(100),
    PASSWORD varchar(100) not null,
    POINT int default 0,
    constraint USER_PK primary key (ID)
);

create table PLACE (
    ID char(36),
    NAME varchar(255) not null,
    constraint PLACE_PK primary key (ID)
);

create table REVIEW (
    ID char(36),
    USER_ID char(36) not null,
    PLACE_ID char(36) not null,
    CONTENT varchar(1000) not null,
    DELETED BOOLEAN default false,
    constraint REVIEW_PK primary key (ID),
    constraint REVIEW_FK_USER foreign key (USER_ID) references USER(ID),
    constraint REVIEW_FK_PLACE foreign key (PLACE_ID) references PLACE(ID)
);

create index REVIEW_INDEX_USER on REVIEW(USER_ID);
create index REVIEW_INDEX_PLACE on REVIEW(PLACE_ID);

create table REVIEW_PHOTO (
    ID char(36),
    REVIEW_ID char(36) not null,
    URL varchar(255) not null,
    constraint REVIEW_PHOTO_PK primary key (ID),
    constraint REVIEW_PHOTO_FK foreign key (REVIEW_ID) references REVIEW(ID)
);

create index REVIEW_PHOTO_INDEX_REVIEW on REVIEW_PHOTO(REVIEW_ID);

create table POINT (
    ID char(36),
    USER_ID char(36) not null,
    CONTENT varchar(255) not null,
    POINT int,
    TYPE varchar(50),
    RELATED_ID char(36),
    ACTION varchar(50),
    constraint POINT_PK primary key (ID),
    constraint POINT_FK foreign key (USER_ID) references USER(ID)
);

create index POINT_INDEX_USER on POINT(USER_ID);
create index POINT_INDEX_TYPE_AND_RELATED on POINT(TYPE, RELATED_ID);
```