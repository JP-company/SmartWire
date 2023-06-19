package jpcompany.smartwire.repository.member;

import jpcompany.smartwire.domain.Member;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

//@Repository
public class MemoryMemberRepository implements MemberRepository {

    private final static Map<Long, Member> storeMembers = new HashMap<>();

    private static Long sequence = 0L;  // id로 씀 단순히 1, 2, 3 ,4 ...

    @Override
    public Member save(Member member) {
        setDateTime(member); // 생성날짜시간, 업데이트날짜시간 설정
        storeMembers.put(member.getId(), member);  // store Map의 키를 id, member객체 자체를 value로 지정한다.
        return member;
    }

    @Override
    public Member update(Member member) {
        return null;
    }

    private void setDateTime(Member member) {
        LocalDateTime now = LocalDateTime.now();
        if (member.getCreatedDateTime() == null) {
            member.setCreatedDateTime(now);
        }
        member.setUpdatedDateTime(now);
    }


    @Override
    public Optional<Member> findById(Long memberId) {
        return Optional.ofNullable(storeMembers.get(memberId));
    }

    @Override
    public Optional<Member> findByLoginId(String LoginId) {
        return storeMembers.values().stream()
                .filter(member -> member.getLoginId().equals(LoginId))
                .findAny();
    }


    @Override
    public List<Member> findAll() {
        return new ArrayList<>(storeMembers.values());
    }

    public void clearStore() {
        storeMembers.clear();
    }
}
