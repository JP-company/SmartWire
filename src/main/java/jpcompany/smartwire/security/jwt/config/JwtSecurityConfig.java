package jpcompany.smartwire.security.jwt.config;

import jpcompany.smartwire.security.jwt.entry_point.JwtLoginAuthenticationEntryPoint;
import jpcompany.smartwire.security.jwt.filter.JwtAuthorizationFilter;
import jpcompany.smartwire.security.jwt.handler.JwtAccessDeniedHandler;
import jpcompany.smartwire.security.jwt.handler.JwtAuthenticationFailureHandler;
import jpcompany.smartwire.security.jwt.handler.JwtAuthenticationSuccessHandler;
import jpcompany.smartwire.security.jwt.provider.JwtAuthenticationProvider;
import jpcompany.smartwire.web.member.repository.MemberJdbcTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class JwtSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserDetailsService userDetailsService;
    private final MemberJdbcTemplateRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/messages").hasRole("MEMBER")
                .antMatchers("/api/login").permitAll()
                .anyRequest().authenticated();

        http
                .exceptionHandling()
                .authenticationEntryPoint(new JwtLoginAuthenticationEntryPoint())
                .accessDeniedHandler(jwtAccessDeniedHandler());

        http
                .apply(new JwtLoginConfigurer<>())
                .setSuccessHandlerJwt(jwtAuthenticationSuccessHandler())
                .setFailureHandlerJwt(jwtAuthenticationFailureHandler())
                .setAuthenticationManager(authenticationManager(authenticationConfiguration))
                .setAuthorizationFilter(jwtAuthorizationFilter())
                .loginProcessingUrl("/api/login");

        return http.build();
    }

    @Bean
    public AccessDeniedHandler jwtAccessDeniedHandler() {
        return new JwtAccessDeniedHandler();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        ProviderManager authenticationManager = (ProviderManager)authenticationConfiguration.getAuthenticationManager();
        authenticationManager.getProviders().add(jwtAuthenticationProvider());
        return authenticationManager;
    }
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(authenticationManager(authenticationConfiguration), memberRepository);
    }
    @Bean
    public AuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    @Bean
    public AuthenticationSuccessHandler jwtAuthenticationSuccessHandler() {
        return new JwtAuthenticationSuccessHandler();
    }
    @Bean
    public AuthenticationFailureHandler jwtAuthenticationFailureHandler() {
        return new JwtAuthenticationFailureHandler();
    }
}