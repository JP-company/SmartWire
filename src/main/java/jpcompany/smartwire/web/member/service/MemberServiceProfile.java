package jpcompany.smartwire.web.member.service;

import jpcompany.smartwire.domain.member.Member;
import jpcompany.smartwire.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceProfile {

    private final MemberRepository repository;

    public Member findMember(String loginId) {
        Member member = repository.findByLoginId(loginId).orElse(null);
        log.info("findMember={}", member);
        return member;
    }
}
