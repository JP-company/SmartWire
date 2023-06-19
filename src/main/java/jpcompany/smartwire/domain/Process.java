package jpcompany.smartwire.domain;

import java.time.LocalTime;

public class Process {
    private long id;
    private String operationFileName;
    private LocalTime startTime;
    private LocalTime finishedTime;
    private int thickness;
    private LocalTime wholeProcessingTime;
    private LocalTime actualProcessingTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOperationFileName() {
        return operationFileName;
    }

    public void setOperationFileName(String operationFileName) {
        this.operationFileName = operationFileName;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(LocalTime finishedTime) {
        this.finishedTime = finishedTime;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public LocalTime getWholeProcessingTime() {
        return wholeProcessingTime;
    }

    public void setWholeProcessingTime(LocalTime wholeProcessingTime) {
        this.wholeProcessingTime = wholeProcessingTime;
    }

    public LocalTime getActualProcessingTime() {
        return actualProcessingTime;
    }

    public void setActualProcessingTime(LocalTime actualProcessingTime) {
        this.actualProcessingTime = actualProcessingTime;
    }
}
