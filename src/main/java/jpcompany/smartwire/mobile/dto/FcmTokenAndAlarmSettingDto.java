package jpcompany.smartwire.mobile.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class FcmTokenAndAlarmSettingDto {
    private String fcmToken;
    private Integer memberId;
}
