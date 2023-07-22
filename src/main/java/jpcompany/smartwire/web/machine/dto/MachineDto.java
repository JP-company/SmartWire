package jpcompany.smartwire.web.machine.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class MachineDto {
    private Integer id;
    @NotEmpty
    @Size(max = 15)
    private String machineName;
    @Size(max = 15)
    private String machineModel;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateManufacture;
    @NotNull
    private Integer sequence;
    public MachineDto() {
    }
}
