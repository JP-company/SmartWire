package jpcompany.smartwire.domain.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;

@Getter @Setter @ToString
public class MemberJoinForm {

    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-z\\d]{8,16}$")
    private String loginId;
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*+=])[a-zA-Z\\d!@#$%^&*+=]{10,20}$")
    private String loginPassword;
    @NotEmpty
    private String loginPasswordDoubleCheck;
    @NotEmpty
    private String companyName;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String phoneNumber;
    @AssertTrue
    private Boolean termOfUse;

    public MemberJoinForm() {
    }

    public MemberJoinForm(String loginId, String loginPassword, String loginPasswordDoubleCheck) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.loginPasswordDoubleCheck = loginPasswordDoubleCheck;
    }

    public MemberJoinForm(String loginId, String loginPassword, String loginPasswordDoubleCheck, String companyName, String email, String phoneNumber, Boolean termOfUse) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.loginPasswordDoubleCheck = loginPasswordDoubleCheck;
        this.companyName = companyName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.termOfUse = termOfUse;
    }


}
