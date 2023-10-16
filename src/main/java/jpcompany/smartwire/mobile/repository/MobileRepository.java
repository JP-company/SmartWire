package jpcompany.smartwire.mobile.repository;

import jpcompany.smartwire.mobile.dto.FcmTokenAndAlarmSettingDto;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class MobileRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public MobileRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("fcmtokens")
                .usingGeneratedKeyColumns("id")
                .usingColumns("fcm_token", "member_id");
    }

    public void saveFcmTokenAndAlarmSetting(FcmTokenAndAlarmSettingDto fcmTokenAndAlarmSettingDto) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(fcmTokenAndAlarmSettingDto);
        jdbcInsert.execute(param);
    }
}
