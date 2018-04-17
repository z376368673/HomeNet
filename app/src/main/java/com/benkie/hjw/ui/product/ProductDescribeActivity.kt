package com.benkie.hjw.ui.product

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
import com.benkie.hjw.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_product_des.*
class ProductDescribeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_des)
        ButterKnife.bind(this)
        headView!!.setTitle("项目描述")
        headView.setBtBackListener { onBackPressed() }
        val des = intent.getStringExtra("Des")
        if (!des.isNullOrBlank())
            ed_feedback.setText(des)
        ed_feedback.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length > 80) {
                    ToastUtil.showInfo(mActivity, "最多输入80个字符")
                }
                isModify= true
                tv_count.text = charSequence.length.toString() + ""
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }

    fun sumbit(view: View) {
        val text = ed_feedback.text?.toString()
        setResult(Activity.RESULT_OK,Intent().putExtra("Des",text))
        finish();
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