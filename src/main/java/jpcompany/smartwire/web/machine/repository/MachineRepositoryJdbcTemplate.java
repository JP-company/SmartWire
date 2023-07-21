package jpcompany.smartwire.web.machine.repository;

import jpcompany.smartwire.domain.Machine;
import jpcompany.smartwire.web.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
@Slf4j
public class MachineRepositoryJdbcTemplate {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;
    private final MemberRepository memberRepository;

    public MachineRepositoryJdbcTemplate(DataSource dataSource, MemberRepository memberRepository) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("machines")
                .usingGeneratedKeyColumns("id");
        this.memberRepository = memberRepository;
    }

    public Machine save(Machine machine) {
        machine.setSelected(false);
        SqlParameterSource param = new BeanPropertySqlParameterSource(machine);
        Number key = jdbcInsert.executeAndReturnKey(param);
        machine.setId(key.intValue());
        return machine;
    }

}
