package spring.security.init.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import spring.security.init.security.handler.CustomAccessDeniedHandler;
import spring.security.init.security.handler.CustomAuthenticationFailureHandler;
import spring.security.init.security.handler.CustomAuthenticationSuccessHandler;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 활성화 => 기본 스프링 필터체인에 등록
public class SecurityConfig {
    // user password encoder 빈등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 인증을 무시할 경로들을 설정 >> static resource 보안설정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers("/css/**", "/js/**");
    }

    /**
     * in memory 방식
      */
//    // UserDetails 객체등록
//    @Bean
//    public UserDetailsService userDetailsServiceInMemory() {
//        PasswordEncoder passwordEncoder = passwordEncoder();
//        System.out.println("passwordEncoder.encode(\"1111\") = " + passwordEncoder.encode("1111"));
//
//        UserDetails user = User.builder()
//                                    .username("user")   // id
//                                    .password(passwordEncoder.encode("1111")) // password
//                                    .roles("USER")
//                                    .build();
//        System.out.println("user.getPassword() = " + user.getPassword());
//
//        UserDetails admin = User.builder()
//                                    .username("admin")
//                                    .password(passwordEncoder.encode("1111"))
//                                    .roles("USER", "MANAGER", "ADMIN")
//                                    .build();
//        System.out.println("admin.getPassword() = " + admin.getPassword());
//
//        return new InMemoryUserDetailsManager(user, manager, admin);
//    }
//
//    // http관련 인증 설정
//    @Bean
//    public SecurityFilterChain filterChainInMemory(HttpSecurity httpSecurity) throws Exception {
//        // csrf token disable
//        httpSecurity
//                .csrf().disable();
//
//        // 권한 별 url 접근설정 : in memory test
//        httpSecurity
//                .authorizeRequests()
//                .antMatchers("/").anonymous()
//                .antMatchers("/admin").hasRole("ADMIN")
//                .antMatchers("/user").hasAnyRole("USER", "ADMIN")
//                .anyRequest().authenticated();
//
//        // 로그인 설정
//        httpSecurity
//                .formLogin()
//                .defaultSuccessUrl("/home");
//        // Spring Security는 인증이 필요한 페이지에 사용자가 접근하면, 로그인 페이지로 이동
//        // 로그인이 성공하면 사용자가 처음에 접근했던 페이지로 리다이렉트
//        // 사용자가 로그인하기 전에 방문했던 페이지가 아닌, 다른 페이지를 원한다면 defaultSuccessUrl을 사용
//
//
//        return httpSecurity.build();
//    }


    /**
     * Session 방식
     */
    @Bean
    public SecurityFilterChain filterChainSession(HttpSecurity httpSecurity) throws Exception {
        // csrf token disable
        httpSecurity
                .csrf().disable();

        // 권한 별 url 접근설정
        httpSecurity
                .authorizeRequests()
                .antMatchers("/", "/sign-in").anonymous()
                .antMatchers("/admin").hasAnyAuthority("ADMIN")
                .antMatchers("/user").hasAnyAuthority("USER", "ADMIN")
                .anyRequest().authenticated();

        // 로그인 설정
        httpSecurity
                .formLogin()
                .loginPage("/")
                .loginProcessingUrl("/sign-in")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler());

        // Exception 설정
        httpSecurity
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler()); // 접근권한이 없을때 handler

        // 로그아웃 설정
        httpSecurity
                .logout()
                .logoutUrl("/sign-out")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)	                 // 로그아웃 후 세션 전체 삭제 여부
                .deleteCookies("JSESSIONID"); // 로그아웃 후 cookie 삭제


        return httpSecurity.build();
    }


    // CustomAuthenticationProvider가 자동으로 등록되어있음
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }









}
