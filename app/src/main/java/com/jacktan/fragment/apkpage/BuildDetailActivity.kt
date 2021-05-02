package com.jacktan.fragment.apkpage

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jacktan.ApiManager
import com.jacktan.testflight.R
import com.jacktan.TestFlightApplication
import com.jacktan.common.RingProgressView
import com.jacktan.fragment.apkpage.bean.AppBean
import com.jacktan.fragment.apkpage.bean.BuildDetailBean
import com.jacktan.fragment.apkpage.bean.JobDetailBean
import com.jacktan.utils.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File

class BuildDetailActivity : AppCompatActivity(), IView {
    private val TAG = "BuildDetailActivity"
    lateinit var recyclerView: RecyclerView
    lateinit var mAppPresenter: AppPresenter
    lateinit var mJobName: String
    lateinit var mAdapter: BuildAdapter
    lateinit var mLoadingView: RingProgressView
    var mJobDetailBean: JobDetailBean ?= null
    var mBuildList: MutableList<JobDetailBean.BuildBean> = ArrayList<JobDetailBean.BuildBean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_detail)
        recyclerView = findViewById(R.id.jobs_rv)
        mLoadingView = findViewById(R.id.loading_view)
        mJobName = intent.getStringExtra("jobName")!!

        initAppInfo()
        mAppPresenter = AppPresenter(this)
        mAppPresenter.getJobDetail(jobName = mJobName)

        mAdapter = BuildAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = mAdapter


    }

    private fun initAppInfo() {
        var jobName = intent.getStringExtra("jobName")
        var packetName = intent.getStringExtra("packetName")
        val localAppInfo = AppUtils.getAppInfo(packetName)
        var iconImg: ImageView = findViewById(R.id.app_icon_img)
        val appNameTv: TextView = findViewById(R.id.apk_name_tv)
        val appVersionTv: TextView = findViewById(R.id.version_tv)
        val appPacketTv: TextView = findViewById(R.id.packet_name_tv)
        val minSdkVersionTv: TextView = findViewById(R.id.min_sdk_tv)
        val targetSdkVersionTv: TextView = findViewById(R.id.target_sdk_tv)

        if (localAppInfo == null) {
            appNameTv.text = jobName
            appPacketTv.text = "包名: $packetName"
            appVersionTv.text = "当前版本:  未安装"
            minSdkVersionTv.text = "minSdkVersion: "
            targetSdkVersionTv.text = "targetSdkVersion: "
        } else {
            Glide.with(this).load(localAppInfo.icon).into(iconImg)
            appNameTv.text = localAppInfo.name
            appPacketTv.text = "包名: "+localAppInfo.packageName
            appVersionTv.text = "当前版本:  ${localAppInfo.versionName}:(${localAppInfo.versionCode})"
            minSdkVersionTv.text = "minSdkVersion: " +localAppInfo.minSdkVersion.toString()
            targetSdkVersionTv.text = "targetSdkVersion: "+localAppInfo.targetSdkVersion.toString()
        }

    }

    override fun onLoadSuccess(apps: MutableList<AppBean>) {
        Log.d(TAG, "onLoadSuccess: ")
    }

    override fun addApp(appBean: AppBean) {
        mJobDetailBean = appBean.jobDetailBean
        mJobDetailBean?.builds?.let { mAdapter.setData(it) }
    }

    override fun fetchError(msg: String, code: Int) {
    }

    inner class BuildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jobNameTv : TextView = itemView.findViewById(R.id.build_name)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.build_rv)
        val buildTimeTv: TextView = itemView.findViewById(R.id.build_time_tv)
        val buildErrorTv: TextView = itemView.findViewById(R.id.build_error_tv)
    }

    inner class BuildAdapter : RecyclerView.Adapter<BuildViewHolder>() {
        private var buildArray = mutableListOf<JobDetailBean.BuildBean>()

        fun setData(list: MutableList<JobDetailBean.BuildBean>) {
            buildArray.clear()
            buildArray.addAll(list)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): BuildViewHolder {
            var view = LayoutInflater.from(viewGroup.context).inflate(R.layout.build_item_layout, null)
            return BuildViewHolder(view)
        }

        override fun getItemCount(): Int {
            return buildArray.size
        }

        @SuppressLint("CheckResult")
        override fun onBindViewHolder(viewholder: BuildViewHolder, position: Int) {
            var buildBean = buildArray[position]
            viewholder.jobNameTv.text = "#"+buildBean.number.toString()
            val adapter  = ArtifactsAdapter(buildBean.number)
            viewholder.buildErrorTv.visibility = View.GONE
            viewholder.recyclerView.adapter = adapter
            viewholder.recyclerView.adapter = adapter
            viewholder.recyclerView.layoutManager = LinearLayoutManager(viewholder.itemView.context, RecyclerView.VERTICAL, false)
            if (buildBean.detailBean == null) {
                // 获取编译详情
                ApiManager.getApiService()
                    .getBuildDetail(mJobName, buildBean.number)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        buildBean.setDetailBeans(it)
                        viewholder.buildTimeTv.text = TimeUtil.getStringDate(it.timestamp)
                    }
            }
            viewholder.itemView.setOnClickListener {
                if(buildBean.detailBean.result != "SUCCESS") { //FAILURE
                    viewholder.buildErrorTv.visibility = View.VISIBLE
                } else {
                    viewholder.buildErrorTv.visibility = View.GONE
                    adapter.setData(buildBean.detailBean.artifacts)
                    if (viewholder.recyclerView.visibility == View.VISIBLE) {
                        viewholder.recyclerView.visibility = View.GONE
                    } else {
                        viewholder.recyclerView.visibility = View.VISIBLE
                    }
                }
            }
        }

    }


    inner class ArtifactsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val artifactTv = itemView.findViewById<TextView>(R.id.artifact_name)
    }

    inner class ArtifactsAdapter(number:Int) : RecyclerView.Adapter<ArtifactsViewHolder>() {
        private var artifactsArray = mutableListOf<BuildDetailBean.ArtifactsBean>()
        private var number: Int = number

        fun setData(data: MutableList<BuildDetailBean.ArtifactsBean>) {
            artifactsArray.clear()
            artifactsArray.addAll(data)
            notifyDataSetChanged()
        }
        fun clean() {
            artifactsArray.clear()
            notifyDataSetChanged()
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtifactsViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.artifact_item_layout, null)
            return ArtifactsViewHolder(view)
        }

        override fun getItemCount(): Int {
            return artifactsArray.size
        }

        override fun onBindViewHolder(holder: ArtifactsViewHolder, position: Int) {
            var artifact = artifactsArray[position]
            holder.artifactTv.text = artifact.fileName
            var isDownload = isExit(artifact.fileName)
            if (isDownload) {
                holder.artifactTv.setTextColor(Color.BLUE)
            }
            holder.itemView.setOnClickListener {
                var targetFilePath = artifact.fileName
                if (isDownload) {
                    CommonUtil.instance.installApp(this@BuildDetailActivity, targetFilePath)
                } else {
                    DownloadUtil.downloadFile(
                        mJobName,
                        number,
                        artifact.relativePath,
                        File(TestFlightApplication.apkDir,  artifact.fileName).absolutePath
                    )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<Float> {
                            override fun onComplete() {
                                holder.artifactTv.setTextColor(Color.BLUE)
                                mLoadingView.visibility = View.GONE
                                CommonUtil.instance.installApp(
                                    this@BuildDetailActivity,
                                    targetFilePath
                                )
                            }

                            override fun onSubscribe(d: Disposable) {
                            }

                            override fun onError(e: Throwable) {
                            }

                            override fun onNext(progress: Float) {
                                mLoadingView.visibility = View.VISIBLE
                                mLoadingView.currentProgress = (progress * 100).toInt()
                            }
                        })
                }
            }
        }
    }

    private fun isExit(fileName: String):Boolean {
        val file = File(TestFlightApplication.apkDir, fileName)
        return file.exists()
    }

    @SuppressLint("CheckResult")
    fun getBuildDetail(jobName: String, buildNumber: Int, adapter: ArtifactsAdapter) {
        ApiManager.getApiService().getBuildDetail(jobName, buildNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                adapter.setData(it.artifacts)
            }
    }

}