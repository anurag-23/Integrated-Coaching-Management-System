package in.org.cris.icms.models.consistverification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by anurag on 7/7/17.
 */
public class Trains {
    @Expose
    @SerializedName("alertMsg")
    private String alertMessage;

    @Expose
    @SerializedName("Trains")
    private List<Train> trains;

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public List<Train> getTrains() {
        return trains;
    }

    public void setTrains(List<Train> trains) {
        this.trains = trains;
    }
}
