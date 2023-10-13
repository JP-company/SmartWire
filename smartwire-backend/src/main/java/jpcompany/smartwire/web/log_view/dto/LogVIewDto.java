package jpcompany.smartwire.web.log_view.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@ToString
public class LogVIewDto {
    private String log;
    private LocalDate date;
    private LocalTime logTime;
    private String file;
    private Integer thickness;
    private LocalTime startedTime;
    private Integer sequence;
    private String machineName;

    public LogVIewDto() {
    }

    public LogVIewDto(String machineName, Integer sequence) {
        this.machineName = machineName;
        this.sequence = sequence;
    }
}
