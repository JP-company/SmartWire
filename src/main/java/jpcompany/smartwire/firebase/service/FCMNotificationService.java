package jpcompany.smartwire.firebase.service;

import com.google.firebase.messaging.*;
import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.firebase.dto.FCMNotificationDto;
import jpcompany.smartwire.mobile.dto.FCMTokenAndAlarmSettingDto;
import jpcompany.smartwire.mobile.repository.MobileRepository;
import jpcompany.smartwire.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMNotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final MemberRepository memberRepository;
    private final MobileRepository mobileRepository;

    private final AndroidConfig androidConfig = AndroidConfig.builder()
            .setPriority(AndroidConfig.Priority.HIGH) // 최대 우선순위 설정
            .setNotification(AndroidNotification.builder()
                    .setSound("default") // 소리 설정
                    .build()
            )
            .build();

    private final ApnsConfig apnsConfig = ApnsConfig.builder()
            .putHeader("apns-priority", "10") // 최대 우선순위로 설정 (10은 최대값입니다)
            .setAps(Aps.builder()
                    .setSound("default") // 기본 소리를 설정하거나 원하는 소리 파일 이름을 제공할 수 있습니다.
                    .build())
            .build();

    public void sendNotificationByToken(FCMNotificationDto notificationDto, Member member) {
        // Heroku 기준 한국 시간
//        LocalTime now = LocalTime.now().plusHours(9);
        LocalTime now = LocalTime.now();

        List<FCMTokenAndAlarmSettingDto> fcmTokenAndAlarmSettingDtoList = memberRepository.getFcmTokenListById(member.getId());
        if (fcmTokenAndAlarmSettingDtoList != null) {
            Notification notification = Notification.builder()
                    .setTitle(notificationDto.getTitle())
                    .setBody(notificationDto.getBody())
                    .build();

            for (FCMTokenAndAlarmSettingDto fcmTokenAndAlarmSettingDto : fcmTokenAndAlarmSettingDtoList) {
                String alarmSetting = fcmTokenAndAlarmSettingDto.getAlarmSetting();

                if (alarmSetting.charAt(0) == 'y' && isTime(now, alarmSetting)) {
                    Message message = Message.builder()
                            .setToken(fcmTokenAndAlarmSettingDto.getFcmToken())
                            .setNotification(notification)
                            .setAndroidConfig(androidConfig)
                            .setApnsConfig(apnsConfig)
                            .build();
                    try {
                        firebaseMessaging.send(message);
                        log.info( "푸시 알림 성공 / id = {} / token = {}",
                                notificationDto.getTargetMemberId(),
                                fcmTokenAndAlarmSettingDto.getFcmToken()
                        );
                    } catch (FirebaseMessagingException e) {
                        if (e.getMessage().contains("not a valid FCM") ||
                            e.getMessage().contains("Requested entity was not found")
                        ) {
                            mobileRepository.deleteFCMTokenById(fcmTokenAndAlarmSettingDto.getId());
                            log.info("유효하지 않은 FCM Token 삭제 = {}", fcmTokenAndAlarmSettingDto.getFcmToken());
                        }
                        log.error( "푸시 알림 실패 / id = {} / token = {} / message = {}",
                                notificationDto.getTargetMemberId(),
                                fcmTokenAndAlarmSettingDto.getFcmToken(),
                                e.getMessage()
                        );
                    }
                } else {
                    log.error( "푸시 알림 시간 아님 / id = {} / token = {} / alarmSetting = {}",
                            notificationDto.getTargetMemberId(),
                            fcmTokenAndAlarmSettingDto.getFcmToken(),
                            fcmTokenAndAlarmSettingDto.getAlarmSetting()
                    );
                }
            }
        } else {
            log.error( "푸시 알림 실패 해당 맴버 토큰 없음 / id = {}", notificationDto.getTargetMemberId());
        }

    }

    private boolean isTime(LocalTime now, String alarmSetting) {
        String alarmStartTimeStr = alarmSetting.substring(1).split(",")[0];
        String alarmLastTimeStr = alarmSetting.substring(1).split(",")[1];
        LocalTime alarmStartTime = LocalTime.of(
                Integer.parseInt(alarmStartTimeStr.split(":")[0]),
                Integer.parseInt(alarmStartTimeStr.split(":")[1]), 0);
        LocalTime alarmLastTime = LocalTime.of(
                Integer.parseInt(alarmLastTimeStr.split(":")[0]),
                Integer.parseInt(alarmLastTimeStr.split(":")[1]), 0);

        if (alarmStartTime.isBefore(alarmLastTime)) {
            return now.isAfter(alarmStartTime) && now.isBefore(alarmLastTime);
        } else {
            return now.isAfter(alarmStartTime) || now.isBefore(alarmLastTime);
        }
    }
}
