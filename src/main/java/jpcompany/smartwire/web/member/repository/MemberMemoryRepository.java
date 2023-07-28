package jpcompany.smartwire.web.member.repository;

import jpcompany.smartwire.domain.Member;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

//@Repository
public class MemberMemoryRepository implements MemberRepository{

    private final static Map<Integer, Member> memberDB = new HashMap<>();
    private static Integer sequence = 0;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        setDateTime(member);
        memberDB.put(member.getId(), member);
        return member;
    }

    private void setDateTime(Member member) {
        LocalDateTime now = LocalDateTime.now();
        if (member.getCreatedDateTime() == null) {
            member.setCreatedDateTime(now);
        }
        member.setUpdatedDateTime(now);
    }

    @Override
    public Member update(Member member) {
        Member result = findById(member.getId()).get();
        setDateTime(result);
        result.setLoginPassword(member.getLoginPassword());
        result.setEmail(member.getEmail());
        result.setCompanyName(member.getCompanyName());
        result.setPhoneNumber(member.getPhoneNumber());

        return result;
    }

    @Override
    public Optional<Member> findById(Integer id) {
        return Optional.ofNullable(memberDB.get(id));
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return memberDB.values().stream()
                .filter(member -> member.getLoginId().equals(loginId))
                .findAny();
    }

    @Override
    public Optional<Member> findByAuthToken(String token) {
        return Optional.empty();
    }


    @Override
    public void updateAuthTokenEmail(String loginId, String AuthCode, String email) {
        Member member = findByLoginId(loginId).get();
        member.setAuthToken(AuthCode);
        member.setEmail(email);
    }

    @Override
    public void setEmailVerified(String loginId) {
        Member member = findByLoginId(loginId).get();
        member.setEmailVerified(true);
    }

    @Override
    public void updateHaveMachine(Integer memberId, Boolean bool) {

    }

    public void clearStore() {
        memberDB.clear();
    }
}
