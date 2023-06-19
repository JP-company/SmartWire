package jpcompany.smartwire.controller.machine;

import jpcompany.smartwire.domain.Machine;

import java.util.List;

public class MachineList {
    private List<Machine> machines;

    public List<Machine> getMachines() {
        return machines;
    }

    public void setMachines(List<Machine> machines) {
        this.machines = machines;
    }

    @Override
    public String toString() {
        return "MachineList{" +
                "machines=" + machines +
                '}';
    }
}
