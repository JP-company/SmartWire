package jpcompany.smartwire.security.session.config;

import jpcompany.smartwire.security.session.handler.CustomAuthenticationFailureHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



//@Configuration
//@EnableWebSecurity  // 스프링 시큐리티 필터가 스프링 필터체인에 등록
@Slf4j
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)  // secured 어노테이션 활성화, preAuthorize,postAuthorize 어노테이션 활성화
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // 아예 보안 필터를 거치지 않음
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

//    @Order(1)
//    @Bean
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .authorizeRequests()
                .antMatchers("/login", "/error/**",
                        "/join", "/email_verify/**", "api/login", "/actuator/beans").permitAll()
                .anyRequest().authenticated()
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