package jpcompany.smartwire.web.member.service;

import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.web.member.dto.MemberJoinDto;
import jpcompany.smartwire.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberJoinService {

    private final MemberRepository repository;
    private final MemberEmailService memberEmailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void join(MemberJoinDto memberJoinDto) throws MessagingException {
        String emailAuthToken = memberEmailService.createEmailAuthToken();

        memberJoinDto.setLoginPassword(bCryptPasswordEncoder.encode(memberJoinDto.getLoginPassword()));
        memberJoinDto.setAuthToken(emailAuthToken);

        try {
            repository.save(memberJoinDto);  // DB에 정상적으로 저장되면 이메일 전송
            memberEmailService.sendEmail(memberJoinDto.getLoginId(), memberJoinDto.getEmail(), emailAuthToken); // 메일 전송 안되면 재전송 사용자에게 재전송 버튼 클릭 요청
        } catch (MessagingException | MailAuthenticationException | MailSendException e) {
            // 메일 전송 실패
        } catch (Exception e) {
            // DB 저장 실패
        }

    }

    @Transactional
    public boolean idDuplicateCheck(String loginId) {
        return repository.findByLoginId(loginId).orElse(null) == null;
    }

    public boolean passwordDoubleCheck(String password, String passwordDoubleCheck) {
        return password.equals(passwordDoubleCheck);
    }
}
