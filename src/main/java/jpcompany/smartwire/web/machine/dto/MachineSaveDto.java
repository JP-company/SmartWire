package jpcompany.smartwire.web.machine.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class MachineSaveDto {

    @Max(15)
    @NotEmpty
    private String machineName;
    @Max(10)
    private String machineModel;
    private String dateManufacture;
}
