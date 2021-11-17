package in.org.cris.icms.models;

import java.util.List;

/**
 * Created by anurag on 22/6/17.
 */
public class LineType {
    private String lineType;
    private List<String> linesList;

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public List<String> getLinesList() {
        return linesList;
    }

    public void setLinesList(List<String> linesList) {
        this.linesList = linesList;
    }
}
