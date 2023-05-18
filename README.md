# 게시판 포트폴리오
* * *
## 1일차
* 스프링 부트 기본 설정
* 타임리프 설정


## 2일차
* 타임리프 레이아웃 템플릿 완성
* 스프링 시큐리티 설정
    - 회원가입 엔티티, 레포지토리

## 3일차
* 스프링 시큐리티 설정
    - 로그인 양식
    - UserDetails, UserDetailsService 인터페이스 구현 클래스
    - Spring Data JPA + Spring Security : 수정자(AwareAuditor 인터페이스 구현체)
    - Spring Security의 회원 정보 조회 방법
      - 요청 처리 메서드에 주입
        - Principal principal - String getUsername() : 아이디
        - @AuthenticationPrincipal UserDetails 구현 클래스의 객체
      
      - 직접 회원정보 가져오기
        - SecurityContextHolder
          - getContext.getAuthentication()
          - getPrincipal() : 비회원 (String) - anonymousUser / 회원 - UserDetails 구현 객체
           


* 기본 에러 응답 코드 처리
    - 템플릿 경로 : /error/응답코드.html
      - timestamp : 오류 발생 시각
      - status : HTTP 상태 코드
      - error : 오류 발생 원인
      - exception : 예외 객체
      - errors : Errors 객체
      - trace : printStackTrace()
      - path : 오류의 유입 URL

  
* 공통 오류 페이지
    - @ExceptionHandler, @ControllerAdvice, @RestControllerAdvice


## 4일차
* 공통 오류 페이지 처리
* 관리자 페이지
    - 사이트 설정
    - 게시판 설정
