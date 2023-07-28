package jpcompany.smartwire.web.member.auth;

import org.springframework.security.core.AuthenticationException;

public class EmailNotVerifiedException extends AuthenticationException {

    private final String loginId;
    private final String email;

    public EmailNotVerifiedException(String loginId, String email) {
        super("이메일 인증이 필요합니다.");
        this.loginId = loginId;
        this.email = email;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getEmail() {
        return email;
    }
}
