-- 기존 Connection을 선택했는데도 오라클 데이터베이스와 연결이 되지 않는 경우
-- Data Source Explorer에서 해당 Connection을 선택한 후 우클릭하여 connect 버튼을 눌러 활성화시킨다.

create table member(
	memberid varchar2(50) primary key,
	name varchar2(50) not null,
	password varchar2(10) not null,
	regdate date not null
);

insert into member values('aaa','aaa','aaa', sysdate);

-- Ch22
-- > 게시글 데이터를 저장하기 위해 2개의 테이블 생성
-- 	1. article: 게시글 작성자, 제목, 작성일시, 수정일시, 조회수 데이터
-- 	2. article_content: 내용 데이터
-- > 내용과 그 외 정보를 따로 저장하는 이유
-- 	서로 사용빈도가 다르기 때문에
--	article은 게시글 목록 조회할때도 사용되지만 article_content는 게시글볼 때만 사용된다.

create table article(
	article_no int primary key,
	writer_id varchar2(50) not null,
	writer_name varchar2(50) not null,
	title varchar2(255) not null,
	regdate date not null,
	moddate date not null,
	read_cnt int
);

-- article_no에 적용할 시퀀스 만들기
create sequence article_no_seq
increment by 1 -- 증감 숫자
start with 1 -- 시작 숫자 1
nomaxvalue;

-- 만약 sequence 잘못 만들었을 경우: drop


create table article_content(
	article_no int primary key,
	content varchar2(4000)
);
