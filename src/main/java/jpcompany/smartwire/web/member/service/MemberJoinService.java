package jpcompany.smartwire.web.member.service;

import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.web.member.dto.MemberJoinDto;
import jpcompany.smartwire.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberJoinService {

    private final MemberRepository repository;
    private final MemberEmailService memberEmailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Member join(MemberJoinDto memberJoinDto) throws MessagingException, UnsupportedEncodingException {
        Member member = new Member();
        member.setLoginId(memberJoinDto.getLoginId());
        member.setLoginPassword(bCryptPasswordEncoder.encode(memberJoinDto.getLoginPassword()));
        member.setCompanyName(memberJoinDto.getCompanyName());
        member.setEmail(memberJoinDto.getEmail());
        member.setPhoneNumber(memberJoinDto.getPhoneNumber());
        member.setTermOfUse(memberJoinDto.getTermOfUse());
        member.setEmailVerified(false);
        member.setHaveMachine(false);
        member.setAuthToken(memberEmailService.sendEmail(memberJoinDto.getLoginId(), memberJoinDto.getEmail()));

        repository.save(member);
        return member;
    }

    @Transactional
    public boolean idDuplicateCheck(String loginId) {
        return repository.findByLoginId(loginId).orElse(null) == null;
    }

    public boolean passwordDoubleCheck(String password, String passwordDoubleCheck) {
        return password.equals(passwordDoubleCheck);
    }
}
