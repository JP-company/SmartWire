package jpcompany.smartwire.security.jwt.handler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.security.common.PrincipalDetails;
import jpcompany.smartwire.security.jwt.filter.JwtProperties;
import jpcompany.smartwire.security.jwt.handler.dto.JwtAuthenticationDto;
import jpcompany.smartwire.security.jwt.handler.dto.JwtMemberDto;
import jpcompany.smartwire.web.machine.dto.MachineDto;
import jpcompany.smartwire.web.machine.repository.MachineRepositoryJdbcTemplate;
import jpcompany.smartwire.web.member.controller.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final MachineRepositoryJdbcTemplate machineRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Jwt 토큰 담아서 response 헤더에 담아주기
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        Member member = (Member) authentication.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject(member.getLoginId())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim(SessionConst.LOGIN_ID, member.getLoginId())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        objectMapper.registerModule(new JavaTimeModule()); // JavaTimeModule 등록
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 없는 필드 무시

        JwtMemberDto jwtMemberDto = objectMapper.convertValue(member, JwtMemberDto.class);
        List<MachineDto> machineList = machineRepository.findAll(member.getId());
        machineList.sort(Comparator.comparingInt(MachineDto::getSequence));

        String jwtAuthenticationJson = objectMapper.writeValueAsString(new JwtAuthenticationDto(jwtMemberDto, machineList));

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(jwtAuthenticationJson);
    }
}
