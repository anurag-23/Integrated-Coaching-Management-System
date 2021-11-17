package in.org.cris.icms.models.consistverification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by anurag on 11/7/17.
 */
public class Consist {
    @Expose
    @SerializedName("TrainNumber")
    private String trainNo;

    @Expose
    @SerializedName("LastEventTime")
    private String lastEventTime;

    @Expose
    @SerializedName("alertMsg")
    private String alertMessage;

    @Expose
    @SerializedName("ArrivalDate")
    private String arrDate;

    @Expose
    @SerializedName("CoachList")
    private List<Coach> coachList;

    @Expose
    @SerializedName("LineTypeList")
    private List<LineType> lineTypeList;

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public String getLastEventTime() {
        return lastEventTime;
    }

    public void setLastEventTime(String lastEventTime) {
        this.lastEventTime = lastEventTime;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public String getArrDate() {
        return arrDate;
    }

    public void setArrDate(String arrDate) {
        this.arrDate = arrDate;
    }

    public List<Coach> getCoachList() {
        return coachList;
    }

    public void setCoachList(List<Coach> coachList) {
        this.coachList = coachList;
    }

    public List<LineType> getLineTypeList() {
        return lineTypeList;
    }

    public void setLineTypeList(List<LineType> lineTypeList) {
        this.lineTypeList = lineTypeList;
    }
}
