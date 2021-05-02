package com.jacktan.fragment.apkpage

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jacktan.testflight.R
import com.jacktan.common.RingProgressView

/**
 * Author by jacktan, Date on 19-3-19.
 */
class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        const val STATUS_IDEL = 0
        const val STATUS_DOWNLOADING = 1
        const val STATUS_DOWNLOADED = 2
        const val STATUS_INSTALLED = 3
        const val STATUS_INSTALLING = 4
        const val STATUS_DIR = 5;

    }
    val appNameTv : TextView = itemView.findViewById(R.id.app_desc_tv)
    val appVersionTv : TextView = itemView.findViewById(R.id.version_tv)
    val appPacketNameTv : TextView = itemView.findViewById(R.id.packetname_tv)
    val jobNumTv: TextView = itemView.findViewById(R.id.job_num_tv)
    val appIconImg: ImageView = itemView.findViewById(R.id.app_icon)
    val cleanBtn: TextView = itemView.findViewById(R.id.clear_data_btn)
    val processView: RingProgressView = itemView.findViewById(R.id.loading_view)
    val openBtn: Button = itemView.findViewById(R.id.open_btn)

    fun setStatus(status: Int) {
        val statusStr = when(status) {
            STATUS_IDEL -> {"未下载"}
            STATUS_DOWNLOADING -> { "下载中..." }
            STATUS_DOWNLOADED -> { ""}
            STATUS_INSTALLED -> { "安装完成"}
            STATUS_INSTALLING -> {"正在安装..."}
            else -> {""}
        }
    }
}
