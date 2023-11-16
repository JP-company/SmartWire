package jpcompany.smartwire.web.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter @Setter @ToString
public class MemberLoginDto {

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String loginPassword;

    private String role;

    public MemberLoginDto() {
    }

    public MemberLoginDto(String loginId, String loginPassword) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
    }

    public MemberLoginDto(String loginId) {
        this.loginId = loginId;
    }
}
