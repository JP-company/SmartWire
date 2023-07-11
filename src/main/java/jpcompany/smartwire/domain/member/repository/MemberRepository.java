package jpcompany.smartwire.domain.member.repository;

import jpcompany.smartwire.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Member update(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByLoginId(String loginId);
    List<Member> findAll();
    void setAuthCode(String loginId, String AuthCode, String email);
    void setEmailVerified(String loginId, String AuthCode);
}
