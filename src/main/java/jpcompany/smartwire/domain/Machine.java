package jpcompany.smartwire.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;


@Getter
@Setter
@ToString
public class Machine {
    private Integer id;
    private String machineName;
    private String machineModel;
    private LocalDate dateManufacture;
    private Integer sequence;
    private Boolean selected;
    private Integer memberId;

    public Machine() {}

    public Machine(String machineName, String machineModel, LocalDate dateManufacture, Integer sequence, Boolean selected, Integer memberId) {
        this.machineName = machineName;
        this.machineModel = machineModel;
        this.dateManufacture = dateManufacture;
        this.sequence = sequence;
        this.selected = selected;
        this.memberId = memberId;
    }
}
