package jpcompany.smartwire.web.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class MemberUpdateDto {

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String companyName;
    @NotEmpty
    private String phoneNumber;
}
