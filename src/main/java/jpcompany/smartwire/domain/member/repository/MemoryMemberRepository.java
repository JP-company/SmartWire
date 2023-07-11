package jpcompany.smartwire.domain.member.repository;

import jpcompany.smartwire.domain.member.Member;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class MemoryMemberRepository implements MemberRepository{

    private final static Map<Long, Member> memberDB = new HashMap<>();
    private static Long sequence = 0L;

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
        result.setLoginPassword(member.getLoginPassword());
        result.setEmail(member.getEmail());
        result.setCompanyName(member.getCompanyName());
        result.setPhoneNumber(member.getPhoneNumber());

        return result;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(memberDB.get(id));
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return memberDB.values().stream()
                .filter(member -> member.getLoginId().equals(loginId))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(memberDB.values());
    }

    @Override
    public void setAuthCode(String loginId, String AuthCode, String email) {
        Member member = findByLoginId(loginId).get();
        member.setAuthCode(AuthCode);
        member.setEmail(email);
    }

    public void setEmailVerified(String loginId, String AuthCode) {
        Member member = findByLoginId(loginId).get();
        member.setEmailVerified(true);
    }

    public void clearStore() {
        memberDB.clear();
    }
}
