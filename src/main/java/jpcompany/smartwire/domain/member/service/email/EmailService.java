package jpcompany.smartwire.domain.member.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;


public interface EmailService {
    String sendEmail(String loginId, String email) throws MessagingException, UnsupportedEncodingException;

}
