package jpcompany.smartwire.web.log.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@ToString
public class LogDto {
    private String log;
    private LocalDate date;
    private LocalTime logTime;
    private String file;
    private Integer thickness;
    private LocalTime startedTime;
    private Integer sequence;
    private String machineName;
}
