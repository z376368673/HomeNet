package com.benkie.hjw.ui.setting

import android.os.Bundle
import butterknife.ButterKnife
import com.alibaba.fastjson.JSON
import com.benkie.hjw.R
import com.benkie.hjw.net.Http
import com.benkie.hjw.ui.BaseActivity
import com.benkie.hjw.utils.Tools
import kotlinx.android.synthetic.main.activity_use_help.*


class UseHelpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_use_help)
        ButterKnife.bind(this)
        headView.setTitle("使用帮助")
        headView.setBtBack(this)
        initView()
    }

     fun initView(){
         getDate()
    }

    /**
     *  获取数据
     */
    fun getDate() {
        val call = Http.links.explain()
        Http.http.call(mActivity,call, true, object : Http.JsonCallback {
            override fun onResult(json: String, error: String) {
                var obj = JSON.parseObject(json);
                val msg = obj.getIntValue("msg")
                if (msg==1){
                   var  arr = obj.getJSONArray("info")
                    obj =   arr.getJSONObject(0)
                    //val url = "https://b-ssl.duitang.com/uploads/item/201406/12/20140612211118_YYXAC.jpeg"//obj.getString("itemAgreement")
                    val url = obj.getString("itemAgreement")
                    Tools.loadImg(mActivity,iv_img,url)
                }
            }
            override fun onFail(error: String) {

            }
        })
    }


}