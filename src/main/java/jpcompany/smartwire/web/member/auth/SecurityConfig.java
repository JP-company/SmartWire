package jpcompany.smartwire.web.member.auth;

import jpcompany.smartwire.web.member.auth.jwt.JwtAuthenticationFilter;
import jpcompany.smartwire.web.member.auth.jwt.JwtAuthorizationFilter;
import jpcompany.smartwire.web.member.auth.session.CustomAuthenticationFailureHandler;
import jpcompany.smartwire.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@Slf4j
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize,postAuthorize 어노테이션 활성화
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final MemberRepository memberRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.authorizeRequests()
                    .antMatchers("/css/**", "/image/**", "/login", "/error/**",
                            "/join", "/email_verify/**", "api/login", "/actuator/beans").permitAll()
                    .anyRequest().hasAuthority("EMAIL_VERIFIED")
                    .and()

                .formLogin()
                    .usernameParameter("loginId")
                    .passwordParameter("loginPassword")
                    .loginPage("/login")  // 인증되지 않은 계정이 다른 URL 에 접근했을 때 '/login' 으로 Get 요청 (redirect)
                    .loginProcessingUrl("/login") // '/login' POST 요청을 시큐리티가 낚아채서 대신 로그인을 진행
                    .defaultSuccessUrl("/")
                    .failureHandler(new CustomAuthenticationFailureHandler())  // 이메일 인증되지 않았으면 로그인 무효, 인증 페이지로 이동
                    .and()
                .apply(new MyCustomDsl());

        return http.build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            log.info("api 로그인 설정");
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager);
            JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager, memberRepository);
            http
                    .antMatcher("/api/**")
                    .addFilter(corsConfig.corsFilter())  // @CrossOrigin -> (인증X), 시큐리티 필터에 등록해줘야 인증이 필요할때도 접근 가능
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        }
    }
}