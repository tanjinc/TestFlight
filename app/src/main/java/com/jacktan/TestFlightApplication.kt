package com.jacktan

import android.os.Build
import android.os.Environment
import androidx.multidex.MultiDexApplication
import com.jacktan.utils.FileUtils
import com.jacktan.utils.SharePrefUtil
import kotlinx.android.synthetic.main.about_layout.*
import java.io.File


class TestFlightApplication: MultiDexApplication() {
    private val TAG = "TestFlightApplication"
    companion object {
        lateinit var apkDir:File
    }
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            apkDir = filesDir
        } else {
            apkDir = File(Environment.getExternalStorageDirectory().absolutePath+"/testflight/")
            if(!apkDir.exists()) {
                FileUtils.createDir(apkDir.absolutePath)
            }
        }

        Constant.BASE_URL = SharePrefUtil.getValue(this, Constant.jenkins_host, "")
        Constant.JENKINS_USER = SharePrefUtil.getValue(this, Constant.jenkins_user, "")
        Constant.JENKINS_PASSWD = SharePrefUtil.getValue(this, Constant.jenkins_passwd, "")
    }
}