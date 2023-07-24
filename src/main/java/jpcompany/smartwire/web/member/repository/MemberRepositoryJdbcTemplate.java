package jpcompany.smartwire.web.member.repository;

import jpcompany.smartwire.domain.Member;
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
public class MemberRepositoryJdbcTemplate implements MemberRepository{

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberRepositoryJdbcTemplate(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("members")
                .usingGeneratedKeyColumns("id");
//                .usingColumns("item_name", "price", "quantity"); // 생략가능
    }

    @Override
    public Member save(Member member) {
        updateDateTime(member);
        SqlParameterSource param = new BeanPropertySqlParameterSource(member);
        Number key = jdbcInsert.executeAndReturnKey(param);
        member.setId(key.intValue());
        return member;
    }

    @Override
    public Member update(Member member) {
        String sql = "update members " +
                "set company_name=:companyName, phone_number=:phoneNumber, updated_date_time=:updatedDateTime " +
                "where login_id=:loginId";

        updateDateTime(member);
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("companyName", member.getCompanyName())
                .addValue("phoneNumber", member.getPhoneNumber())
                .addValue("updatedDateTime", member.getUpdatedDateTime())
                .addValue("loginId", member.getLoginId());
        template.update(sql, param);

        return findByLoginId(member.getLoginId()).get();
    }

    @Override
    public Optional<Member> findById(Integer id) {
        String sql = "select id, login_id, login_password, email, company_name, phone_number, term_of_use, " +
                "email_verified, auth_code, created_date_time, updated_date_time " +
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
        String sql = "select id, login_id, login_password, email, company_name, phone_number, term_of_use, " +
                "email_verified, auth_code, created_date_time, updated_date_time, have_machine" +
                " from members where login_id = :loginId";

        try {
            Map<String, Object> param = Map.of("loginId", loginId);
            Member member = template.queryForObject(sql, param, MemberRowMapper());  // 없으면 예외터짐
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    @Override
    public void updateAuthCodeEmail(String loginId, String AuthCode, String email) {
        String sql = "update members " +
                "set auth_code=:authCode, email=:email " +
                "where login_id=:loginId";

        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("authCode", AuthCode)
                .addValue("email", email)
                .addValue("loginId", loginId);

        template.update(sql, param);
    }

    @Override
    public void setEmailVerified(String loginId) {
        String sql = "update members set email_verified=true where login_id=:loginId";

        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("loginId", loginId);

        template.update(sql, param);
    }

    public void updateHaveMachine(Integer memberId, Boolean bool) {
        String sql = "update members set have_machine=:bool where id=:memberId";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("bool", bool);

        template.update(sql, param);
    }

    private void updateDateTime(Member member) {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        if (member.getCreatedDateTime() == null) {
            member.setCreatedDateTime(now);
        }
        member.setUpdatedDateTime(now);
    }

    private RowMapper<Member> MemberRowMapper() {
        return BeanPropertyRowMapper.newInstance(Member.class);
    }
}
