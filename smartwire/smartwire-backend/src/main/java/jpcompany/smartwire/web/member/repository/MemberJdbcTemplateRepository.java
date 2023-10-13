package jpcompany.smartwire.web.member.repository;

import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.web.member.dto.MemberJoinDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class MemberJdbcTemplateRepository implements MemberRepository{

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberJdbcTemplateRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("members")
                .usingGeneratedKeyColumns("id")
                .usingColumns("login_id", "login_password", "email", "company_name", "phone_number", "term_of_use", "auth_token", "created_date_time", "updated_date_time");
    }

    @Override
    public void save(MemberJoinDto memberJoinDto) {
        // TODO - MySQL 한국 시간 설정
        updateDateTime(memberJoinDto);
        SqlParameterSource param = new BeanPropertySqlParameterSource(memberJoinDto);
        jdbcInsert.execute(param);
    }

    @Override
    public Member update(Member member) {
        String sql = "update members " +
                "set company_name=:companyName, phone_number=:phoneNumber, updated_date_time=:updatedDateTime " +
                "where login_id=:loginId";

        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("companyName", member.getCompanyName())
                .addValue("phoneNumber", member.getPhoneNumber())
                .addValue("updatedDateTime", LocalDateTime.now().withNano(0))
                .addValue("loginId", member.getLoginId());
        template.update(sql, param);

        return findByLoginId(member.getLoginId()).get();
    }

    @Override
    public Optional<Member> findById(Integer id) {
        String sql = "select id, login_id, login_password, email, company_name, phone_number, role, term_of_use, " +
                "auth_token, created_date_time, updated_date_time " +
                "from members where id = :id";

        try {
            Map<String, Object> param = Map.of("id", id);
            Member member = template.queryForObject(sql, param, MemberRowMapper());  // 없으면 예외터짐
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        String sql = "select id, login_id, login_password, email, company_name, phone_number, role, term_of_use, " +
                "auth_token, created_date_time, updated_date_time, have_machine " +
                "from members where login_id = :loginId";

        try {
            Map<String, Object> param = Map.of("loginId", loginId);
            Member member = template.queryForObject(sql, param, MemberRowMapper());  // 없으면 예외터짐
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByAuthToken(String token) {
        String sql = "select auth_token from members where auth_token = :token";

        try {
            Map<String, Object> param = Map.of("token", token);
            Member member = template.queryForObject(sql, param, MemberRowMapper());  // 없으면 예외터짐
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    @Override
    public void updateAuthTokenEmail(String loginId, String authToken, String email) {
        String sql = "update members " +
                "set auth_token=:authToken, email=:email " +
                "where login_id=:loginId";

        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("authToken", authToken)
                .addValue("email", email)
                .addValue("loginId", loginId);

        template.update(sql, param);
    }

    @Override
    public void setEmailVerified(String token) {
        String sql = "update members set role='ROLE_MEMBER' where auth_token=:token";

        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("token", token);

        template.update(sql, param);
    }

    public void updateHaveMachine(Integer memberId, Boolean bool) {
        String sql = "update members set have_machine=:bool where id=:memberId";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("bool", bool);

        template.update(sql, param);
    }

    private void updateDateTime(MemberJoinDto memberJoinDto) {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        memberJoinDto.setCreatedDateTime(now);
        memberJoinDto.setUpdatedDateTime(now);
    }

    private RowMapper<Member> MemberRowMapper() {
        return BeanPropertyRowMapper.newInstance(Member.class);
    }
}
