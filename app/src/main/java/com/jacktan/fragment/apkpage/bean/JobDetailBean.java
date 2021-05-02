package com.jacktan.fragment.apkpage.bean;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class JobDetailBean {
    private String description;
    private String displayName;
    private String name;
    private String url;
    private BuildBean firstBuild;
    private BuildBean lastSuccessfulBuild;
    private int nextBuildNumber;
    private List<BuildBean> builds;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BuildBean getFirstBuild() {
        return firstBuild;
    }

    public void setFirstBuild(BuildBean firstBuild) {
        this.firstBuild = firstBuild;
    }

    public BuildBean getLastSuccessfulBuild() {
        return lastSuccessfulBuild;
    }

    public void setLastSuccessfulBuild(BuildBean lastSuccessfulBuild) {
        this.lastSuccessfulBuild = lastSuccessfulBuild;
    }

    public int getNextBuildNumber() {
        return nextBuildNumber;
    }

    public void setNextBuildNumber(int nextBuildNumber) {
        this.nextBuildNumber = nextBuildNumber;
    }

    public List<BuildBean> getBuilds() {
        return builds;
    }

    public void setBuilds(List<BuildBean> builds) {
        this.builds = builds;
    }


    @Keep
    public static class BuildBean {
        /**
         * number : 265
         * url : http://10.12.128.210:8089/job/cfwallpaper/265/
         */

        private int number;
        private String url;
        private BuildDetailBean detailBean;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public BuildDetailBean getDetailBean() {
            return detailBean;
        }

        public void setDetailBeans(BuildDetailBean detailBean) {
            this.detailBean = detailBean;
        }
    }
}
