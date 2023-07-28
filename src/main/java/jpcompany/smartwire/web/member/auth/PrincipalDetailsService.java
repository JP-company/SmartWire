package jpcompany.smartwire.web.member.auth;

import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login");
// login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 메서드가 실행된다.
@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // 시큐리티 session(내부 Authentication(내부 UserDetails))
    @Override
    // '/login' POST 에서 받는 폼 input 태그의 name 과 loadUserByUsername() 메서드의 파라미터가 같아야 한다.
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        Member member = memberRepository.findByLoginId(loginId).orElse(null);
        log.info("로그인 계정={}", member);

        if (member != null) {
            if (!member.getEmailVerified()) {
                throw new EmailNotVerifiedException(member.getLoginId(), member.getEmail());
            }
            return new PrincipalDetails(member);
        }
        return null;
    }
}
