package com.jacktan.fragment.apkpage

import android.annotation.SuppressLint
import android.util.Log
import com.jacktan.ApiManager
import com.jacktan.fragment.apkpage.bean.AppBean
import com.jacktan.fragment.apkpage.bean.BuildDetailBean
import com.jacktan.fragment.apkpage.bean.JobBean
import com.jacktan.fragment.apkpage.bean.JobDetailBean
import com.jacktan.utils.AppUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class AppPresenter(view: IView) {
    private val TAG = "AppPresenter"
    private var view: IView?=null
    init {
        this.view = view
    }


    fun getJobDetail(jobName: String) {
        ApiManager.getApiService()
            .getJobDetail(jobName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter{t: JobDetailBean ->
                // 过滤掉没有备注#的job
                !t.description.isNullOrEmpty() && t.description.contains("#")
            }
            .subscribe (object : Observer<JobDetailBean> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(jobDetailBean: JobDetailBean) {
                    Log.d(TAG, "getJobDetail: " + jobDetailBean.description)
                    val descArray = jobDetailBean.description.split("#")
                    val descStr = descArray[0];
                    val packageName = descArray[1]
                    val localAppInfo = AppUtils.getAppInfo(packageName)
                    val bean = AppBean(
                        name = descStr.trim(),
                        desc = descStr.trim(),
                        appInfo = localAppInfo,
                        jobDetailBean = jobDetailBean
                    )
                    view?.addApp(bean);
                }

                override fun onError(e: Throwable) {
                }
            })
    }

    fun getBuildDetail(jobName: String, buildNumber: Int): Observable<BuildDetailBean> {
        return ApiManager.getApiService().getBuildDetail(jobName, buildNumber)
    }


    fun fetchAllJobs() {
        ApiManager.getApiService().getAllJobs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<JobBean> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(jobBean: JobBean) {
                    jobBean?.jobs.forEach {
                        getJobDetail(it.name)
                    }
                }

                override fun onError(t: Throwable) {
                    t.message?.let { view?.fetchError(it, 0) }
                }
            })
    }
}