package com.benkie.hjw.ui.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import butterknife.ButterKnife
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.benkie.hjw.R
import com.benkie.hjw.db.DataHpler
import com.benkie.hjw.net.Http
import com.benkie.hjw.ui.BaseActivity
import com.benkie.hjw.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_modify_phone.*


class ModifyPhoneActivity : BaseActivity() {
    var smsOld: String = "10086"
    var smsNew: String = "10086"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_phone)
        ButterKnife.bind(this)
        headView!!.setTitle("更改手机号")
        headView!!.setBtBack(this)
        initView()
    }
    var time = 60;
    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            if (msg.what == 1) {
                time = time - 1;
                if (time != 0){
                    tv_old_code.setText(time.toString()+"秒后重新获取")
                   sendEmptyMessageDelayed(1, 1000)
                }else{
                    tv_old_code.setText("获取验证码")
                    tv_old_code.setEnabled(true)
                    time = 60;
                }
            } else if (msg.what == 2) {
                time = time - 1;
                if (time != 0){
                    tv_new_code.setText(time.toString()+"秒后重新获取")
                    sendEmptyMessageDelayed(1, 1000)
                }else{
                    tv_new_code.setText("获取验证码")
                    tv_new_code.setEnabled(true)
                    time = 60;
                }
            }
        }
    }

    /**
     * 发送验证码
     */
    fun registerVerifyOld(view: View) {
        val phone = tv_old_phone.text.toString()
        val call = Http.links.otherVerify(phone)
        Http.http.call(mActivity, call, false, object : Http.JsonCallback {
            override fun onResult(json: String, error: String) {
                var obj = JSONObject.parseObject(json)
                var msg = obj.getString("msg")
                if (msg.equals("1")) {
                    smsOld = obj.getString("sms")
                    // var mobile = obj.getString("mobile")
                    handler.sendEmptyMessageDelayed(1, 1000)
                    tv_old_code.setEnabled(false)
                } else {
                    ToastUtil.showInfo(mActivity, "服务器返回数据错误")
                }

            }

            override fun onFail(error: String) {
                ToastUtil.showInfo(mActivity, error)
            }
        })
    }

    /**
     * 发送验证码
     */
    fun registerVerifyNew(view: View) {
        val phone = ed_new_phone.text.toString()
        if (phone.isNullOrBlank()||phone.length!=11){
            ToastUtil.showInfo(mActivity, "请填写正确的手机号")
        }
        val call = Http.links.registerVerify(phone)
        Http.http.call(mActivity, call, false, object : Http.JsonCallback {
            override fun onResult(json: String, error: String) {
                var obj = JSONObject.parseObject(json)
                var msg = obj.getString("msg")
                if (msg.equals("1")) {
                    smsNew = obj.getString("sms")
                    // var mobile = obj.getString("mobile")
                    handler.sendEmptyMessageDelayed(2, 1000)
                    tv_new_code.setEnabled(false)
                } else {
                    ToastUtil.showInfo(mActivity, "服务器返回数据错误")
                }

            }

            override fun onFail(error: String) {
                ToastUtil.showInfo(mActivity, error)
            }
        })
    }

    private fun initView() {
        tv_old_phone.setText(DataHpler.getUserInfo().mobile)
    }

    fun confirm(view: View) {
        val codeOld = ed_old_code.text.toString()
        val codeNew = ed_new_code.text.toString()
        val phone = ed_new_phone.text.toString()
        if (!codeOld.equals(smsOld)){
            ToastUtil.showInfo(mActivity, "当前手机号的验证码不正确")
            return
        }else if (!codeNew.equals(smsNew)){
            ToastUtil.showInfo(mActivity, "验证码不正确")
            return
        }
        upDatePhone(phone)
    }

    private fun upDatePhone(phone: String) {
        val call = Http.links.updateUserPhone(DataHpler.getToken(), phone)
        Http.http.call(mActivity, call, true, object : Http.JsonCallback {
            override fun onResult(json: String, error: String) {
                val jsObj = JSON.parseObject(json)
                val msg = jsObj.getIntValue("msg")
                if (msg == 1) {
                    val userInfo = DataHpler.getUserInfo()
                    userInfo.mobile = phone
                    DataHpler.setUserInfo(JSON.toJSONString(userInfo))
                    setResult(Activity.RESULT_OK)
                    finish()
                    onFail("保存成功")
                } else {
                    onFail("保存失败")
                }
            }

            override fun onFail(error: String) {
                ToastUtil.showInfo(mActivity, error)
            }
        })
    }
}
