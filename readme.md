![casealot-logo](https://github.com/casealot/casealot-backend/assets/70744371/9f311cb1-78bd-4881-a35d-dae8a37e4dc8)

## 📢 프로젝트 설명
    CASE A LOT 쇼핑몰 개발

## ⚙ 개발 환경

    운영체제 :  Windows
    통합개발환경(IDE) : IntelliJ
    JDK 버전 : JDK 17
    데이터 베이스 : Mysql
    빌드 툴 : Gradle-7.6.1
    CI/CD : jenkins
    관리 툴 : GitHub
    웹 호스팅 : AWS(EC2, S3, RDS, ROUTE 53)

## 💻 기술 스택

### Backend
  <img src="https://img.shields.io/badge/Java-000000?style=flat-square&logo=OpenJDK&logoColor=#6DB33F"/></a>
  <img src="https://img.shields.io/badge/Spring Boot-000000?style=flat-square&logo=Spring Boot&logoColor=#6DB33F"/></a>
  <img src="https://img.shields.io/badge/Gradle-000000?style=flat-square&logo=Gradle&logoColor=#02303A"/></a>
  <img src="https://img.shields.io/badge/Spring Security-000000?style=flat-square&logo=Spring Security&logoColor=#6DB33F"/></a>
  <img src="https://img.shields.io/badge/Spring JPA-000000?style=flat-square&logo=Spring Jpa&logoColor=#6DB33F"/></a>
  <img src="https://img.shields.io/badge/Oauth 2.0-000000?style=flat-square&logo=Authy&logoColor=blue"/></a>
  <img src="https://img.shields.io/badge/JSON Web Tokens-000000?style=flat-square&logo=JSON Web Tokens&logoColor=purple"/></a>
### Database
  <img src="https://img.shields.io/badge/Mysql-000000?style=flat-square&logo=MySql&logoColor="/></a>
  <img src="https://img.shields.io/badge/H2-000000?style=flat-square&logo=H2&logoColor=#DC382D"/></a>
### DevOps
  <img src="https://img.shields.io/badge/AWS-000000?style=flat-square&logo=Amazon AWS&logoColor=#232F3E"/></a>
  <img src="https://img.shields.io/badge/Amazon EC2-000000?style=flat-square&logo=Amazon EC2&logoColor=#FF9900"/></a>
  <img src="https://img.shields.io/badge/Amazon RDS-000000?style=flat-square&logo=Amazon RDS&logoColor=#527FFF"/></a>
  <img src="https://img.shields.io/badge/Amazon S3-000000?style=flat-square&logo=Amazon S3&logoColor=#569A31"/></a>
  <img src="https://img.shields.io/badge/Docker-000000?style=flat-square&logo=Docker&logoColor=#2496ED"/></a>
  <img src="https://img.shields.io/badge/Jenkins-000000?style=flat-square&logo=Jenkins&logoColor=#D24939"/></a>
### Collaboration tool
  <img src="https://img.shields.io/badge/Slack-000000?style=flat-square&logo=Slack&logoColor=red"/></a>
  <img src="https://img.shields.io/badge/Notion-000000?style=flat-square&logo=Notion&logoColor=#000000"/></a>
  <img src="https://img.shields.io/badge/Discord-000000?style=flat-square&logo=Discord&logoColor="/></a>


## 👳‍♂️ ADMIN

    오늘의 할일을 통해 오늘의 주문, 교환, 취소, 문의 개수를 파악할 수 있습니다.
    오늘의 할일을 통해 주문, 교환, 취소, 문의 내역의 목록을 최신순으로 확인할 수 있습니다.
    주간 주문건 수, 매출액 , 가입자 수, 문의 내역 수를 일자별로 조회 할 수 있습니다.
    주간 매출 현황과 주문건수를 각각 그래프로 확인할 수 있습니다.

### Product

    관리자는 관리자 화면에서 상품 등록, 수정, 삭제가 가능합니다.
    관리자는 상품 사진을 등록 할 수 있습니다. (Amazon S3)
    상품 등록시에는 이름, 색상, 가격, 카테고리를 작성해야합니다.
    상품 삭제 시 해당 상품의 리뷰와 리뷰 댓글은 모두 삭제됩니다.

### QnA

    관리자는 사용자의 문의에 답변을 작성할 수 있습니다.
    관리자는 답변을 수정, 삭제 할 수 있습니다.
    관리자는 관리자 화면에서 답변이 달리지 않은 문의목록을 조회 할 수 있습니다.

### Notice

    관리자는 공지사항을 작성, 수정, 삭제 할 수 있습니다.
    공지 작성에는 제목과 내용이 작성되어야합니다.

## 🙍‍♂️ USER

    사용자는 회원가입을 할 수 있습니다.
    회원가입 시에는 아이디, 비밀번호, 이메일 주소, 이름, 전화번호, 주소를 입력해야하며 아이디는 고유해야 합니다.
    사용자는 회원가입시 입력한 아이디, 비밀번호로 로그인을 할 수 있습니다.
    사용자는 Social Login 을 진행할 수 있습니다. (OAuth2 : 카카오, 네이버)
    로그인이 완료되면 JSON Web Token(Access Token & Refresh Token) 이 발급됩니다.
    사용자는 회원정보, 장바구니, 위시리스트, 배송정보를 조회 할 수 있습니다.
    사용자는 회원 정보를 수정할 수 있으며, 프로필 사진을 등록할 수 있습니다.
    
### Token

    사용자는 로그인시 Access Token 과 Refresh Token을 부여받습니다.
    Access Token 시간이 만료되면 Refresh Token 을 통해 Access Token을 갱신받습니다.
    사용자의 Access Token 은 로그아웃시 BlackList Token 에 등록됩니다.
    BlackList Token 에 등록된 토큰은 다시 사용할 수 없습니다.

### Product

    사용자는 상품목록을 조회 할 수 있습니다.
    상품목록이 많을 수 있으므로 페이징 처리를 합니다.
    사용자는 상품을 카테고리별로 조회 할 수 있습니다. (NEW, BEST, CAP, TOP ...)
    사용자는 상품을 색상, 가격으로 필터링 할 수 있습니다.
    사용자는 상품을 찜한순, 가격순, 할인율순, 판매량순, 평점순, 리뷰순으로 정렬 할 수 있습니다.
    사용자는 상품을 검색 할 수 있으며, 상품명 자동완성이 가능합니다.
    사용자는 특정 상품의 정보를 자세히 조회 할 수 있습니다.
    특정 상품의 정보에는 상품명, 가격, 할인율, 별점평균, 위시리스트 등록횟수, 상품사진, 상품리뷰가 있습니다.

### REVIEW

    상품을 구매한 사용자에 한해 리뷰를 작성 할 수 있고 리뷰 작성은 한번만 가능합니다.
    리뷰에는 내용과 별점을 작성하며, 본인의 리뷰를 수정, 삭제할 수 있습니다.
    리뷰에 대한 댓글을 작성할 수 있으며, 본인의 댓글을 수정, 삭제할 수 있습니다.

### CART

    사용자는 특정 상품을 원하는 수량만큼 장바구니에 추가 할 수 있습니다.
    장바구니에서 상품의 수량을 증가 또는 감소 할 수 있으며 삭제가 가능합니다.
    특정 상품의 썸네일, 상품명, 가격, 할인 가격, 수량, 총 금액을 조회 할 수 있습니다.
    사용자는 장바구니에 담긴 상품들을 주문할 수 있습니다.

### WISHLIST

    사용자는 특정 상품을 위시리스트에 등록 할 수 있습니다.
    특정 상품이 이미 위시리스트에 등록되어 있으면 위시리스트에 등록 할 수 없습니다.
    사용자는 위시리스트를 삭제 할 수 있습니다.

### Q&A

    사용자는 문의글을 등록, 조회, 수정, 삭제 할 수 있습니다.
    본인이 작성한 글의 경우 수정, 삭제가 가능합니다  
    등록, 수정시에는 제목, 내용 입력이 가능합니다.
    Q&A의 게시글이 많을 수 있으므로 페이징 처리를 합니다.

### NOTICE

    사용자는 공지사항 목록을 조회 할 수 있습니다.
    공지사항의 게시글이 많을 수 있으므로 페이징 처리를 통해 나타냅니다.
    사용자는 특정 공지사항을 조회 할 수 있습니다.
    사용자는 특정 공지사항에 댓글을 작성 할 수 있습니다.
    사용자는 본인이 작성한 댓글을 수정, 삭제 할 수 있습니다.

### ORDER

    사용자는 상품을 바로구매 또는 장바구니에 담긴 상품들을 주문 할 수 있습니다.
    사용자는 본인의 주문을 취소할 수 있습니다.
    주문 생성 시 랜덤 주문번호가 생성되고 결제단계로 넘어갑니다.

### PAYMENT

    주문 생성 시 얻은 정보와 결제 정보를 비교하여 검증을 진행합니다.
    검증이 완료되면 카드정보를 입력하고 결제를 진행합니다.
    결제가 완료되면 랜덤 송장번호가 생성되고 배송단계로 넘어갑니다.
    사용자는 결제가 완료된 본인의 주문내역을 조회 할 수 있습니다.
    사용자는 본인의 결제를 취소할 수 있습니다.

### DELIVERY

    본인의 주문건에 해당하는 송장번호로 현재 배송 상태를 조회 할 수 있습니다.
    실제 송장번호를 사용할 수 없어 임의의 송장번호를 생성하고, 스케쥴링을 통해 배송상태를 변경시킵니다.
    스케쥴링은 3일단위로 배송전, 배송중, 배송완료 순으로 상태가 변경됩니다.

## 🖨 Backend Architecture

![image](https://github.com/casealot/casealot-backend/assets/70744371/a481b40f-8d25-4802-b333-39d823acba82)

## 📜 [SWAGGER](http://43.201.170.8:8000/swagger-ui/index.html#/)

![img.png](https://github.com/casealot/casealot-backend/assets/101981639/d09d1efd-0036-46c8-beb2-fe919a45b59e)

## 📃 ERD

![image](https://github.com/casealot/casealot-backend/assets/101981639/1623731d-fe66-47cd-a88c-0f43718e22de)


