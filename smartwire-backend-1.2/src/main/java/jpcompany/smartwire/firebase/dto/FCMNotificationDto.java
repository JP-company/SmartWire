package jpcompany.smartwire.firebase.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMNotificationDto {
    private Integer targetMemberId;
    private String title;
    private String body;

    @Builder
    public FCMNotificationDto(Integer targetMemberId, String title, String body) {
        this.targetMemberId = targetMemberId;
        this.title = title;
        this.body = body;
    }
}
