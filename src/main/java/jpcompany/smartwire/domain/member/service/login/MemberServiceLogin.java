package jpcompany.smartwire.domain.member.service.login;

import jpcompany.smartwire.domain.member.Member;

public interface MemberServiceLogin {
    Member login(String id, String password);
}
