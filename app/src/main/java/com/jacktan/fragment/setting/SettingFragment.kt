package com.jacktan.fragment.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jacktan.testflight.R
import com.jacktan.common.recyclerview.adapter.MultiItemTypeAdapter
import com.jacktan.common.recyclerview.base.ItemViewDelegate
import com.jacktan.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_setting_layout.*

class SettingFragment: BaseFragment(){

    private lateinit var mAdapter: MultiItemTypeAdapter<SettingDataBean>
    private var mDataArrayList = mutableListOf<SettingDataBean>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting_layout, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun getName(): String {
        return "系统设置"
    }

    override fun onBackPress(): Boolean {
        return false
    }

    private fun initView() {
        mAdapter = MultiItemTypeAdapter(context)
        mAdapter.addViewDelegate(SettingViewDelegate())
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        mDataArrayList.add(
            SettingDataBean(
                "设置时间",
                Settings.ACTION_DATE_SETTINGS
            )
        )
        mDataArrayList.add(
            SettingDataBean(
                "设置代理",
                Settings.ACTION_WIFI_SETTINGS
            )
        )
        mDataArrayList.add(
            SettingDataBean(
                "手机信息",
                Settings.ACTION_DEVICE_INFO_SETTINGS
            )
        )
        mDataArrayList.add(
            SettingDataBean(
                "系统设置",
                Settings.ACTION_SETTINGS
            )
        )
        mAdapter.setData(mDataArrayList)
    }

    data class SettingDataBean(val name:String, val cm:String?=null)



    class SettingViewDelegate : ItemViewDelegate<SettingDataBean, SettingViewHolder> {
        override fun getItemViewLayoutId(): Int {
            return R.layout.setting_item_layout
        }

        override fun isForViewType(data: SettingDataBean?, p: Int): Boolean {
            return true
        }

        override fun bind(viewHolder: SettingViewHolder?, data: SettingDataBean?, position: Int) {
            viewHolder?.settingNameTv?.text = data?.name
            viewHolder?.itemView?.setOnClickListener {
                if (data?.cm != null) {
                    viewHolder.startTargetActivity(data?.cm)
                }
            }
        }

        override fun createViewHolder(view: View): SettingViewHolder {
            return SettingViewHolder(
                view
            )
        }

    }

    class SettingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val settingNameTv:TextView = itemView.findViewById(R.id.setting_name)
        val settingIcon: ImageView = itemView.findViewById(R.id.setting_icon)

        fun startTargetActivity(target: String) {
            val intent = Intent(target)
//            intent.action = "android.intent.action.VIEW"
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                itemView.context?.startActivity(intent)
            } catch (e: Exception) {
                Log.e("", e.toString())
            }
        }
    }
}