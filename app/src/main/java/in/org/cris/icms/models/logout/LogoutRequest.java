package in.org.cris.icms.models.logout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anurag on 6/7/17.
 */
public class LogoutRequest {
    @Expose
    @SerializedName("username")
    private String username;

    @Expose
    @SerializedName("serviceID")
    private String serviceID;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
}
