package in.org.cris.icms.models;

import java.util.List;

/**
 * Created by anurag on 22/6/17.
 */
public class LineType {
    private String type;
    private List<String> linesList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getLinesList() {
        return linesList;
    }

    public void setLinesList(List<String> linesList) {
        this.linesList = linesList;
    }
}
