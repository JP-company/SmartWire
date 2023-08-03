package jpcompany.smartwire.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class Date {
    private Integer id;
    private LocalDate date;
    private Integer machineId;
}
