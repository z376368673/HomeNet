package com.benkie.hjw.ui.skill

import android.os.Bundle
import android.view.View
import butterknife.ButterKnife
import com.alibaba.fastjson.JSON
import com.benkie.hjw.R
import com.benkie.hjw.db.DataHpler
import com.benkie.hjw.net.Http
import com.benkie.hjw.ui.BaseActivity
import com.benkie.hjw.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_skill_feedback.*

/***
 * 添加用户想要的技能
 */
class SkillFeedbackkActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skill_feedback)
        ButterKnife.bind(this)
        headView!!.setTitle("技能反馈")
        headView!!.setBtBack(this)
    }

    fun sumbit(view: View) {
        val text = ed_feedback.text?.toString() + " ——技能反馈"
        val call = Http.links.feedback(DataHpler.getUserInfo().userid, text);
        Http.http.call(mActivity, call, true, object : Http.JsonCallback {
            override fun onResult(json: String, error: String) {
                var jsObj = JSON.parseObject(json);
                val msg = jsObj.getIntValue("msg")
                if (msg == 1) {
                    ToastUtil.showInfo(mActivity, "提交成功，等待审核")
                } else {
                    ToastUtil.showInfo(mActivity, "提交失败,请联系客服")
                }
            }

            override fun onFail(error: String) {
            }
        })

    }

}