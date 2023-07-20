package jpcompany.smartwire.web.member.repository;

import jpcompany.smartwire.domain.member.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    void update(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByLoginId(String loginId);
    void updateAuthCodeEmail(String loginId, String AuthCode, String email);
    void setEmailVerified(String loginId);
}
