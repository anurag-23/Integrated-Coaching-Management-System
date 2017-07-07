package in.org.cris.icms.models.consistverification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anurag on 20/6/17.
 */
public class Train {
    @Expose
    @SerializedName("TrainNumber")
    private String trainNo;

    @Expose
    @SerializedName("TrainName")
    private String name;

    @Expose
    @SerializedName("Source")
    private String source;

    @Expose
    @SerializedName("Destination")
    private String destination;

    @Expose
    @SerializedName("TrainStartDate")
    private String startDate;

    @Expose
    @SerializedName("TrainArrivalTime")
    private String arrTime;

    @Expose
    @SerializedName("TrainDepartureTime")
    private String depTime;

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    public String getDepTime() {
        return depTime;
    }

    public void setDepTime(String depTime) {
        this.depTime = depTime;
    }
}
