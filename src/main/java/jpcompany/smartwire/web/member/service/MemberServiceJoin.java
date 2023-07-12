package jpcompany.smartwire.web.member.service;

import jpcompany.smartwire.domain.member.Member;
import jpcompany.smartwire.web.member.dto.MemberJoinDto;
import jpcompany.smartwire.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberServiceJoin {

    private final MemberRepository repository;

    public Member join(MemberJoinDto memberJoinDto) {
        Member member = new Member();
        member.setLoginId(memberJoinDto.getLoginId());
        member.setLoginPassword(memberJoinDto.getLoginPassword());
        member.setCompanyName(memberJoinDto.getCompanyName());
        member.setEmail(memberJoinDto.getEmail());
        member.setPhoneNumber(memberJoinDto.getPhoneNumber());
        member.setTermOfUse(memberJoinDto.getTermOfUse());
        member.setEmailVerified(false);
        repository.save(member);
        return member;
    }

    public boolean idDuplicateCheck(String loginId) {
        return repository.findByLoginId(loginId).orElse(null) == null;
    }

    public boolean passwordDoubleCheck(String password, String passwordDoubleCheck) {
        return password.equals(passwordDoubleCheck);
    }
}
