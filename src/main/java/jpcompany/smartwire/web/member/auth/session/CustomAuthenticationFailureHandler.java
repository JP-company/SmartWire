package jpcompany.smartwire.web.member.auth.session;

import jpcompany.smartwire.web.member.auth.session.EmailNotVerifiedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        if (exception.getCause() instanceof EmailNotVerifiedException) {
            EmailNotVerifiedException emailException = (EmailNotVerifiedException) exception.getCause();
            HttpSession newSession = request.getSession();
            newSession.setAttribute("loginId", emailException.getLoginId());
            newSession.setAttribute("email", emailException.getEmail());
            response.sendRedirect("/email_verify");
        } else {
            response.sendRedirect("/login?error");
        }
    }
}