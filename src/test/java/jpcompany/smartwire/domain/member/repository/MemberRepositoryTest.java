package jpcompany.smartwire.domain.member.repository;

import lombok.extern.slf4j.Slf4j;
import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.web.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository repository;

//    MemberRepositoryMemory repository = new MemberRepositoryMemory();
//    @AfterEach
//    void afterEach() {
//        repository.clearStore();
//    }

    @Test
    @DisplayName("회원 저장")
    void save() {
        Member member = new Member("sitsit", "1234", "에스아이티",
                "wjsdj2008@naver.com", "010-8714-4246", true,
                false, "asd");
        repository.save(member);

        Member result = repository.findById(member.getId()).get();
        assertThat(result).usingRecursiveComparison().isEqualTo(member);
    }

    @Test
    @DisplayName("회원 정보 수정")
    void update() {
        Member member = new Member("sitsit", "1234", "에스아이티",
                "wjsdj2008@naver.com", "010-8714-4246", true,
                false, "asd");
        repository.save(member);

        member.setCompanyName("SIT");
        repository.update(member);
        Member result = repository.findById(member.getId()).get();

        assertThat(result.getCompanyName()).isEqualTo("SIT");
    }

    @Test
    @DisplayName("이메일 인증키, 이메일 재설정")
    void updateEmailVerifyKeyNEmail() {
        Member member = new Member("sitsit", "1234", "에스아이티",
                "wjsdj2008@naver.com", "010-8714-4246", true,
                false, "asd");
        repository.save(member);

        String newUuid = UUID.randomUUID().toString();
        String newEmail = "wjsdj2008@gmail.com";
        repository.updateAuthCodeEmail(member.getLoginId(), newUuid, newEmail);
        Member result = repository.findById(member.getId()).get();

        assertThat(result.getAuthCode()).isEqualTo(newUuid);
        assertThat(result.getEmail()).isEqualTo("wjsdj2008@gmail.com");
    }

    @Test
    @DisplayName("해당 계정에 메일인증 여부 추가")
    void setEmailVerified() {
        Member member = new Member("sitsit", "1234", "에스아이티",
                "wjsdj2008@naver.com", "010-8714-4246", true,
                false, "asd");
        repository.save(member);

        repository.setEmailVerified(member.getLoginId());
        Member result = repository.findByLoginId(member.getLoginId()).get();
        assertThat(result.getEmailVerified()).isTrue();
    }

    @Test
    @DisplayName("저장, 수정 시 생성시간, 수정시간 적용")
    void createdTimeUpdatedTime() throws InterruptedException {
        Member member = new Member("sitsit", "1234", "에스아이티",
                "wjsdj2008@naver.com", "010-8714-4246", true,
                false, "asd");
        repository.save(member);
        assertThat(member.getUpdatedDateTime()).isEqualTo(member.getCreatedDateTime());

        Thread.sleep(1000);
        repository.update(member);
        assertThat(member.getUpdatedDateTime()).isNotEqualTo(member.getCreatedDateTime());
    }

    @Test
    @DisplayName("id로 회원 조회")
    void findById() {
        Member member1 = new Member("sitsit", "1234", "에스아이티",
                "wjsdj2008@naver.com", "010-8714-4246", true,
                false, "asd");
        Member member2 = new Member("gmgm", "1234", "광명와이어",
                "wjsdj2008@naver.com", "010-8714-4246", true,
                false, "asd");
        repository.save(member1);
        repository.save(member2);
        Member result = repository.findById(member1.getId()).orElse(null);
        assertThat(result).usingRecursiveComparison().isEqualTo(member1);
    }

    @Test
    @DisplayName("없는 아이디로 조회")
    void findByInvalidId() {
        Member member1 = new Member("sitsit", "1234", "에스아이티",
                "wjsdj2008@naver.com", "010-8714-4246", true,
                false, "asd");
        Member member2 = new Member("gmgm", "1234", "광명와이어",
                "wjsdj2008@naver.com", "010-8714-4246", true,
                false, "asd");
        repository.save(member1);
        assertThat(repository.findById(0).isPresent()).isFalse();
    }

    @Test
    @DisplayName("로그인 아이디로 조회")
    void findByLoginId() {
        Member member1 = new Member("sitsit", "1234", "에스아이티",
                "wjsdj2008@naver.com", "010-8714-4246", true,
                false, "asd");
        Member member2 = new Member("gmgm", "1234", "광명와이어",
                "wjsdj2008@naver.com", "010-8714-4246", true,
                false, "asd");
        repository.save(member1);
        repository.save(member2);
        Member result = repository.findByLoginId(member1.getLoginId()).get();
        assertThat(result).usingRecursiveComparison().isEqualTo(member1);
    }

    @Test
    @DisplayName("없는 로그인 아이디로 조회")
    void findByInvalidLoginId() {
        Member member1 = new Member("sitsit", "1234", "에스아이티",
                "wjsdj2008@naver.com", "010-8714-4246", true,
                false, "asd");
        Member member2 = new Member("gmgm", "1234", "광명와이어",
                "wjsdj2008@naver.com", "010-8714-4246", true,
                false, "asd");
        repository.save(member1);
        Optional<Member> byLoginId = repository.findByLoginId(member2.getLoginId());
        assertThat(repository.findByLoginId(member2.getLoginId()).isPresent()).isFalse();
    }
}