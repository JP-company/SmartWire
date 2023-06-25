package jpcompany.smartwire.repository.member;

import jpcompany.smartwire.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcTemplateMemberRepository implements MemberRepository{

    private final JdbcTemplate jdbcTemplate;
    public JdbcTemplateMemberRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("members").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();

        // 생성날짜, 업데이트날짜 생성
        LocalDateTime now = LocalDateTime.now();
        member.setCreatedDateTime(now);
        member.setUpdatedDateTime(now);

        parameters.put("login_id", member.getLoginId());
        parameters.put("login_password", member.getLoginPassword());
        parameters.put("company_name", member.getCompanyName());
        parameters.put("created_date_time", member.getCreatedDateTime());
        parameters.put("updated_date_time", member.getUpdatedDateTime());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        member.setId(key.longValue());
        return member;
    }

    @Override
    public Member update(Member member) {
        member.setUpdatedDateTime(LocalDateTime.now());
        jdbcTemplate.update("UPDATE members SET login_password = ? WHERE id = ? ", member.getLoginPassword(), member.getId());
        jdbcTemplate.update("UPDATE members SET company_name = ? WHERE id = ? ", member.getCompanyName(), member.getId());
        jdbcTemplate.update("UPDATE members SET updated_date_time = ? WHERE id = ? ", member.getUpdatedDateTime(), member.getId());
        return null;
    }

    @Override
    public Optional<Member> findById(Long id) {
        List<Member> result = jdbcTemplate.query("select * from members where id = ?", memberRowMapper(), id);
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        List<Member> result = jdbcTemplate.query("select * from members where login_id = ?", memberRowMapper(), loginId);
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return null;
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setLoginId(rs.getString("login_id"));
            member.setLoginPassword(rs.getString("login_password"));
            member.setCompanyName(rs.getString("company_name"));
            member.setCreatedDateTime(rs.getTimestamp("created_date_time").toLocalDateTime());
            member.setUpdatedDateTime(rs.getTimestamp("updated_date_time").toLocalDateTime());
            return member;
        };
    }
}
