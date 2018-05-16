package com.benkie.hjw.ui.setting

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import butterknife.ButterKnife
import com.alibaba.fastjson.JSON
import com.benkie.hjw.R
import com.benkie.hjw.net.Http
import com.benkie.hjw.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_about_our.*

/**
 *
 *
 * 初次尝试用Kotlin   关于我们
 *
 */

class AboutOurActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_our)
        ButterKnife.bind(this)
        headView.setTitle("关于我们")
        headView.setBtBack(this)

        initView()
    }

    fun initView() {
        val code = getLocalVersion(this)
        tv_version.setText("还居：" + code)
        getDate()
    }

    /**
     *  获取数据
     */
    fun getDate() {
        val call = Http.links.aboutWe()
        Http.http.call(mActivity, call, true, object : Http.JsonCallback {
            override fun onResult(json: String, error: String) {
                var obj = JSON.parseObject(json);
                val msg = obj.getIntValue("msg")
                if (msg==1){
                    obj = obj.getJSONObject("info")
                    val phone = obj.getString("phone")
                    val qq = obj.getString("qq")
                    val explain = obj.getString("explain")
                    if (!explain.isNullOrEmpty()){
                        tv_content.setText(explain)
                        //tv_phone.setText("客服电话：" + phone)
                        //tv_qq.setText("QQ：" + qq)
                    }
                }
            }

            override fun onFail(error: String) {

            }
        })
    }

    /**
     *
     */
    fun getLocalVersion(ctx: Context): String {
        var locaVersionName: String = "1.0"
        try {
            val packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0)
            // localVersion = packageInfo.versionCode
            locaVersionName = packageInfo.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return locaVersionName
    }

}
