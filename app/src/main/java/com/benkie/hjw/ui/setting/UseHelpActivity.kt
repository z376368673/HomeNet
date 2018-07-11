package com.benkie.hjw.ui.setting

import android.graphics.BitmapFactory
import android.os.Bundle
import butterknife.ButterKnife
import com.alibaba.fastjson.JSON
import com.benkie.hjw.R
import com.benkie.hjw.net.Http
import com.benkie.hjw.ui.BaseActivity
import com.benkie.hjw.utils.Tools
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_use_help.*
import java.io.File


class UseHelpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_use_help)
        ButterKnife.bind(this)
        headView.setTitle("使用帮助")
        headView.setBtBack(this)
        initView()
    }

    fun initView() {
        getDate()
    }

    /**
     *  获取数据
     */
    fun getDate() {
        val call = Http.links.explain()
        Http.http.call(mActivity, call, true, object : Http.JsonCallback {
            override fun onResult(json: String, error: String) {
                var obj = JSON.parseObject(json);
                val msg = obj.getIntValue("msg")
                if (msg == 1) {
                    var arr = obj.getJSONArray("info")
                    obj = arr.getJSONObject(0)
                    //val url = "https://b-ssl.duitang.com/uploads/item/201406/12/20140612211118_YYXAC.jpeg"//obj.getString("itemAgreement")
                    val url = obj.getString("itemAgreement")
                    //Tools.loadImg(mActivity,iv_img,url)
//                    Glide.with(mActivity)
//                            .load(url)
//                            .error(R.mipmap.iv_defult_img)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(iv_img)
//                    Picasso.get()
//                            .load(url)
//                            .into(iv_img);

                    Glide.with(mActivity).load(url)
                            .downloadOnly(object : SimpleTarget<File>() {
                                override fun onResourceReady(resource: File, glideAnimation: GlideAnimation<in File>) {
                                    // 将保存的图片地址给SubsamplingScaleImageView,这里注意设置ImageViewState设置初始显示比例
                                    val newOpts = BitmapFactory.Options()
                                    //开始读入图片，此时把options.inJustDecodeBounds 设回true了
                                    //newOpts.inJustDecodeBounds = true
                                    newOpts.inSampleSize = 1
                                    val bitmap = BitmapFactory.decodeFile(resource.absolutePath, BitmapFactory.Options())
                                    // 显示处理好的Bitmap图片
                                    iv_img.setImageBitmap(bitmap)
                                }
                            })

//                    Glide.with(mActivity)
//                            .load(url)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(iv_img)
                }
            }

            override fun onFail(error: String) {

            }
        })
    }


}