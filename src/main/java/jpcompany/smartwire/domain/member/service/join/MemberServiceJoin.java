package jpcompany.smartwire.domain.member.service.join;

import jpcompany.smartwire.domain.member.Member;
import jpcompany.smartwire.domain.member.MemberJoinForm;


public interface MemberServiceJoin {

    Member join(MemberJoinForm memberJoinForm);
    boolean idDuplicateCheck(String loginId);
    boolean passwordDoubleCheck(String password, String passwordDoubleCheck);
}
