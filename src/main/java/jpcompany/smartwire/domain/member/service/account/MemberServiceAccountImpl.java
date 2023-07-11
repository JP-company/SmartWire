package jpcompany.smartwire.domain.member.service.account;

import jpcompany.smartwire.domain.member.Member;
import jpcompany.smartwire.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceAccountImpl implements MemberServiceAccount{

    private final MemberRepository repository;
    @Override
    public void updateAuthCode(String loginId, String authCode, String email) {
        repository.setAuthCode(loginId, authCode, email);
    }

    public Member verifyAuthCode(String loginId, String authCode) {
        Member member = repository.findByLoginId(loginId).orElse(null);
        if (member!= null && member.getAuthCode().equals(authCode)) {
            repository.setEmailVerified(loginId, authCode);
            return member;
        }
        return null;
    }
}
