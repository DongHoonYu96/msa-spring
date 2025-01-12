# Spring Security 인증 과정의 전체 흐름

1. **사용자 로그인 요청**
    - 사용자의 로그인 정보(이메일/비밀번호) 서버 전송

2. **AuthenticationFilter**
   ```java
   private Filter getAuthenticationFilter(AuthenticationManager authenticationManager) {
       AuthenticationFilter authenticationFilter = new AuthenticationFilter();
       authenticationFilter.setAuthenticationManager(authenticationManager);
       return authenticationFilter;
   }
   ```
    - HTTP 요청 가로채기 및 인증 처리 시작
    - `AuthenticationManager`로의 인증 처리 위임

3. **AuthenticationManager**
   ```java
   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) {
       return authConfig.getAuthenticationManager();
   }
   ```
    - 실제 인증 처리 매니저
    - `AuthenticationProvider`를 통한 인증 수행

4. **AuthenticationProvider**
   ```java
   @Bean
   public DaoAuthenticationProvider authenticationProvider() {
       DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       authProvider.setUserDetailsService(userService);
       authProvider.setPasswordEncoder(bCryptPasswordEncoder);
       return authProvider;
   }
   ```
    - `UserDetailsService`를 통한 사용자 정보 조회
    - `PasswordEncoder`를 통한 비밀번호 검증

5. **UserDetailsService (UserService)**
   ```java
   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       UserEntity userEntity = userRepository.findByEmail(username)
           .orElseThrow(() -> new UsernameNotFoundException(username));
       
       return new User(userEntity.getEmail(), 
                      userEntity.getEncryptedPwd(), 
                      true, true, true, true, 
                      new ArrayList<>());
   }
   ```
    - DB로부터의 사용자 정보 조회
    - Spring Security 사용 가능한 UserDetails 객체로의 변환

6. **인증 결과 처리**
    - 인증 성공: JWT 토큰 생성 등 후속 처리
    - 인증 실패: 예외 처리

이러한 과정을 통한 사용자 인증 수행과 SecurityFilterChain에서의 필터 순서 및 보안 설정 관리:

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) {
    http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/**").permitAll()
            )
            .addFilter(getAuthenticationFilter(authenticationManager));

    return http.build();
}
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