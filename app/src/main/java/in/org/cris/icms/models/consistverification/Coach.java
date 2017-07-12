package in.org.cris.icms.models.consistverification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anurag on 20/6/17.
 */
public class Coach {
    @Expose
    @SerializedName("srNo")
    private String serialNo;

    @Expose
    @SerializedName("cOwnRly")
    private String ownRly;

    @Expose
    @SerializedName("cType")
    private String coachType;

    @Expose
    @SerializedName("cNo")
    private String coachNo;

    @Expose
    @SerializedName("cFrom")
    private String from;

    @Expose
    @SerializedName("cTo")
    private String to;

    @Expose
    @SerializedName("prsId")
    private String prsID;

    @Expose
    @SerializedName("remark")
    private String remark;

    @Expose
    @SerializedName("attachTo")
    private String attachTo;

    @Expose
    @SerializedName("transferStn")
    private String transferStation;

    @Expose
    @SerializedName("posEng")
    private String posEng;

    @Expose
    @SerializedName("acFlag")
    private String acFlag;

    @Expose
    @SerializedName("cStatus")
    private String coachStatus;

    @Expose
    @SerializedName("cGauge")
    private String gauge;

    @Expose
    @SerializedName("lastEventReason")
    private String lastEventReason;

    @Expose
    @SerializedName("detachFrom")
    private String detachFrom;

    @Expose
    @SerializedName("secFlag")
    private String secFlag;

    @Expose
    @SerializedName("cId")
    private int coachID;

    private boolean dispute;

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

    public String getAttachTo() {
        return attachTo;
    }

    public void setAttachTo(String attachTo) {
        this.attachTo = attachTo;
    }

    public String getTransferStation() {
        return transferStation;
    }

    public void setTransferStation(String transferStation) {
        this.transferStation = transferStation;
    }

    public String getPosEng() {
        return posEng;
    }

    public void setPosEng(String posEng) {
        this.posEng = posEng;
    }

    public String getAcFlag() {
        return acFlag;
    }

    public void setAcFlag(String acFlag) {
        this.acFlag = acFlag;
    }

    public String getCoachStatus() {
        return coachStatus;
    }

    public void setCoachStatus(String coachStatus) {
        this.coachStatus = coachStatus;
    }

    public String getGauge() {
        return gauge;
    }

    public void setGauge(String gauge) {
        this.gauge = gauge;
    }

    public String getLastEventReason() {
        return lastEventReason;
    }

    public void setLastEventReason(String lastEventReason) {
        this.lastEventReason = lastEventReason;
    }

    public String getDetachFrom() {
        return detachFrom;
    }

    public void setDetachFrom(String detachFrom) {
        this.detachFrom = detachFrom;
    }

    public String getSecFlag() {
        return secFlag;
    }

    public void setSecFlag(String secFlag) {
        this.secFlag = secFlag;
    }

    public int getCoachID() {
        return coachID;
    }

    public void setCoachID(int coachID) {
        this.coachID = coachID;
    }

    public boolean isDispute() {
        return dispute;
    }

    public void setDispute(boolean dispute) {
        this.dispute = dispute;
    }
}
