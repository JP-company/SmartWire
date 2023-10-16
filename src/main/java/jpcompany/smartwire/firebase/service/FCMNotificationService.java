package jpcompany.smartwire.firebase.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.firebase.dto.FCMNotificationDto;
import jpcompany.smartwire.mobile.dto.FCMTokenAndAlarmSettingDto;
import jpcompany.smartwire.mobile.repository.MobileRepository;
import jpcompany.smartwire.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMNotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final MemberRepository memberRepository;
    private final MobileRepository mobileRepository;

    public void sendNotificationByToken(FCMNotificationDto notificationDto) {
        Optional<Member> member = memberRepository.findById(notificationDto.getTargetMemberId());

        if (member.isPresent()) {
            List<FCMTokenAndAlarmSettingDto> fcmTokenAndAlarmSettingDtoList = memberRepository.getFcmTokenListById(member.get().getId());
            if (fcmTokenAndAlarmSettingDtoList != null) {
                Notification notification = Notification.builder()
                        .setTitle(notificationDto.getTitle())
                        .setBody(notificationDto.getBody())
                        .build();

                for (FCMTokenAndAlarmSettingDto fcmTokenAndAlarmSettingDto : fcmTokenAndAlarmSettingDtoList) {
                    Message message = Message.builder()
                            .setToken(fcmTokenAndAlarmSettingDto.getFcmToken())
                            .setNotification(notification)
                            .build();
                    try {
                        firebaseMessaging.send(message);

                        log.info( "푸시 알림 성공 / id = {} / token = {}",
                                notificationDto.getTargetMemberId(),
                                fcmTokenAndAlarmSettingDto.getFcmToken()
                        );

                    } catch (FirebaseMessagingException e) {
                        if (e.getMessage().contains("not a valid FCM")) {
                            mobileRepository.deleteFCMTokenById(fcmTokenAndAlarmSettingDto.getId());
                            log.info("유효하지 않은 FCM Token 삭제 = {}", fcmTokenAndAlarmSettingDto.getFcmToken());
                            return;
                        }

                        log.error( "푸시 알림 실패 / id = {} / token = {} / message = {}",
                                notificationDto.getTargetMemberId(),
                                fcmTokenAndAlarmSettingDto.getFcmToken(),
                                e.getMessage()
                        );

                    }
                }
            } else {
                log.error( "푸시 알림 실패 해당 맴버 토큰 없음 / id = {}", notificationDto.getTargetMemberId());
            }
        }
    }
}
