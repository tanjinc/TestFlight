package com.jacktan.fragment.apkpage

import com.jacktan.fragment.apkpage.bean.AppBean

interface IView {
    fun onLoadSuccess(apps: MutableList<AppBean>)
    fun addApp(appBean: AppBean)
    fun fetchError(msg: String, code: Int)
}