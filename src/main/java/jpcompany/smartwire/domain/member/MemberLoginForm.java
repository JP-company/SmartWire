package jpcompany.smartwire.domain.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter @Setter @ToString
public class MemberLoginForm {

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String loginPassword;

    public MemberLoginForm() {
    }

    public MemberLoginForm(String loginId) {
        this.loginId = loginId;
    }
}
