package com.jacktan.fragment.apkpage.bean

import com.jacktan.utils.AppUtils


data class AppBean(val name:String,
                   val desc:String,
                   val jobDetailBean: JobDetailBean,
                   val appInfo: AppUtils.AppInfo?)