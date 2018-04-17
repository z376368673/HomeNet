package com.benkie.hjw.ui.setting

import android.os.Bundle
import android.view.View
import butterknife.ButterKnife
import com.alibaba.fastjson.JSON
import com.benkie.hjw.R
import com.benkie.hjw.db.DataHpler
import com.benkie.hjw.net.Http
import com.benkie.hjw.ui.BaseActivity
import com.benkie.hjw.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_feedback.*

class FeedbackActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        ButterKnife.bind(this)
        headView!!.setTitle("用户反馈")
        headView!!.setBtBack(this)
    }

    fun sumbit(view: View) {
        val text = ed_feedback.text?.toString()
        if (!text.isNullOrBlank()){
            val call = Http.links.feedback(DataHpler.getUserInfo().userid,text)
            Http.http.call(mActivity,call, true, object : Http.JsonCallback {
                override fun onResult(json: String, error: String) {
                    var obj = JSON.parseObject(json)
                    val msg = obj.getIntValue("msg")
                    if (msg==1){
                        onFail("提交成功")
                        finish()
                    }else{
                        onFail("提交失败")
                    }
                }

                override fun onFail(error: String) {
                    ToastUtil.showInfo(mActivity,error)
                }
            })
        }else{
            ToastUtil.showInfo(mActivity,"请输入您的意见...")
        }

    }

}
