package com.benkie.hjw.ui.skill

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import butterknife.ButterKnife
import com.benkie.hjw.R
import com.benkie.hjw.dialog.BaseDialog
import com.benkie.hjw.net.Http
import com.benkie.hjw.ui.BaseActivity
import com.benkie.hjw.utils.CheckUtil
import com.benkie.hjw.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_my_introduce.*

class MyIntroduceActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_introduce)
        ButterKnife.bind(this)
        headView!!.setTitle("我的实力")
        headView!!.setBtBack(this)
        val des = intent.getStringExtra("Des")
        if (!des.isNullOrBlank())
            ed_feedback.setText(des)
        ed_feedback.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                isModify = true
                if (charSequence.length > 80) {
                    ToastUtil.showInfo(mActivity, "最多输入80个字符")
                }
                tv_count.text = charSequence.length.toString() + ""
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }

    fun sumbit(view: View) {
        var intent = Intent()
        var info = ed_feedback.text.toString()
        intent.putExtra("info", info)
        setResult(Activity.RESULT_OK, intent)
        finish()
//        if (!CheckUtil.containsEmoji(info)){
//
//        }else{
//            ToastUtil.showInfo(this,"您输入的内容有非法字符或表情")
//        }

    }


    override fun onBackPressed() {
        if (isModify)
            BaseDialog.dialogStyle2(mActivity, "您的修改还未保存，您确认退出吗？", "保存", "放弃") { view ->
                val tag = view.tag as Int
                if (tag == 1) {
                    sumbit(ed_feedback)
                } else {
                    finish()
                }
            }
        else
            super.onBackPressed()
    }

}