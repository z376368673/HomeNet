package com.benkie.hjw.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import com.alibaba.fastjson.JSONObject
import com.benkie.hjw.R
import com.benkie.hjw.net.Http
import com.benkie.hjw.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Created by 37636 on 2018/1/19.
 */

class RegisterActivity : BaseActivity() {
    var sms: String = "10086"
    var type = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        headView.setBtBack(this)
        type = intent.getIntExtra("type", 1);
        if (type == 1) {
            headView.setTitle("注册")
            tv_submit.setText("注册");
        } else {
            headView.setTitle("忘记密码")
            tv_submit.setText("确认修改");
        }

    }

    fun sumbit(v: View) {
        val phone = ed_phone.text.toString()
        val code = ed_code.text.toString()
        val password = ed_password.text.toString()
        val password1 = ed_password1.text.toString()
        if (phone.isNullOrBlank()) {
            ToastUtil.showInfo(this, "请输入账号")
            return
        } else if (code.isNullOrBlank()) {
            ToastUtil.showInfo(this, "请输入验证码")
            return
        } else if (!code.equals(sms)) {
            ToastUtil.showInfo(this, "验证码不正确")
            return
        } else if (password.isNullOrBlank()) {
            ToastUtil.showInfo(this, "请输入密码")
            return
        } else if (password1.isNullOrBlank()) {
            ToastUtil.showInfo(this, "请再次输入密码")
            return
        } else if (!password1.equals(password)) {
            ToastUtil.showInfo(this, "两次输入密码不一致,请重新输入")
            return
//            ed_password.setText("")
//            ed_password1.setText("")
        } else {
            register(phone, password)
        }

    }

    var time = 60;
    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1) {
                time = time - 1;
                if (time != 0) {
                    tv_code.setText(time.toString() + "秒后重试")
                    sendEmptyMessageDelayed(1, 1000)
                } else {
                    tv_code.setText("获取验证码")
                    sendEmptyMessageDelayed(2, 1000)
                    time = 60
                }
            } else {
                tv_code.setEnabled(true)
            }
        }
    }


    /**
     * 发送验证码
     */
    fun registerVerify(view: View) {
        val phone = ed_phone.text.toString()
        val call: Call<ResponseBody>
        if (type == 1) {
            call = Http.links.registerVerify(phone)
        } else {
            call = Http.links.otherVerify(phone)
        }

        Http.http.call(mActivity, call, true, object : Http.JsonCallback {
            override fun onResult(json: String, error: String) {
                var obj = JSONObject.parseObject(json)
                var msg = obj.getString("msg")
                if (msg.equals("1")) {
                    sms = obj.getString("sms")
                    // var mobile = obj.getString("mobile")
                    handler.sendEmptyMessageDelayed(1, 1000)
                    tv_code.setEnabled(false)
                } else {
                    if (type == 1)
                        ToastUtil.showInfo(mActivity, "该手机号已被注册")
                    else
                        ToastUtil.showInfo(mActivity, "系统错误")
                }
            }

            override fun onFail(error: String) {
                ToastUtil.showInfo(mActivity, error)
            }
        })
    }

    /**
     * 注册账号
     */
    fun register(phone: String, pwd: String) {
        val call: Call<ResponseBody>
        if (type == 1) {
            call = Http.links.register(phone, pwd)
        } else {
            call = Http.links.forgetPwd(phone, pwd)
        }
        Http.http.call(mActivity, call, true, object : Http.JsonCallback {
            override fun onResult(json: String, error: String) {
                var obj: JSONObject = JSONObject.parseObject(json)
                var msg = obj.getString("msg")
                if (msg.equals("1")) {
                    if (type == 1) {
                        ToastUtil.showInfo(mActivity, "注册成功")
                    } else {
                        ToastUtil.showInfo(mActivity, "修改成功")
                    }
                    var intent = Intent()
                    intent.putExtra("Phone", phone)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    ToastUtil.showInfo(mActivity, "系统错误")
                }

            }

            override fun onFail(error: String) {
                ToastUtil.showInfo(mActivity, "注册失败" + error)
            }
        })
    }
}