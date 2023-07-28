package jpcompany.smartwire.web.member.controller.websocket;

import jpcompany.smartwire.web.member.auth.PrincipalDetails;
import jpcompany.smartwire.web.member.controller.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

@Slf4j
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof PrincipalDetails) {
            PrincipalDetails member = (PrincipalDetails) auth.getPrincipal();
            log.info("사용자 정보={}", member.getUsername());

            // WebSocket 세션에 인증 정보를 연결
            attributes.put("PRINCIPAL", member);
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        // Nothing to do after handshake
    }
}