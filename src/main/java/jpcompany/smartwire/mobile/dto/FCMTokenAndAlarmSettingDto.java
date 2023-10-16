package jpcompany.smartwire.mobile.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class FCMTokenAndAlarmSettingDto {
    private Integer id;
    private String fcmToken;
    private Integer memberId;
}
