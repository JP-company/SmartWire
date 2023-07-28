package jpcompany.smartwire.window_desktop.log_receiver.repository;

import jpcompany.smartwire.domain.Date;
import jpcompany.smartwire.domain.Log;
import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.domain.Process;
import jpcompany.smartwire.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
public class LogReceiverJdbcTemplateRepository {

    private final NamedParameterJdbcTemplate template;

    public LogReceiverJdbcTemplateRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    public Date saveDate(Date date) {
        String sql = "insert into dates(date, machine_id) values(:date, :machineId)";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("date", date.getDate())
                .addValue("machineId", date.getMachineId());
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(sql, param, keyHolder);

        Integer id = keyHolder.getKey().intValue();

        date.setId(id);

        return date;
    }

    public Process saveProcess(Process process) {
        String sql = "insert into processes(file, thickness, started_time, machine_date_id) " +
                "values(:file, :thickness, :startedTime, :machineDateId)";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("file", process.getFile())
                .addValue("thickness", process.getThickness())
                .addValue("startedTime", process.getStartedTime())
                .addValue("machineDateId", process.getMachineDateId());
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(sql, param, keyHolder);

        Integer id = keyHolder.getKey().intValue();

        process.setId(id);

        return process;
    }

    public Log saveLog(Log log) {
        String sql = "insert into logs(log, log_time, process_id, machine_date_id, machine_id) " +
                "values(:log, :logTime, :processId, :machineDateId, :machineId)";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("log", log.getLog())
                .addValue("logTime", log.getLogTime())
                .addValue("processId", log.getProcessId())
                .addValue("machineDateId", log.getMachineDateId())
                .addValue("machineId", log.getMachineId());
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(sql, param, keyHolder);

        Integer id = keyHolder.getKey().intValue();

        log.setId(id);

        return log;
    }

    public Optional<Integer> findRecentDateIdByMachineId(Integer machineId) {
        String sql = "select id from dates where machine_id = :machineId order by id desc limit 1";

        try {
            Map<String, Object> param = Map.of("machineId", machineId);
            Integer id = template.queryForObject(sql, param, Integer.class);
            return Optional.ofNullable(id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Integer> findRecentProcessIdByMachineDateId(Integer machineDateId) {
        String sql = "select id from processes where machine_date_id=:machineDateId order by id desc limit 1";
        try {
            Map<String, Object> param = new ConcurrentHashMap<>();
            param.put("machineDateId", machineDateId);
            Integer id = template.queryForObject(sql, param, Integer.class); // 없으면 예외터짐
            return Optional.ofNullable(id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<Date> DateRowMapper() {
        return BeanPropertyRowMapper.newInstance(Date.class);
    }
}
