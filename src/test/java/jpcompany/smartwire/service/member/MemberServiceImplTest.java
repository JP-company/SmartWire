package jpcompany.smartwire.service.member;

import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("회원가입")
    void join() {
        Member member = new Member("sitsit", "123123", "SIT", 2);
        Long saveID = memberService.join(member);

        Member findMember = memberService.findOne(saveID).get();
        assertThat(member.getId()).isEqualTo(findMember.getId());
    }

    @Test
    void 중복_회원_예외() {
        // given
        Member member1 = new Member("sitsit", "123123", "SIT", 2);
        Member member2 = new Member("sitsit", "321321", "SITPRESS", 2);

        // when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        // then
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

    @Test
    void changePassword() {
    }
}