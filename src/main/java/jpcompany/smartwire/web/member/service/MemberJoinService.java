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
    public void join(MemberJoinDto memberJoinDto) throws MessagingException, UnsupportedEncodingException {
        memberJoinDto.setLoginPassword(bCryptPasswordEncoder.encode(memberJoinDto.getLoginPassword()));
        memberJoinDto.setAuthToken(memberEmailService.sendEmail(memberJoinDto.getLoginId(), memberJoinDto.getEmail()));
        repository.save(memberJoinDto);
    }

    @Transactional
    public boolean idDuplicateCheck(String loginId) {
        return repository.findByLoginId(loginId).orElse(null) == null;
    }

    public boolean passwordDoubleCheck(String password, String passwordDoubleCheck) {
        return password.equals(passwordDoubleCheck);
    }
}
