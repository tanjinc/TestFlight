package com.jacktan

import android.os.Build
import android.os.Environment
import androidx.multidex.MultiDexApplication
import com.jacktan.utils.FileUtils
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
            apkDir = File(Environment.getExternalStorageDirectory().absolutePath+"/supertool/")
            if(!apkDir.exists()) {
                FileUtils.createDir(apkDir.absolutePath)
            }
        }
    }
}