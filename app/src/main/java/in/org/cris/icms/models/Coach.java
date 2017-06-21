package in.org.cris.icms.models;

/**
 * Created by anurag on 20/6/17.
 */
public class Coach {
    private String serialNo;
    private String ownRly;
    private String coachType;
    private String coachNo;
    private String from;
    private String to;
    private String prsID;
    private String remark;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getOwnRly() {
        return ownRly;
    }

    public void setOwnRly(String ownRly) {
        this.ownRly = ownRly;
    }

    public String getCoachType() {
        return coachType;
    }

    public void setCoachType(String coachType) {
        this.coachType = coachType;
    }

    public String getCoachNo() {
        return coachNo;
    }

    public void setCoachNo(String coachNo) {
        this.coachNo = coachNo;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPrsID() {
        return prsID;
    }

    public void setPrsID(String prsID) {
        this.prsID = prsID;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
