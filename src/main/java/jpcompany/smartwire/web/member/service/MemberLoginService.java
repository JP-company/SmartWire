package jpcompany.smartwire.web.member.service;

import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberLoginService {

    private final MemberRepository repository;
    public void updateAuthToken(String loginId, String authToken, String email) {
        repository.updateAuthTokenEmail(loginId, authToken, email);
    }
    public Member verifyAuthToken(String authToken) {
        Member member = repository.findByAuthToken(authToken).orElse(null);

        if (member != null && member.getAuthToken().equals(authToken)) {
            repository.setEmailVerified(authToken);
            return member;
        }
        return null;
    }
    public boolean isEmailVerified(String loginId) {
        Member member = repository.findByLoginId(loginId).orElse(null);
        return member != null && !member.getRole().equals("ROLE_UNVERIFIED");
    }
}