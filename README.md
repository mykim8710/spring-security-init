# Springboot Spring Security init

## [Project Info]
- Springboot 2.7.7, Java 11
- Name(project name) : spring-security-init
- Language : Java
- Type : Gradle(Groovy)
- Packaging : Jar
- [Project Metadata]
  - Group: spring.security
  - Artifact: init
  - Package name: spring.security.init
- [Dependencies]
  - Spring Web
  - Spring Security
  - Spring Data JPA
  - Lombok
  - H2 Database
  - thymeleaf

## [Project package design]
```
└── src
    ├── main
    │   ├── java
    │   │     └── spring.security.init
    │   │            ├── SpringsecurityInitApplication(C)
    │   │            ├── api
    │   │            │    └── UserApiController(C)
    │   │            ├── dto
    │   │            │    └── ResponseUserSignDto(C)
    │   │            ├── entity
    │   │            │    ├── User(C)
    │   │            │    └── UserType(E)
    │   │            ├── global
    │   │            │    ├── init
    │   │            │    │    └── InitUserInsert(C)
    │   │            │    ├── result
    │   │            │    │    │── error
    │   │            │    │    │     │── BusinessException(C)
    │   │            │    │    │     │── ErrorCode(E)
    │   │            │    │    │     └── GlobalExceptionHandler(C)
    │   │            │    │    │── SuccessCode(E)
    │   │            │    │    └── CommonResult(C)
    │   │            │    │── config
    │   │            │    │    ├── security
    │   │            │    │    │       └── handler
    │   │            │    │    │              │── CustomAccessDeniedHandler(C)
    │   │            │    │    │              │── CustomAuthenticationFailureHandler(C)
    │   │            │    │    │              └── CustomAuthenticationSuccessHandler(C)
    │   │            │    │    │── CustomAuthenticationProvider(C)
    │   │            │    │    └── CustomUserDetailsService(C)
    │   │            ├── repository
    │   │            │    └── UserRespository(I)
    │   │            └── web
    │   │                 └── ViewController(C)
    │   │        
    │   └── resources
    │       ├── templates : thymeleaf template : html           
    │       └── application.yaml
```

## [내용]
- SecurityConfig
  - 기존에는 WebSecurityConfigurerAdapter 를 상속받아 설정을 오버라이딩 하는 방식
  - 스프링 버전이 업데이트 됨에 따라 상속 후 오버라이딩하는 방식이 아닌 필요한 것을 Bean으로 등록하는 방식
  ```
  // 기존 방식
  @EnableWebSecurity 
  public class SecurityConfig extends WebSecurityConfigurerAdapter { 
     
      // 인증을 무시할 경로들을 설정 >> static resource 보안설정
      @Override
      public void configure(WebSecurity web) {
          ....
      }
  
      // http 관련 인증 설정
      @Override
      protected void configure(HttpSecurity http) throws Exception {
          ....    
      }
      ....
  }
  
  // 새 방식
  @EnableWebSecurity
  public class SecurityConfig {
  
      // 인증을 무시할 경로들을 설정 >> static resource 보안설정
      @Bean
      public WebSecurityCustomizer webSecurityCustomizer() {
          return ....;
      }
  
      @Bean
      public SecurityFilterChain filterChainSession(HttpSecurity httpSecurity) throws Exception {
          .....

          return httpSecurity.build();
      }
  }
  ```
- in memory 방식
- session 방식
