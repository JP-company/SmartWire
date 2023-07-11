package jpcompany.smartwire.domain.member.service.join;

import jpcompany.smartwire.domain.member.Member;
import jpcompany.smartwire.domain.member.MemberJoinForm;
import jpcompany.smartwire.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberServiceJoinImpl implements MemberServiceJoin {

    private final MemberRepository repository;

    @Override
    public Member join(MemberJoinForm memberJoinForm) {
        Member member = new Member();
        member.setLoginId(memberJoinForm.getLoginId());
        member.setLoginPassword(memberJoinForm.getLoginPassword());
        member.setCompanyName(memberJoinForm.getCompanyName());
        member.setEmail(memberJoinForm.getEmail());
        member.setPhoneNumber(memberJoinForm.getPhoneNumber());
        member.setTermOfUse(memberJoinForm.getTermOfUse());
        member.setEmailVerified(false);
        repository.save(member);
        return member;
    }

    @Override
    public boolean idDuplicateCheck(String loginId) {
        return repository.findByLoginId(loginId).orElse(null) == null;
    }

    @Override
    public boolean passwordDoubleCheck(String password, String passwordDoubleCheck) {
        return password.equals(passwordDoubleCheck);
    }
}
