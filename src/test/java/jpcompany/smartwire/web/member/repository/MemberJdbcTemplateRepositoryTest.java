package jpcompany.smartwire.web.member.repository;

import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.firebase.dto.FCMNotificationDto;
import jpcompany.smartwire.firebase.service.FCMNotificationService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberJdbcTemplateRepositoryTest {

    @Autowired FCMNotificationService service;

    @Test
    void doTest() {
        service.sendNotificationByToken(FCMNotificationDto.builder().targetMemberId(195).title("test").body("testMessage").build());

//        List<String> fcmTokenListById = memberRepository.getFcmTokenListById(195);
//        for (String token : fcmTokenListById) {
//            System.out.println("token = " + token);
//        }
    }
}