package jpcompany.smartwire.security.jwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jpcompany.smartwire.security.common.PrincipalDetails;
import jpcompany.smartwire.security.jwt.token.JwtAuthenticationToken;
import jpcompany.smartwire.web.machine.dto.MachineDto;
import jpcompany.smartwire.web.machine.repository.MachineRepositoryJdbcTemplate;
import jpcompany.smartwire.web.member.dto.MemberLoginDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;


// UsernamePasswordAuthenticationFilter 와 같은 역할을 하는 필터
@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public JwtAuthenticationFilter() {
        super(new AntPathRequestMatcher("/api/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper om = new ObjectMapper();
        MemberLoginDto memberLoginDto = null;
        try {
            memberLoginDto = om.readValue(request.getInputStream(), MemberLoginDto.class);
        } catch (IOException e) {
            log.error("로그인 Dto 객체 할당 실패=", e);
        }

        // 유저네임패스워드 토큰 생성
        JwtAuthenticationToken authenticationToken =
                new JwtAuthenticationToken(
                        memberLoginDto.getLoginId(),
                        memberLoginDto.getLoginPassword());

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    /*  TODO - Jwt 요청 확인 나중에 적용해보자
    private boolean isJwt(HttpServletRequest request) {  // 약속에 따라 Jwt 요청인지 구분한다.
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
     */

}
