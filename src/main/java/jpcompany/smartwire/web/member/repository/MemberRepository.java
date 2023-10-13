package jpcompany.smartwire.web.member.repository;

import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.web.member.dto.MemberJoinDto;

import java.util.Optional;

public interface MemberRepository {
    void save(MemberJoinDto memberJoinDto);
    Member update(Member member);
    Optional<Member> findById(Integer id);
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByAuthToken(String token);
    void updateAuthTokenEmail(String loginId, String authToken, String email);
    void setEmailVerified(String token);
    void updateHaveMachine(Integer memberId, Boolean bool);
}
