package in.org.cris.icms.models.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anurag on 5/7/17.
 */
public class LoginRequest {
    @Expose
    @SerializedName("newSession")
    private boolean newSession;

    @Expose
    @SerializedName("overwrite")
    private boolean overwrite;

    @Expose
    @SerializedName("loginTime")
    private String loginTime;

    @Expose
    @SerializedName("user")
    private User user;

    @Expose
    @SerializedName("serviceID")
    private String serviceID;

    public boolean isNewSession() {
        return newSession;
    }

    public void setNewSession(boolean newSession) {
        this.newSession = newSession;
    }

    public boolean getOverwriteFlag() {
        return overwrite;
    }

    public void setOverwriteFlag(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
}
