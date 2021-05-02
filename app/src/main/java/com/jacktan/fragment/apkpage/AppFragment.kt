package com.jacktan.fragment.apkpage

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jacktan.testflight.*
import com.jacktan.fragment.BaseFragment
import com.jacktan.fragment.apkpage.bean.AppBean
import com.jacktan.utils.CommonUtil
import com.jacktan.utils.DownloadUtil
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import com.jacktan.ApiManager
import com.jacktan.TestFlightApplication
import kotlinx.android.synthetic.main.fragment_app_layout.*
import java.io.File

@SuppressLint("CheckResult")
class AppFragment : BaseFragment(), IView{
    private val TAG = "AppFragment"
    private val PROGRESS_BAR_MAX = 100
    private var mAdapter: MyAdapter?= null
    private var mPathArray  = mutableListOf<String>()

    private lateinit var mPresenter: AppPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_app_layout, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = AppPresenter(this)
        swipeRefreshLayout.setOnRefreshListener {
            mAdapter?.clear()
            mPresenter.fetchAllJobs()
            swipeRefreshLayout.isRefreshing = false
        }
        mPresenter.fetchAllJobs()
        mAdapter = MyAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = mAdapter
    }

    override fun getName(): String {
        return "APP"
    }

    override fun onBackPress(): Boolean {
        return false
    }

    override fun onResume() {
        super.onResume()
    }


    private fun cleanAppData(packageName: String) {
        val packageURI = Uri.parse("package:$packageName")
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI))
    }

    private fun openApp(packageName: String) {
        activity?.startActivity(context?.packageManager?.getLaunchIntentForPackage(packageName))
    }

    fun downloadApp(viewholder: AppViewHolder, appBean: AppBean) {
        val jobDetailBean = appBean.jobDetailBean
        var targetFileName: String = ""
        ApiManager.getApiService()
            .getBuildDetail(jobDetailBean.name, jobDetailBean.lastSuccessfulBuild.number)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                if (it.artifacts.size > 0) {
                    it.artifacts[0]
                }
                targetFileName = it.artifacts[0].fileName
                DownloadUtil.downloadFile(jobDetailBean.name,
                    jobDetailBean.lastSuccessfulBuild.number,
                    it.artifacts[0].relativePath,
                    File(TestFlightApplication.apkDir, it.artifacts[0].fileName).absolutePath)
            }
            .subscribe(object: Observer<Float> {
                override fun onComplete() {
                    toast("下载成功")
                    mAdapter?.notifyDataSetChanged()
                    viewholder.processView.visibility = View.GONE
                    CommonUtil.instance.installApp(context, targetFileName)
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    toast("download onFail ${e.message}")
                }

                override fun onNext(progress: Float) {
                    viewholder.appNameTv.setTextColor(Color.BLUE)
                    viewholder.setStatus(AppViewHolder.STATUS_DOWNLOADING)
                    viewholder.processView.currentProgress = (progress * PROGRESS_BAR_MAX).toInt()
                }
            })
    }

    inner class MyAdapter : RecyclerView.Adapter<AppViewHolder>() {
        private var appArray = mutableListOf<AppBean>()

        fun addData(bean: AppBean) {
            appArray.add(bean)
            appArray.sortBy { appBean -> appBean.name }
            notifyDataSetChanged()
        }

        fun clear() {
            appArray.clear()
            notifyDataSetChanged()
        }


        fun setData(list: MutableList<AppBean>){
            appArray = list
            notifyDataSetChanged()
        }



        override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): AppViewHolder {
            var view = LayoutInflater.from(viewGroup.context).inflate(R.layout.app_item_layout, null)
            return AppViewHolder(view)
        }

        override fun getItemCount(): Int {
            return appArray.size
        }

        override fun onBindViewHolder(viewholder: AppViewHolder, position: Int) {
            var appBean = appArray[position]
            var appInfo = appBean.appInfo
            val jobDetailBean = appBean.jobDetailBean
            if (appInfo != null) {
                Glide.with(this@AppFragment).load(appInfo.icon).into(viewholder.appIconImg)
                viewholder.appNameTv.text = appInfo.name
                viewholder.appPacketNameTv.text = "包名: ${appInfo.packageName}"
                viewholder.appVersionTv.text = "当前版本: ${appInfo.versionName} (${appInfo.versionCode})"
                viewholder.jobNumTv.text = "最新版本: ${appBean.jobDetailBean?.lastSuccessfulBuild?.number}"
                viewholder.cleanBtn.visibility = View.VISIBLE
                viewholder.cleanBtn.setOnClickListener {
                    cleanAppData(appInfo?.packageName)
                }
                viewholder.appIconImg.setOnClickListener {
                    openApp(appInfo.packageName)
                }
                viewholder.itemView.setOnClickListener {
                    val intent = Intent(context, BuildDetailActivity::class.java)
                    intent.putExtra("jobName", appBean.jobDetailBean.name)
                    intent.putExtra("packetName", appBean.appInfo?.packageName)
                    startActivity(intent)
                }
                // 没有版本更新
                if (isSameVersion(appBean.jobDetailBean.lastSuccessfulBuild.number.toString(), appInfo.versionCode.toString())){
                    viewholder.openBtn.visibility = View.GONE
                } else {
                    viewholder.openBtn.visibility = View.VISIBLE
                    viewholder.openBtn.text = "更新"
                    viewholder.jobNumTv.setTextColor(Color.RED)
                }

            } else {
                viewholder.appNameTv.text = appBean.name
                Glide.with(this@AppFragment).load(resources.getDrawable(R.mipmap.ic_launcher)).into(viewholder.appIconImg)
                viewholder.appPacketNameTv.text = "包名: ${appBean.desc}"
                viewholder.appVersionTv.text = "当前版本: 未安装"
                viewholder.jobNumTv.text = "最新版本: ${appBean.jobDetailBean.lastSuccessfulBuild?.number}"
                viewholder.cleanBtn.visibility = View.GONE
                viewholder.openBtn.visibility = View.VISIBLE

                viewholder.itemView.setOnClickListener {
                    val intent = Intent(context, BuildDetailActivity::class.java)
                    intent.putExtra("jobName", appBean.jobDetailBean.name)
                    intent.putExtra("packetName",appBean.desc)
                    startActivity(intent)
                }
            }
            viewholder.openBtn.setOnClickListener {
                viewholder.openBtn.visibility = View.GONE
                viewholder.processView.visibility = View.VISIBLE
                downloadApp(viewholder, appBean)
            }

            viewholder.processView.visibility = View.GONE
            viewholder.processView.maxProgress = PROGRESS_BAR_MAX
        }

    }

    private fun isSameVersion(lastBuildNumber: String, versionCode: String):Boolean {
        val currentCode = versionCode.substring(versionCode.length - lastBuildNumber.length)
        return currentCode == lastBuildNumber
    }

    override fun onLoadSuccess(apps: MutableList<AppBean>) {
        mAdapter?.setData(apps)
    }


    override fun addApp(appBean: AppBean) {
        mAdapter?.addData(appBean)
    }

    override fun fetchError(msg: String, code: Int) {
        toast(msg)
    }
}