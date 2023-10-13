package jpcompany.smartwire.security.session.handler;

import jpcompany.smartwire.security.session.exception.EmailNotVerifiedException;
import jpcompany.smartwire.web.member.controller.SessionConst;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        if (exception.getCause() instanceof EmailNotVerifiedException) {
            EmailNotVerifiedException emailException = (EmailNotVerifiedException) exception.getCause();
            HttpSession newSession = request.getSession();
            newSession.setAttribute(SessionConst.LOGIN_ID, emailException.getLoginId());
            newSession.setAttribute(SessionConst.EMAIL, emailException.getEmail());
            response.sendRedirect("/email_verify");
        } else {
            response.sendRedirect("/login?error");
        }
    }
}