package jpcompany.smartwire.window_desktop.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter @ToString
public class LogDto {

    private String loginId;
    private String machineName;
    private String log;
    private LocalTime logTime;
    private LocalDate date;
    private String file;
    private Integer thickness;
    private LocalTime startedTime;
    private Integer sequence;
}
