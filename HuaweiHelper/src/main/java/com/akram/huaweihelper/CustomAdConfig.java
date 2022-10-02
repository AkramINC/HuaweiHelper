package com.akram.huaweihelper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomAdConfig {

    @SerializedName("hw_app_id")
    @Expose
    private String hwAppId;
    @SerializedName("hw_native_id")
    @Expose
    private String hwNativeId;
    @SerializedName("hw_inter_id")
    @Expose
    private String hwInterId;

    @SerializedName("hw_rewarded_ad_id")
    @Expose
    private String hwRewardedAdId;

    @SerializedName("hw_splash_ad_id")
    @Expose
    private String hwSplashAdId;
    @SerializedName("hw_inter_count")
    @Expose
    private int hwInterCount;

    public String getHwRewardedAdId() {
        return hwRewardedAdId;
    }

    public void setHwRewardedAdId(String hwRewardedAdId) {
        this.hwRewardedAdId = hwRewardedAdId;
    }

    public int getHwInterCount() {
        return hwInterCount;
    }

    public void setHwInterCount(int hwInterCount) {
        this.hwInterCount = hwInterCount;
    }

    public String getHwAppId() {
        return hwAppId;
    }

    public void setHwAppId(String hwAppId) {
        this.hwAppId = hwAppId;
    }

    public String getHwNativeId() {
        return hwNativeId;
    }

    public void setHwNativeId(String hwNativeId) {
        this.hwNativeId = hwNativeId;
    }

    public String getHwInterId() {
        return hwInterId;
    }

    public void setHwInterId(String hwInterId) {
        this.hwInterId = hwInterId;
    }

    public String getHwSplashAdId() {
        return hwSplashAdId;
    }

    public void setHwSplashAdId(String hwSplashAdId) {
        this.hwSplashAdId = hwSplashAdId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(CustomAdConfig.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("hwAppId");
        sb.append('=');
        sb.append(((this.hwAppId == null)?"<null>":this.hwAppId));
        sb.append(',');
        sb.append("hwNativeId");
        sb.append('=');
        sb.append(((this.hwNativeId == null)?"<null>":this.hwNativeId));
        sb.append(',');
        sb.append("hwInterId");
        sb.append('=');
        sb.append(((this.hwInterId == null)?"<null>":this.hwInterId));
        sb.append(',');
        sb.append("hwSplashAdId");
        sb.append('=');
        sb.append(((this.hwSplashAdId == null)?"<null>":this.hwSplashAdId));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
