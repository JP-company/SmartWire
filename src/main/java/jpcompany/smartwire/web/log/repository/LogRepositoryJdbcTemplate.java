package jpcompany.smartwire.web.log.repository;

import groovy.util.logging.Slf4j;
import jpcompany.smartwire.domain.Log;
import jpcompany.smartwire.web.log.dto.LogDto;
import jpcompany.smartwire.web.machine.dto.MachineDto;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
@Slf4j
public class LogRepositoryJdbcTemplate {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public LogRepositoryJdbcTemplate(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("machines")
                .usingGeneratedKeyColumns("id");
    }

    public List<LogDto> getRecentLogAtEachMachine(List<Integer>  memberIdList) {
        String sql = "SELECT log, log_time, processes.file, processes.thickness, processes.started_time, dates.date, machines.sequence, machines.machine_name\n" +
                "FROM (\n" +
                "  SELECT MAX(id) as id, machine_id\n" +
                "  FROM logs\n" +
                "  WHERE machine_id IN (:memberIdList)\n" +
                "  GROUP BY machine_id\n" +
                ") as latest_logs\n" +
                "INNER JOIN logs ON latest_logs.id = logs.id\n" +
                "INNER JOIN processes ON logs.process_id = processes.id\n" +
                "INNER JOIN dates ON logs.machine_date_id = dates.id \n" +
                "INNER JOIN machines ON logs.machine_id = machines.id";

        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("memberIdList", memberIdList);
        return template.query(sql, param, LogRowMapper());
    }

    private RowMapper<LogDto> LogRowMapper() {
        return BeanPropertyRowMapper.newInstance(LogDto.class);
    }
}
