package jpcompany.smartwire.domain.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter @ToString
public class Member {

    private Long id;
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

    public Member(String loginId, String loginPassword, String companyName) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.companyName = companyName;
    }

    public Member(String loginId, String loginPassword, String companyName, String email, String phoneNumber) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.companyName = companyName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
