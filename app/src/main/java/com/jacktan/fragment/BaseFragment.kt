package com.jacktan.fragment

import android.annotation.SuppressLint
import android.os.Looper
import androidx.fragment.app.Fragment
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

open abstract class BaseFragment : Fragment() {
    abstract fun getName() : String

    @SuppressLint("CheckResult")
    fun toast(msg: String) {
        if (isAdded) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
            } else {
                Observable.just(msg)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
                    }
            }
        }
    }

    abstract fun onBackPress():Boolean
}