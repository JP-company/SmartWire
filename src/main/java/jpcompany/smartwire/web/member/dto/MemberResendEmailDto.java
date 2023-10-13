package jpcompany.smartwire.web.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter @Setter @ToString
public class MemberResendEmailDto {
    @NotEmpty
    private String loginId;

    @NotEmpty
    @Email
    private String email;

    public MemberResendEmailDto(String loginId, String email) {
        this.loginId = loginId;
        this.email = email;
    }
}
