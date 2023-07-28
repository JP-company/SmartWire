package jpcompany.smartwire.web.member.repository;

import jpcompany.smartwire.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Member update(Member member);
    Optional<Member> findById(Integer id);
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByAuthToken(String token);
    void updateAuthTokenEmail(String loginId, String authToken, String email);
    void setEmailVerified(String token);
    void updateHaveMachine(Integer memberId, Boolean bool);
}
