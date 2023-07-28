package jpcompany.smartwire.web.member.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Configuration
@Slf4j
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize,postAuthorize 어노테이션 활성화
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/css/**", "/image/**", "/login", "/error/**",
                        "/join", "/email_verify/**", "/api/**", "/actuator/beans").permitAll()
                .anyRequest().hasAuthority("EMAIL_VERIFIED")
                .and()

                .formLogin()
                .usernameParameter("loginId")
                .passwordParameter("loginPassword")
                .loginPage("/login")  // 인증되지 않은 계정이 다른 URL 에 접근했을 때 '/login' 으로 Get 요청 (redirect)
                .loginProcessingUrl("/login") // '/login' POST 요청을 시큐리티가 낚아채서 대신 로그인을 진행
                .defaultSuccessUrl("/")
                .failureHandler(new CustomAuthenticationFailureHandler());  // 이메일 인증되지 않았으면 로그인 무효, 인증 페이지로 이동

        return http.build();
    }
}