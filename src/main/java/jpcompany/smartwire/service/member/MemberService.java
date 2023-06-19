package jpcompany.smartwire.service.member;

import jpcompany.smartwire.domain.Member;

import java.util.Optional;

public interface MemberService {

    Long join(Member member);
    void updateInfo(Member member);
    Optional<Member> findOne(String memberId);
    Optional<Member> findOne(Long id);
    void changePassword(Member member);

}
