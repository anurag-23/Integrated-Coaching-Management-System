package in.org.cris.icms.models;

/**
 * Created by anurag on 23/6/17.
 */
public class Rake {
    private int trainNo;
    private String trainName;
    private String startDate;
    private String status;
    private String lastEventTime;

    public int getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(int trainNo) {
        this.trainNo = trainNo;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getLastEventTime() {
        return lastEventTime;
    }

    public void setLastEventTime(String lastEventTime) {
        this.lastEventTime = lastEventTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
