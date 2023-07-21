package jpcompany.smartwire.web.member.repository;

import jpcompany.smartwire.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Member update(Member member);
    Optional<Member> findById(Integer id);
    Optional<Member> findByLoginId(String loginId);
    void updateAuthCodeEmail(String loginId, String AuthCode, String email);
    void setEmailVerified(String loginId);
}
