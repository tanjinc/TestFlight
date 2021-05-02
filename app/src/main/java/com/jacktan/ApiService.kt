package com.jacktan

import com.jacktan.fragment.apkpage.bean.BuildDetailBean
import com.jacktan.fragment.apkpage.bean.JobBean
import com.jacktan.fragment.apkpage.bean.JobDetailBean
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*


interface ApiService {

    /**
     * 所以job列表
     */
    @GET("/api/json")
    fun getAllJobs(): Observable<JobBean>

    /**
     * JOB 详情
     */
    @GET("/job/{jobName}/api/json")
    fun getJobDetail(@Path("jobName") jobName: String): Observable<JobDetailBean>

    /**
     * 编译详情
     */
    @GET("/job/{jobName}/{buildNumber}/api/json")
    fun getBuildDetail(@Path("jobName") jobName: String,
                       @Path("buildNumber") buildNumber: Int): Observable<BuildDetailBean>

    /**
     * 下载apk
     */
    @Streaming
    @GET("/job/{jobName}/{buildNumber}/artifact/{relativePath}")
    fun getApkFile(@Path("jobName") jobName: String,
               @Path("buildNumber") buildNumber: Int,
               @Path("relativePath") relativePath: String ): Observable<ResponseBody>

    @Streaming
    @GET
    fun downloadFile(@Url fileUrl: String): Observable<ResponseBody>
}