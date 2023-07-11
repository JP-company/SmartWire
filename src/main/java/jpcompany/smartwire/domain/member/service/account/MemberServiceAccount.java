package jpcompany.smartwire.domain.member.service.account;

import jpcompany.smartwire.domain.member.Member;
import jpcompany.smartwire.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public interface MemberServiceAccount {
    void updateAuthCode(String loginId, String AuthCode, String email);
    Member verifyAuthCode(String loginId, String AuthCode);
}
