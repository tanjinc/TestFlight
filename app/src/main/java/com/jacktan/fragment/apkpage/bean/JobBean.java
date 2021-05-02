package com.jacktan.fragment.apkpage.bean;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class JobBean {

    private List<JobsBean> jobs;

    public List<JobsBean> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobsBean> jobs) {
        this.jobs = jobs;
    }

    @Keep
    public static class JobsBean {
        /**
         * _class : hudson.model.FreeStyleProject
         * name : cfwallpaper
         * url : http://10.12.128.210:8089/job/cfwallpaper/
         * color : blue
         */

        private String name;
        private String url;

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
    }
}
