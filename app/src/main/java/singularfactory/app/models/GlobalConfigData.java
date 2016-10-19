package singularfactory.app.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Óscar Adae Rodríguez on 19/08/2016.
 */
public class GlobalConfigData {

    @SerializedName("api_version")
    private String apiVersion;

    @SerializedName("media_url")
    private String mediaUrl;


    /** GETTER & SETTER **/
    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }
}
