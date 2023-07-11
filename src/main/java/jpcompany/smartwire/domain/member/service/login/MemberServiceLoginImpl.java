package jpcompany.smartwire.domain.member.service.login;

import jpcompany.smartwire.domain.member.Member;
import jpcompany.smartwire.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceLoginImpl implements MemberServiceLogin{

    private final MemberRepository repository;
    @Override
    public Member login(String loginId, String loginPassword) {
        return repository.findByLoginId(loginId)
                .filter(member -> member.getLoginPassword().equals(loginPassword))
                .orElse(null);
    }
}
