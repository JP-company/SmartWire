package jpcompany.smartwire.firebase.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.firebase.dto.FCMNotificationDto;
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

    public void sendNotificationByToken(FCMNotificationDto notificationDto) {
        Optional<Member> member = memberRepository.findById(notificationDto.getTargetMemberId());

        if (member.isPresent()) {
            List<String> fcmTokenList = memberRepository.getFcmTokenListById(member.get().getId());
            if (fcmTokenList != null) {
                Notification notification = Notification.builder()
                        .setTitle(notificationDto.getTitle())
                        .setBody(notificationDto.getBody())
                        .build();

                for (String fcmToken : fcmTokenList) {
                    Message message = Message.builder()
                            .setToken(fcmToken)
                            .setNotification(notification)
                            .build();
                    try {
                        firebaseMessaging.send(message);
                        log.info( "푸시 알림 성공 / id = {} / token = {}", notificationDto.getTargetMemberId(), fcmToken);
                    } catch (FirebaseMessagingException e) {
                        log.error( "푸시 알림 실패 / id = {} / token = {} / message = {}", notificationDto.getTargetMemberId(), fcmToken, e.getMessage());
                    }
                }
            } else {
                log.error( "해당 맴버 토큰 없음 / id = {}", notificationDto.getTargetMemberId());
            }
        }
    }
}
