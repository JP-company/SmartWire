package jpcompany.smartwire.service.member;

import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService{

    MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Long join(Member member) {
        validateDuplicateLoginId(member);
        memberRepository.save(member);
        return member.getId();
    }

    @Override
    public void updateInfo(Member member) {
        memberRepository.update(member);
    }

    private void validateDuplicateLoginId(Member member) {
        memberRepository.findByLoginId(member.getLoginId())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }


    public Optional<Member> findOne(String memberId) {
        return memberRepository.findByLoginId(memberId);
    }

    public Optional<Member> findOne(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    public void changePassword(Member member) {

    }
}
