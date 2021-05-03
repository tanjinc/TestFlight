package com.jacktan

import android.Manifest
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.jacktan.testflight.R
import com.google.android.material.tabs.TabLayout
import com.jacktan.fragment.apkpage.AppFragment
import com.jacktan.fragment.BaseFragment
import com.jacktan.fragment.setting.SettingFragment
import com.jacktan.utils.SharePrefUtil
import java.util.*


class MainActivity : FragmentActivity() {
    private val TAG:String = "MainActivity"
    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager: ViewPager

    private val mFragmentList = ArrayList<BaseFragment>()
    private val mTitleList = ArrayList<String>()
    private val REQUEST_PERMISSION_CODE = 1

    private val PERMISSIONS_STORAGE =
        arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTabLayout = findViewById(R.id.tabLayout)
        mTabLayout.visibility = View.VISIBLE
        mViewPager = findViewById(R.id.main_viewpager)

        val appFragment = AppFragment()
        mFragmentList.add(appFragment)
        mFragmentList.add(SettingFragment())

        for (item in mFragmentList) {
            mTitleList.add(item.getName())
        }


        val pageAdapter = MyViewPageAdapter(supportFragmentManager, mTitleList, mFragmentList)

        mViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(mTabLayout))
        mViewPager.adapter = pageAdapter

        mTabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(mViewPager))
        mTabLayout.setupWithViewPager(mViewPager)

        if (Constant.BASE_URL.isNullOrEmpty()) {
            showConfig()
        }
    }

    fun showConfig() {
        val configDialog = ConfigDialog(this)
        configDialog.setOnDismissListener {
            ApiManager.init()
        }
        configDialog.show()
    }
    override fun onBackPressed() {
        if (mFragmentList[mViewPager.currentItem].onBackPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {

        }
    }


    inner class MyViewPageAdapter(fm: FragmentManager, private val mTitleArray: ArrayList<String>?, private val mFragmentArray: ArrayList<BaseFragment>?) : FragmentStatePagerAdapter(fm) {

        override fun getItem(i: Int): Fragment {
            return if (mFragmentArray != null) mFragmentArray[i] else AppFragment()
        }

        override fun getCount(): Int {
            return mFragmentArray?.size ?: 0
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return if (mTitleArray != null) mTitleArray[position] else super.getPageTitle(position)
        }
    }
}
