package jpcompany.smartwire.window_desktop.repository;

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
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        String sql = "insert into processes(file, thickness, started_time, machine_date_id, machine_id) " +
                "values(:file, :thickness, :startedTime, :machineDateId, :machineId)";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("file", process.getFile())
                .addValue("thickness", process.getThickness())
                .addValue("startedTime", process.getStartedTime())
                .addValue("machineDateId", process.getMachineDateId())
                .addValue("machineId", process.getMachineId());
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

    public Optional<Date> findRecentDateByMachineId(Integer machineId) {
        String sql = "select id, date, machine_id from dates where machine_id = :machineId order by id desc limit 1";

        try {
            Map<String, Object> param = Map.of("machineId", machineId);
            Date date = template.queryForObject(sql, param, DateRowMapper());
            return Optional.ofNullable(date);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Process> findRecentProcessByMachineId(Integer machineId) {
        String sql = "select id, file, thickness, started_time, finished_date_time, actual_process_time, machine_date_id, machine_id " +
                "from processes where machine_id=:machineId order by id desc limit 1";
        try {
            Map<String, Object> param = new ConcurrentHashMap<>();
            param.put("machineId", machineId);
            Process process = template.queryForObject(sql, param, ProcessRowMapper());// 없으면 예외터짐
            return Optional.ofNullable(process);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void doneProcess(Integer processId, LocalDateTime finishedDateTime, LocalTime actualProcessTime) {
        String sql = "update processes " +
                "set finished_date_time=:finishedDateTime, actual_process_time=:actualProcessTime " +
                "where id=:processId";

        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("finishedDateTime", finishedDateTime)
                .addValue("processId", processId)
                .addValue("actualProcessTime", actualProcessTime);

        template.update(sql, param);
    }

    private RowMapper<Process> ProcessRowMapper() {
        return BeanPropertyRowMapper.newInstance(Process.class);
    }
    private RowMapper<Date> DateRowMapper() {
        return BeanPropertyRowMapper.newInstance(Date.class);
    }
}
