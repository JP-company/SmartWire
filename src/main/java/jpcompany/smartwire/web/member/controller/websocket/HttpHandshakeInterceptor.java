package jpcompany.smartwire.web.member.controller.websocket;

import jpcompany.smartwire.web.member.controller.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

@Slf4j
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        // HttpSession에서 인증 정보를 추출
//        Principal principal = request.getPrincipal();
        Principal principal = null;

        // Check if the HTTP request is a servlet request
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession();

            // Get the user object from HTTP session
            Object user = session.getAttribute(SessionConst.LOGIN_MEMBER);
            log.info("사용자 정보={}", user);

            // Create a custom Principal
            principal = new Principal() {
                @Override
                public String getName() {
                    return user.toString(); // Or any unique identifier of the user
                }
            };
        }

        if (principal == null) {
            // appropriate handling when Principal is null
        } else {
            // WebSocket 세션에 인증 정보를 연결
            attributes.put("PRINCIPAL", principal);
        }

        log.info("principal 이게대체 뭐애={}", principal.getName());

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        // Nothing to do after handshake
    }
}