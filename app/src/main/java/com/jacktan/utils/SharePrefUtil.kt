package com.jacktan.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jacktan.Constant

import java.util.ArrayList

/**
 * Author by jacktan, Date on 19-3-27.
 */
object SharePrefUtil {

    /**
     * 保存List
     * @param tag
     * @param datalist
     */
    fun <T> setDataList(context: Context, tag: String, datalist: List<T>?) {
        if (null == datalist || datalist.isEmpty())
            return

        val preferences = context.getSharedPreferences(tag, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        val gson = Gson()
        //转换成json数据，再保存
        val strJson = gson.toJson(datalist)
        editor.clear()
        editor.putString(tag, strJson)
        editor.apply()

    }

    /**
     * 获取List
     * @param tag
     * @return
     */
    fun <T> getDataList(context: Context?, tag: String): List<T> {
        var datalist: List<T> = ArrayList()
        val preferences = context?.getSharedPreferences(tag, Context.MODE_PRIVATE)
        val strJson = preferences?.getString(tag, null) ?: return datalist
        val gson = Gson()
        datalist = gson.fromJson(strJson, object : TypeToken<List<T>>() {}.type)
        return datalist
    }

    fun setValue(context: Context?, key:String, value: String) {
        val editor = context?.getSharedPreferences(Constant.spName, Context.MODE_PRIVATE)?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    fun getValue(context: Context?, key: String, default: String): String {
        val sp = context?.getSharedPreferences(Constant.spName, Context.MODE_PRIVATE)
        return sp?.getString(key, default)!!
    }
}
