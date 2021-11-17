package in.org.cris.icms.models.consistverification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anurag on 13/7/17.
 */
public class Line {
    @Expose
    @SerializedName("line")
    private String line;

    @Expose
    @SerializedName("capacity")
    private int capacity;

    @Expose
    @SerializedName("coachesOnLine")
    private int coachesOnLine;

    @Expose
    @SerializedName("availableSpace")
    private int availableSpace;

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCoachesOnLine() {
        return coachesOnLine;
    }

    public void setCoachesOnLine(int coachesOnLine) {
        this.coachesOnLine = coachesOnLine;
    }

    public int getAvailableSpace() {
        return availableSpace;
    }

    public void setAvailableSpace(int availableSpace) {
        this.availableSpace = availableSpace;
    }
}
