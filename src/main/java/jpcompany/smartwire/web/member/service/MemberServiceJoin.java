package jpcompany.smartwire.web.member.service;

import jpcompany.smartwire.domain.member.Member;
import jpcompany.smartwire.web.member.dto.MemberJoinDto;
import jpcompany.smartwire.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


@Service
@RequiredArgsConstructor
public class MemberServiceJoin {

    private final MemberRepository repository;
    private final MemberServiceEmail memberServiceEmail;

    public Member join(MemberJoinDto memberJoinDto) throws MessagingException, UnsupportedEncodingException {
        Member member = new Member();
        member.setLoginId(memberJoinDto.getLoginId());
        member.setLoginPassword(memberJoinDto.getLoginPassword());
        member.setCompanyName(memberJoinDto.getCompanyName());
        member.setEmail(memberJoinDto.getEmail());
        member.setPhoneNumber(memberJoinDto.getPhoneNumber());
        member.setTermOfUse(memberJoinDto.getTermOfUse());
        member.setEmailVerified(false);

        String authCode = memberServiceEmail.sendEmail(memberJoinDto.getLoginId(), memberJoinDto.getEmail());
        member.setAuthCode(authCode);
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
