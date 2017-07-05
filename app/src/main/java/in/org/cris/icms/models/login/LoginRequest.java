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
    @SerializedName("loginTime")
    private String loginTime;

    @Expose
    @SerializedName("user")
    private User user;

    public boolean isNewSession() {
        return newSession;
    }

    public void setNewSession(boolean newSession) {
        this.newSession = newSession;
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
}
