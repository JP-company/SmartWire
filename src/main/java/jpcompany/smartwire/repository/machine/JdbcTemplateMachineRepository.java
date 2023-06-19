package jpcompany.smartwire.repository.machine;

import jpcompany.smartwire.domain.Machine;
import jpcompany.smartwire.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcTemplateMachineRepository implements MachineRepository{

    private final JdbcTemplate jdbcTemplate;
    public JdbcTemplateMachineRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Machine save(Machine machine) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);

        // DB에서 id는 자동으로 설정 하게 함
        jdbcInsert.withTableName("machines").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("member_id", machine.getMemberId());
        parameters.put("machine_name", machine.getMachineName());
        parameters.put("machine_model", machine.getMachineModel());
        parameters.put("software_version", machine.getSoftwareVersion());
        parameters.put("date_manufacture", machine.getDateManufacture());


        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        machine.setId(key.longValue());
        return machine;
    }

    @Override
    public Machine update(Machine machine) {
        jdbcTemplate.update("UPDATE machines SET machine_name = ? WHERE id = ? ", machine.getMachineName(), machine.getId());
        jdbcTemplate.update("UPDATE machines SET machine_model = ? WHERE id = ? ", machine.getMachineModel(), machine.getId());
        jdbcTemplate.update("UPDATE machines SET software_version = ? WHERE id = ? ", machine.getSoftwareVersion(), machine.getId());
        jdbcTemplate.update("UPDATE machines SET date_manufacture = ? WHERE id = ? ", machine.getDateManufacture(), machine.getId());
        return machine;
    }

    @Override
    public Optional<Machine> findById(Long id) {
        List<Machine> result = jdbcTemplate.query("select * from machines where id = ?", machineRowMapper(), id);
        return result.stream().findAny();
    }

    @Override
    public List<Machine> findAllMachinesByMemberId(Long memberId) {
        return jdbcTemplate.query("select * from machines where member_id = ?", machineRowMapper(), memberId);
    }


    private RowMapper<Machine> machineRowMapper() {
        return (rs, rowNum) -> {
            Machine machine = new Machine();
            machine.setId(rs.getLong("id"));
            machine.setMemberId(rs.getLong("member_id"));
            machine.setMachineName(rs.getString("machine_name"));
            machine.setMachineModel(rs.getString("machine_model"));
            machine.setSoftwareVersion(rs.getString("software_version"));
            Timestamp timestamp = rs.getTimestamp("date_manufacture");
            if (timestamp != null) {
                machine.setDateManufacture(timestamp.toLocalDateTime().toLocalDate());
            } else {
                machine.setDateManufacture(null);
            }

            return machine;
        };
    }
}
