package jpcompany.smartwire.window_desktop.log_receiver.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter @ToString
public class LogSaveDto {

    private String loginId;
    private String machineName;
    private String log;
    private LocalDate date;
    private LocalTime logTime;
    private String file;
    private Integer thickness;
    private LocalTime startedTime;
    private LocalTime actualProcessTime;
    private Integer sequence;
}
