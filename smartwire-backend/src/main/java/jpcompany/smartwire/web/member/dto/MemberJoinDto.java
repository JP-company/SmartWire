package jpcompany.smartwire.web.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter @Setter @ToString
public class MemberJoinDto {

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
    private String authToken;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;

    public MemberJoinDto() { }
}
