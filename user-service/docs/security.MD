# Spring Security 인증 과정의 이해

## 인증 처리 순서

1. **attemptAuthentication (최초 진입점)**
   - 클라이언트의 JSON 요청을 RequestLogin 객체로 변환
   - UsernamePasswordAuthenticationToken 생성
   ```java
   @Override
   public Authentication attemptAuthentication(...) {
       RequestLogin creds = new ObjectMapper()
           .readValue(request.getInputStream(), RequestLogin.class);
           
       return getAuthenticationManager().authenticate(
           new UsernamePasswordAuthenticationToken(
               creds.getEmail(),
               creds.getPassword(),
               new ArrayList<>()
           )
       );
   }
   ```

2. **AuthenticationManager**
   - AuthenticationProvider 선택
   - 인증 처리 위임

3. **AuthenticationProvider**
   - UserDetailsService 호출
   - 비밀번호 검증

4. **UserDetailsService**
   - DB에서 사용자 정보 조회
   - UserDetails 객체로 변환

5. **인증 결과 처리**
   - 성공: JWT 토큰 생성
   - 실패: 예외 처리

## 핵심 컴포넌트 흐름
```
JSON 요청 → AuthenticationFilter(attemptAuthentication) 
→ UsernamePasswordAuthenticationToken 생성 
→ AuthenticationManager 
→ AuthenticationProvider 
→ UserDetailsService 
→ 결과 처리
```

# Spring Security 인증 URL 설정 방식 변화

## Spring Security 5.7 이전
```java
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/login").permitAll()
            .anyRequest().authenticated();
    }
}
```
- WebSecurityConfigurerAdapter가 기본 로그인 URL(/login) 자동 처리
- configure 메서드 오버라이딩으로 설정
- 암묵적인 URL 처리 방식

## Spring Security 5.7 이후
```java
@EnableWebSecurity
public class WebSecurity {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/login").permitAll()
            .anyRequest().authenticated()
        );
        return http.build();
    }

    private Filter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setFilterProcessesUrl("/login");  // URL 명시적 지정 필요
        return authenticationFilter;
    }
}
```
- WebSecurityConfigurerAdapter 제거
- SecurityFilterChain Bean 등록 방식으로 변경
- 명시적 URL 설정 필요
- 컴포넌트 기반의 설정으로 전환

# 요약
## 핵심 컴포넌트
1. **attemptAuthentication**
   - 로그인 요청을 가로채는 첫 번째 진입점
   - 사용자가 제출한 아이디/비밀번호를 AuthenticationToken으로 변환

2. **AuthenticationManager**
   - 인증을 총괄하는 관리자
   - 적절한 AuthenticationProvider 선택

3. **AuthenticationProvider**
   - 실제 인증 로직 수행
   - UserDetailsService로 사용자 조회
   - PasswordEncoder로 비밀번호 검증

4. **UserDetailsService**
   - DB에서 사용자 정보 조회
   - Spring Security가 이해할 수 있는 UserDetails 객체로 변환

## 인증 처리 순서
1. 사용자 로그인 요청 (`/login`)
2. AuthenticationFilter가 요청 가로챔
3. AuthenticationManager에게 인증 위임
4. AuthenticationProvider가 실제 인증 수행
5. UserDetailsService로 사용자 정보 조회
6. 인증 결과에 따른 처리 (JWT 토큰 생성 또는 에러 반환)

## 버전별 차이점
- 5.7 이전: WebSecurityConfigurerAdapter로 암묵적 설정
- 5.7 이후: SecurityFilterChain으로 명시적 설정

## 설정 코드 작성 순서
1. SecurityFilterChain 구성
2. AuthenticationManager Bean 등록
3. AuthenticationProvider 설정
4. UserDetailsService 구현
5. 인증 성공/실패 핸들러 구현