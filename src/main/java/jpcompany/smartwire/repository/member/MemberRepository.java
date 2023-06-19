package jpcompany.smartwire.repository.member;

import jpcompany.smartwire.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Member update(Member member);
    Optional<Member> findById(Long memberId);
    Optional<Member> findByLoginId(String LoginId);
    List<Member> findAll();
}
