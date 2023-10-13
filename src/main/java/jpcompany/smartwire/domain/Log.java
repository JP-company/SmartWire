package jpcompany.smartwire.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@Setter
@ToString
public class Log {
    private Integer id;
    private String log;
    private LocalTime logTime;
    private Integer processId;
    private Integer machineDateId;
    private Integer machineId;
}
