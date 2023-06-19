package jpcompany.smartwire.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Machine {

    private long id;
    private long memberId;
    private String machineName;
    private String machineModel;
    private String softwareVersion;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateManufacture;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public LocalDate getDateManufacture() {
        return dateManufacture;
    }

    public void setDateManufacture(LocalDate dateManufacture) {
        this.dateManufacture = dateManufacture;
    }

    public String getMachineModel() {
        return machineModel;
    }

    public void setMachineModel(String machineModel) {
        this.machineModel = machineModel;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", machineName='" + machineName + '\'' +
                ", machineModel='" + machineModel + '\'' +
                ", softwareVersion='" + softwareVersion + '\'' +
                ", dateManufacture=" + dateManufacture +
                '}';
    }
}
