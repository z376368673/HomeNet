package com.benkie.hjw.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import cn.jpush.android.api.JPushInterface
import com.alibaba.fastjson.JSONObject
import com.benkie.hjw.R
import com.benkie.hjw.db.DataHpler
import com.benkie.hjw.net.Http
import com.benkie.hjw.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

/**
 * Created by 37636 on 2018/1/19.
 */

class LoginActivity : BaseActivity() {
    var  isBack = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        headView.setTitle("登陆")
        headView.setBtBack(this)
        ed_nickname.setText(DataHpler.getLastAccount())
        isBack = intent.getBooleanExtra("flag",false)
    }

    /**
     * 忘记密码
     */
    fun forgetPassword(view: View) {
        ToastUtil.showInfo(mActivity, "忘记密码")
        val intent = Intent(this, RegisterActivity::class.java)
        intent.putExtra("type", 2)
        startActivityForResult(intent, 1000)
    }
    /**
     * 去注册
     */
    fun register(v:View){
        val intent = Intent(this,RegisterActivity::class.java)
        intent.putExtra("type",1);
        startActivityForResult(intent,1000)
    }
    /**
     * 登陆
     */
    fun login(v: View) {
        val phone = ed_nickname.text.toString()
        val password = ed_password.text.toString()
        if (phone.isNullOrBlank()) {
            ToastUtil.showInfo(this, "请输入账号")
        } else if (password.isNullOrBlank()) {
            ToastUtil.showInfo(this, "请输入密码")
        } else {
            //登陆
            login(phone, password)
        }
    }

    fun login(phone: String, pwd: String) {
        val call = Http.links.login(phone,pwd)
        Http.http.call(mActivity,call, true, object : Http.JsonCallback {
            override fun onResult(json: String, error: String) {
                var obj = JSONObject.parseObject(json)
                var msg = obj.getString("msg")
                if (msg.equals("1")){
                   var token = obj.getString("token")
                    DataHpler.setToken(token)
                    DataHpler.setLastAccount(phone)
                    //获取用户信息
                    getUserInfo(token)
                }else if(msg.equals("0")){
                    ToastUtil.showInfo(mActivity,"账号或密码错误")
                }else if(msg.equals("2")){
                    ToastUtil.showInfo(mActivity,"账号被封")
                }else{
                    ToastUtil.showInfo(mActivity,"密码错误")
                }
            }
            override fun onFail(error: String) {
                ToastUtil.showInfo(mActivity,"登录失败"+error)
            }
        })
    }

    /**
     * 获取用户信息
     */
    fun getUserInfo(token:String){
        val call = Http.links.getUserInfo(token)
        Http.http.call(mActivity,call, true, object : Http.JsonCallback {
            override fun onResult(json: String, error: String) {
                var obj = JSONObject.parseObject(json)
                var msg = obj.getString("msg")
                if (msg.equals("1")){
                    DataHpler.setUserInfo(json)
                    var name = DataHpler.getUserInfo().name;
                    name = name.substring(name.length-4);
                    DataHpler.getUserInfo().name= name
                    setTagAndAlias()
                    goMian()
                }else{
                    ToastUtil.showInfo(mActivity,"获取用户信息失败")
                }
            }

            override fun onFail(error: String) {
                ToastUtil.showInfo(mActivity,"获取用户信息失败"+error)
            }
        })
    }

    fun goMian(){
        if (!isBack){
            var intent = Intent(mActivity,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        finish()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1000&&resultCode== Activity.RESULT_OK&&data!=null){
            ed_nickname.setText(data.getStringExtra("Phone"))
        }
    }

    /**
     * JPush设置标签与别名
     */
    private fun setTagAndAlias() {
        /**
         * 这里设置了别名，在这里获取的用户登录的信息
         * 并且此时已经获取了用户的userId,然后就可以用用户的userId来设置别名了
         */
        //false状态为未设置标签与别名成功
        //if (UserUtils.getTagAlias(getHoldingActivity()) == false) {
        val tags = HashSet<String>()
        //这里可以设置你要推送的人，一般是用户uid 不为空在设置进去 可同时添加多个
        tags.add(DataHpler.getUserInfo().mobile)//设置tag
        //上下文、别名【Sting行】、标签【Set型】、回调
        JPushInterface.setTags(this, 0, tags)
        JPushInterface.setAlias(this, 0, DataHpler.getUserInfo().mobile)
        // }
    }
}
