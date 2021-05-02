package com.jacktan.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import com.jacktan.testflight.BuildConfig
import com.jacktan.TestFlightApplication
import java.io.File

class CommonUtil private constructor() {
    var name:String ? = ""

    companion object {
        val instance: CommonUtil = CommonUtil()
    }

    public fun test() {
    }

    public fun getSDPath(): String {
        var sdDir: File? = null
        val sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED)//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory()//获取跟目录
        }
        return sdDir!!.toString()
    }

    public fun isFileExit(fileName:String):Boolean {
        return File(fileName).exists()
    }


    public fun installApp(context: Context?, apkFile: String) {
        if (context == null) {
            return
        }
        val apkFile = File(TestFlightApplication.apkDir, apkFile)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".myprovider", apkFile)
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
        }
        context.startActivity(intent)
    }
}