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
    private String dateManufacture;
    private Boolean selected;
    private Integer memberId;

    public Machine() {}

    public Machine(String machineName, String machineModel, String dateManufacture, Boolean selected, Integer memberId) {
        this.machineName = machineName;
        this.machineModel = machineModel;
        this.dateManufacture = dateManufacture;
        this.selected = selected;
        this.memberId = memberId;
    }
}
