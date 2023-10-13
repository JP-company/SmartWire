package jpcompany.smartwire.security.jwt.handler.dto;

import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class JwtMemberDto {
    private Integer id;
    private String loginId;
    private String companyName;
    private String email;
    private String phoneNumber;
    private String role;
}
