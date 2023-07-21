package jpcompany.smartwire.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter @ToString
public class Member {

    private Integer id;
    private String loginId;
    private String loginPassword;
    private String companyName;
    private String email;
    private String phoneNumber;
    private Boolean termOfUse;
    private Boolean emailVerified;
    private String authCode;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;

    public Member() { }

    public Member(String loginId, String loginPassword, String companyName, String email, String phoneNumber, Boolean termOfUse, Boolean emailVerified, String authCode) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.companyName = companyName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.termOfUse = termOfUse;
        this.emailVerified = emailVerified;
        this.authCode = authCode;
    }
}
