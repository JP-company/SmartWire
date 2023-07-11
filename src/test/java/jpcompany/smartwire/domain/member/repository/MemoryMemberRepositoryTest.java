package jpcompany.smartwire.domain.member.repository;

import groovy.util.logging.Slf4j;
import jpcompany.smartwire.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@Slf4j
class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    @Test
    @DisplayName("회원 저장")
    void save() {
        Member member = new Member("sitsit", "1234", "에스아이티");
        repository.save(member);

        Member result = repository.findById(member.getId()).get();
        assertThat(result).isEqualTo(member);
    }

    @Test
    @DisplayName("회원 정보 수정")
    void update() {
        Member member = new Member("sitsit", "1234", "에스아이티");
        repository.save(member);

        member.setCompanyName("SIT");
        Member update = repository.update(member);

        assertThat(update.getCompanyName()).isEqualTo("SIT");
    }

    @Test
    @DisplayName("저장 시 생성시간, 수정시간 적용")
    void createdTimeUpdatedTime() throws InterruptedException {
        Member member = new Member("sitsit", "1234", "에스아이티");
        repository.save(member);
        assertThat(member.getUpdatedDateTime()).isEqualTo(member.getCreatedDateTime());

        repository.save(member);
        assertThat(member.getUpdatedDateTime()).isNotEqualTo(member.getCreatedDateTime());
    }

    @Test
    @DisplayName("id로 회원 조회")
    void findById() {
        Member member1 = new Member("sitsit", "1234", "에스아이티");
        Member member2 = new Member("gmgm", "1234", "광명와이어");
        repository.save(member1);
        repository.save(member2);
        Member result = repository.findById(member1.getId()).orElse(null);
        assertThat(result).isEqualTo(member1);
    }

    @Test
    @DisplayName("없는 아이디로 조회")
    void findByInvalidId() {
        Member member1 = new Member("sitsit", "1234", "에스아이티");
        Member member2 = new Member("gmgm", "1234", "광명와이어");
        repository.save(member1);
        Member result = repository.findById(member2.getId()).orElse(null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("로그인 아이디로 조회")
    void findByLoginId() {
        Member member1 = new Member("sitsit", "1234", "에스아이티");
        Member member2 = new Member("gmgm", "1234", "광명와이어");
        repository.save(member1);
        repository.save(member2);
        Member result = repository.findByLoginId(member1.getLoginId()).get();
        assertThat(result).isEqualTo(member1);
    }

    @Test
    @DisplayName("없는 로그인 아이디로 조회")
    void findByInvalidLoginId() {
        Member member1 = new Member("sitsit", "1234", "에스아이티");
        Member member2 = new Member("gmgm", "1234", "광명와이어");
        repository.save(member1);
        Member result = repository.findByLoginId(member2.getLoginId()).orElse(null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("모든 회원 조회")
    void findAll() {
        Member member1 = new Member("sitsit", "1234", "에스아이티");
        Member member2 = new Member("gmgm", "1234", "광명와이어");
        repository.save(member1);
        repository.save(member2);
        List<Member> all = repository.findAll();
        assertThat(all.size()).isEqualTo(2);
    }
}