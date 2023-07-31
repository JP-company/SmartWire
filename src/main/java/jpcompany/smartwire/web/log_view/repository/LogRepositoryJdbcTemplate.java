package jpcompany.smartwire.web.log_view.repository;

import jpcompany.smartwire.web.log_view.dto.LogVIewDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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

    public List<LogVIewDto> getRecentLogAtEachMachine(List<Integer>  machineIdList) {
        log.info("기계 아이디 리스트 리포={}", machineIdList);

        String sql = "SELECT log, log_time, " +
                "CASE WHEN processes.finished_date_time IS NOT NULL THEN NULL ELSE processes.file END AS file, " +
                "processes.thickness, processes.started_time, dates.date, machines.sequence, machines.machine_name " +
                "FROM (\n" +
                "  SELECT MAX(id) as id, machine_id\n" +
                "  FROM logs\n" +
                "  WHERE machine_id IN (:machineIdList)\n" +
                "  GROUP BY machine_id\n" +
                ") as latest_logs\n" +
                "INNER JOIN logs ON latest_logs.id = logs.id\n" +
                "LEFT OUTER JOIN processes ON logs.process_id = processes.id\n" +
                "INNER JOIN dates ON logs.machine_date_id = dates.id \n" +
                "INNER JOIN machines ON logs.machine_id = machines.id";


        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("machineIdList", machineIdList);
        return template.query(sql, param, LogRowMapper());
    }

    private RowMapper<LogVIewDto> LogRowMapper() {
        return BeanPropertyRowMapper.newInstance(LogVIewDto.class);
    }
}
