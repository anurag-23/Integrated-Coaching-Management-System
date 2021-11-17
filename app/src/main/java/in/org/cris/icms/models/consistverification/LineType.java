package in.org.cris.icms.models.consistverification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by anurag on 13/7/17.
 */
public class LineType{
    @Expose
    @SerializedName("lineType")
    private String lineType;

    @Expose
    @SerializedName("lines")
    private List<Line> linesList;

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public List<Line> getLinesList() {
        return linesList;
    }

    public void setLinesList(List<Line> linesList) {
        this.linesList = linesList;
    }

}
