package in.org.cris.icms.models;

/**
 * Created by anurag on 20/6/17.
 */
public class Coach {
    private int serialNo;
    private String ownRly;
    private String coachType;
    private String coachNo;
    private String from;
    private String to;
    private String prsID;
    private String remark;
    private String baseDepot;
    private String builtYear;
    private String pohYear;
    private String pohMonth;

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
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

    public String getBaseDepot() {
        return baseDepot;
    }

    public void setBaseDepot(String baseDepot) {
        this.baseDepot = baseDepot;
    }

    public String getBuiltYear() {
        return builtYear;
    }

    public void setBuiltYear(String builtYear) {
        this.builtYear = builtYear;
    }

    public String getPohYear() {
        return pohYear;
    }

    public void setPohYear(String pohYear) {
        this.pohYear = pohYear;
    }

    public String getPohMonth() {
        return pohMonth;
    }

    public void setPohMonth(String pohMonth) {
        this.pohMonth = pohMonth;
    }
}
