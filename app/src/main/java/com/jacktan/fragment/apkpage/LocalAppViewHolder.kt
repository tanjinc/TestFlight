package com.jacktan.fragment.apkpage

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jacktan.testflight.R

/**
 * Author by jacktan, Date on 19-3-27.
 */
class LocalAppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
    var  apkIconImg = itemView.findViewById<ImageView>(R.id.appIconImg)
    var appNameTv = itemView.findViewById<TextView>(R.id.app_desc_tv)
    var appVersionTv = itemView.findViewById<TextView>(R.id.appVersionTv)
    var appVersionCodeTv = itemView.findViewById<TextView>(R.id.appVersionCodeTv)
    var appCleanBtn = itemView.findViewById<TextView>(R.id.appCleanBtn)


    init {
        val margin = itemView.resources.getDimensionPixelOffset(R.dimen.shadow_margin);
        val viewOutlineProvider: ViewOutlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(margin, 0, view.width-margin, view.height-margin, margin/2f)
            }
        }
//        itemView.elevation = margin*1f
        itemView.outlineProvider = viewOutlineProvider
        itemView.clipToOutline = true
    }
}
