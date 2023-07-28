package jpcompany.smartwire.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter @Setter @ToString
public class Process {
    private Integer id;
    private String file;
    private Integer thickness;
    private LocalTime startedTime;
    private LocalDateTime finishedTime;
    private LocalTime actualProcessTime;
    private Integer machineDateId;
}
