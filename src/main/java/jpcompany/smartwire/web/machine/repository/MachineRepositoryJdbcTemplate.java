package jpcompany.smartwire.web.machine.repository;

import jpcompany.smartwire.domain.Machine;
import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.web.machine.dto.MachineDto;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
public class MachineRepositoryJdbcTemplate {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public MachineRepositoryJdbcTemplate(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("machines")
                .usingGeneratedKeyColumns("id");
    }

    public Machine save(Machine machine) {
        machine.setSelected(false);
        SqlParameterSource param = new BeanPropertySqlParameterSource(machine);
        Number key = jdbcInsert.executeAndReturnKey(param);
        machine.setId(key.intValue());
        return machine;
    }

    public void updateInformation(MachineDto machineDto) {
        String sql = "update machines " +
                "set machine_name=:machineName, machine_model=:machineModel, date_manufacture=:dateManufacture," +
                "sequence=:sequence " +
                "where id=:id";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(machineDto);
        template.update(sql, param);
    }

    public Optional<Integer> findByMemberIdNMachineName(Integer memberId, String machineName) {
        String sql = "select id from machines where member_id = :memberId and machine_name=:machineName";

        try {
            Map<String, Object> param = new ConcurrentHashMap<>();
            param.put("memberId", memberId);
            param.put("machineName", machineName);
            Integer id = template.queryForObject(sql, param, Integer.class); // 없으면 예외터짐
            return Optional.ofNullable(id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    public List<MachineDto> findAll(Integer memberId) {
        String sql = "select id, machine_name, machine_model, date_manufacture, sequence, selected " +
                "from machines " +
                "where member_id =:memberId";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("memberId", memberId);
        return template.query(sql, param, machineRowMapper());
    }

    public void delete(Integer machineId) {
        String sql = "delete from machines where id=:machineId";

        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("machineId", machineId);
        template.update(sql, param);
    }

    private RowMapper<MachineDto> machineRowMapper() {
        return BeanPropertyRowMapper.newInstance(MachineDto.class);
    }

}
