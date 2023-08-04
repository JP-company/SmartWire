package jpcompany.smartwire.web.member.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jpcompany.smartwire.web.machine.dto.MachineDto;
import jpcompany.smartwire.web.machine.repository.MachineRepositoryJdbcTemplate;
import jpcompany.smartwire.web.member.auth.PrincipalDetails;
import jpcompany.smartwire.web.member.dto.MemberLoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

//@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        // /api/login 경로에 대한 요청만 처리하도록 설정
        this.setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("api 로그인 요청, URI={}", request.getRequestURI());

        ObjectMapper om = new ObjectMapper();
        MemberLoginDto memberLoginDto = null;
        try {
            memberLoginDto = om.readValue(request.getInputStream(), MemberLoginDto.class);
        } catch (IOException e) {
            log.error("로그인 Dto 객체 할당 실패=", e);
        }
        log.info("로그인 요청 객체={}", memberLoginDto);

        // 유저네임패스워드 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        memberLoginDto.getLoginId(),
                        memberLoginDto.getLoginPassword());

        log.info("토큰 생성 완료={}", authenticationToken);

        // authenticate() 함수가 호출 되면 인증 프로바이더가 유저 디테일 서비스의
        // loadUserByUsername(토큰의 첫번째 파라메터) 를 호출하고
        // UserDetails 를 리턴받아서 토큰의 두번째 파라메터(credential)과
        // UserDetails(DB값)의 getPassword()함수로 비교해서 동일하면
        // Authentication 객체를 만들어서 필터체인으로 리턴해준다.
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        log.info("principalDetails={}", principalDetails);
        return authentication;
    }

    // JWT Token 생성해서 response 에 담아주기
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        MachineRepositoryJdbcTemplate machineRepository = applicationContext.getBean(MachineRepositoryJdbcTemplate.class);

        log.info("로그인 성공={}", authResult.getPrincipal());

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        List<MachineDto> machineList = machineRepository.findAll(principalDetails.getMember().getId());
        machineList.sort(Comparator.comparingInt(MachineDto::getSequence));
        log.info("기계 리스트 json 바꾸기 전={}", machineList);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // JavaTimeModule 등록

        String machineListJson;
        try {
            machineListJson = objectMapper.writeValueAsString(machineList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert machineList to JSON", e);
        }
        log.info("기계 리스트 api 로 보내기={}", machineListJson);

        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("loginId", principalDetails.getMember().getLoginId())
                //.withClaim("machineList", machineListJson)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        log.info("JWT 토큰={}", jwtToken);

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
    }
}
