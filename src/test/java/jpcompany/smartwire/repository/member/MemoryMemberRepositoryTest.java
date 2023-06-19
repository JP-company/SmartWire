package jpcompany.smartwire.repository.member;

import jpcompany.smartwire.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class MemoryMemberRepositoryTest {
    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    @Test
    @DisplayName("회원가입, Id로 찾기")
    void save() {
        Member member = new Member(1, "wjsdj2009", "hi1!", "SIT", 2);
        repository.save(member);

        Member result = repository.findById(member.getId()).get();
        assertThat(member).isEqualTo(result);
    }

    @Test
    @DisplayName("생성날짜, 업데이트 날짜 테스트")
    void setDateTime() {
        Member member = new Member(1, "wjsdj2009", "hi1!", "SIT", 2);
        repository.save(member);
        LocalDateTime preCreatedDateTime = member.getCreatedDateTime();
        LocalDateTime preUpdatedDateTime = member.getUpdatedDateTime();

        member.setLoginPassword("dsasd1");
        repository.save(member);
        Member result = repository.findById(member.getId()).get();
        LocalDateTime createdDateTime = result.getCreatedDateTime();
        LocalDateTime updatedDateTime = result.getUpdatedDateTime();

        assertThat(preCreatedDateTime).isEqualTo(preUpdatedDateTime); // 생성날짜시간, 업데이트날짜시간은 같은 날짜시간을 가진다.
        assertThat(preCreatedDateTime).isEqualTo(createdDateTime);  // 생성날짜시간은 변하면 안된다.
        assertThat(createdDateTime).isNotEqualTo(updatedDateTime);  // 업데이트시간은 바뀌어여한다.
    }

    @Test
    @DisplayName("로그인아이디 찾기")
    void findByLoginId() {
        Member member = new Member(1, "wjsdj2009", "hi1!", "SIT", 2);
        repository.save(member);
        Member member2 = new Member(2, "hello", "123!", "광명와이어", 6);
        repository.save(member2);

        Member wjsdj2009 = repository.findByLoginId("wjsdj2009").get();

        assertThat(wjsdj2009).isEqualTo(member);

    }

    @Test
    @DisplayName("맴버객체 모두 가져오기")
    void findAll() {
        Member member = new Member(1, "wjsdj2009", "hi1!", "SIT", 2);
        repository.save(member);
        Member member2 = new Member(2, "hello", "123!", "광명와이어", 6);
        repository.save(member2);

        List<Member> all = repository.findAll();

        assertThat(all.size()).isEqualTo(2);
    }
}