package in.org.cris.icms.models.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anurag on 5/7/17.
 */
public class LoginResponse {
    @Expose
    @SerializedName("success")
    private boolean success;

    @Expose
    @SerializedName("responseCode")
    private int responseCode;

    @Expose
    @SerializedName("sessionActiveTime")
    private String sessionActiveTime;

    @Expose
    @SerializedName("serviceID")
    private String serviceID;

    @Expose
    @SerializedName("refreshToken")
    private String refreshToken;

    @Expose
    @SerializedName("message")
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getSessionActiveTime() {
        return sessionActiveTime;
    }

    public void setSessionActiveTime(String sessionActiveTime) {
        this.sessionActiveTime = sessionActiveTime;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}