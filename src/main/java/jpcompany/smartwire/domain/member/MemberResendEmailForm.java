package jpcompany.smartwire.domain.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter @Setter @ToString
public class MemberResendEmailForm {
    @NotEmpty
    private String loginId;

    @NotEmpty
    @Email
    private String email;
}
