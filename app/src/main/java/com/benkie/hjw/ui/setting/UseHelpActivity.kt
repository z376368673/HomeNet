package com.benkie.hjw.ui.setting

import android.os.Bundle
import butterknife.ButterKnife
import com.alibaba.fastjson.JSON
import com.benkie.hjw.R
import com.benkie.hjw.net.Http
import com.benkie.hjw.ui.BaseActivity
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
                    tv_text.setText(obj.getString("itemExplain"))
                }
            }
            override fun onFail(error: String) {

            }
        })
    }


}