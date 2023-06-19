package jpcompany.smartwire.repository.member;

import jpcompany.smartwire.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JdbcTemplateMemberRepositoryTest {

    @Autowired MemberRepository repository;

    @Test
    @DisplayName("맴버 저장")
    void save() {
        Member member = new Member("sitsit", "123123", "SIT", 2);
        repository.save(member);

        Member result = repository.findById(member.getId()).get();
        assertThat(member.getId()).isEqualTo(result.getId());
    }

    @Test
    @DisplayName("id로 찾기")
    void findById() {
        Member member1 = new Member("sitsit", "123123", "SIT", 2);
        repository.save(member1);


        Member member2 = new Member("kmkm", "123123", "광명와이어", 6);
        repository.save(member2);

        Member result = repository.findById(member1.getId()).get();

        assertThat(result.getId()).isEqualTo(member1.getId());
    }

    @Test
    @DisplayName("로그인아이디로 찾기")
    void findByLoginId() {
        Member member1 = new Member("sitsit", "123123", "SIT", 2);
        repository.save(member1);

        Member member2 = new Member("kmkm", "123123", "광명와이어", 6);
        repository.save(member2);

        Member result = repository.findByLoginId(member1.getLoginId()).get();

        assertThat(result.getId()).isEqualTo(member1.getId());
    }

    @Test
    @DisplayName("맴버 데이터 변경")
    void update() {
        Member member = new Member("sitsit", "123123", "SIT", 2);
        repository.save(member);

        member.setLoginPassword("456456");
        member.setCompanyName("광명와이어");
        member.setMachinesNum(8);
        repository.update(member);

        Member memberAfterUpdate = repository.findById(member.getId()).get();

        assertThat(member.getId()).isEqualTo(memberAfterUpdate.getId());
        assertThat(member.getLoginPassword()).isEqualTo("456456");
        assertThat(member.getCompanyName()).isEqualTo("광명와이어");
        assertThat(member.getMachinesNum()).isEqualTo(8);
    }

    @Test
    void findAll() {
    }
}