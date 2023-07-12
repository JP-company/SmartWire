package jpcompany.smartwire.domain.member.join;

import groovy.util.logging.Slf4j;
import jpcompany.smartwire.web.member.service.MemberServiceJoin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class MemberJoinTest {

    @Autowired
    private final MemberServiceJoin serviceJoin;

    public MemberJoinTest(MemberServiceJoin serviceJoin) {
        this.serviceJoin = serviceJoin;
    }
}
