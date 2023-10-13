package jpcompany.smartwire.web.machine.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
@ToString
public class MachineDtoList {

    @Valid
    private List<MachineDto> machines;

    public MachineDtoList(List<MachineDto> machines) {
        this.machines = machines;
    }
}
