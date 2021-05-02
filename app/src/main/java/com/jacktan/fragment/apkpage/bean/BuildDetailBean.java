package com.jacktan.fragment.apkpage.bean;


import androidx.annotation.Keep;

import java.util.List;

@Keep
public class BuildDetailBean {
    /**
     * artifacts : [{"displayPath":"cfwallpaper_v202001265_01181654.apk","fileName":"cfwallpaper_v202001265_01181654.apk","relativePath":"apks/default/cfwallpaper_v202001265_01181654.apk"},{"displayPath":"cfwallpaper_v202001265_01181654_元气桌面壁纸.apk","fileName":"cfwallpaper_v202001265_01181654_元气桌面壁纸.apk","relativePath":"apks/hw/cfwallpaper_v202001265_01181654_元气桌面壁纸.apk"},{"displayPath":"cfwallpaper_v202001265_01181654_元气壁纸正版.apk","fileName":"cfwallpaper_v202001265_01181654_元气壁纸正版.apk","relativePath":"apks/xm/cfwallpaper_v202001265_01181654_元气壁纸正版.apk"}]
     * displayName : #265
     * fullDisplayName : cfwallpaper #265
     * id : 265
     * result : SUCCESS
     * timestamp : 1610960092642
     * url : http://10.12.128.210:8089/job/cfwallpaper/265/
     */

    private String displayName;
    private String fullDisplayName;
    private String id;
    private String result;
    private long timestamp;
    private String url;
    private List<ArtifactsBean> artifacts;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFullDisplayName() {
        return fullDisplayName;
    }

    public void setFullDisplayName(String fullDisplayName) {
        this.fullDisplayName = fullDisplayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ArtifactsBean> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<ArtifactsBean> artifacts) {
        this.artifacts = artifacts;
    }

    @Keep
    public static class ArtifactsBean {
        /**
         * displayPath : cfwallpaper_v202001265_01181654.apk
         * fileName : cfwallpaper_v202001265_01181654.apk
         * relativePath : apks/default/cfwallpaper_v202001265_01181654.apk
         */

        private String displayPath;
        private String fileName;
        private String relativePath;

        public String getDisplayPath() {
            return displayPath;
        }

        public void setDisplayPath(String displayPath) {
            this.displayPath = displayPath;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getRelativePath() {
            return relativePath;
        }

        public void setRelativePath(String relativePath) {
            this.relativePath = relativePath;
        }
    }
}
