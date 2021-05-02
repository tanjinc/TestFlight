package com.jacktan

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import com.jacktan.testflight.R
import com.jacktan.utils.SharePrefUtil
import kotlinx.android.synthetic.main.about_layout.*

/**
 * Author by jacktan, Date on 19-3-26.
 */
class AboutDialog(context: Context): AppCompatDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_layout)
        serverIpEdit.setText(SharePrefUtil.getValue(context,
            Constant.jenkins_host
        ))
        okBtn.setOnClickListener {
            SharePrefUtil.setValue(context,
                Constant.jenkins_host, serverIpEdit.text.toString())
            dismiss()
        }
    }
}