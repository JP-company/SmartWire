package jpcompany.smartwire.web.member.service;

import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.web.member.dto.MemberUpdateDto;
import jpcompany.smartwire.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository repository;

    public Member updateCompanyNameNPhoneNumber(MemberUpdateDto memberUpdateDto) {
        Member member = new Member();
        member.setLoginId(memberUpdateDto.getLoginId());
        member.setCompanyName(memberUpdateDto.getCompanyName());
        member.setPhoneNumber(memberUpdateDto.getPhoneNumber());
        return repository.update(member);
    }

    public Member findMember(String loginId) {
        Member member = repository.findByLoginId(loginId).orElse(null);
        log.info("findMember={}", member);
        return member;
    }
}
