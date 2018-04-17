package com.benkie.hjw.ui.mine

import android.os.Bundle
import butterknife.ButterKnife
import com.benkie.hjw.R
import com.benkie.hjw.bean.MessageInfo
import com.benkie.hjw.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_message.*
import java.text.SimpleDateFormat

class MessageActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        headView.setBtBack(this)
        headView.setTitle("系统通知")
        val msg = intent.extras!!.getSerializable("Msg") as MessageInfo
        if (msg != null) {
            tv_titles!!.text = msg.title
            val date = SimpleDateFormat("yyyy-MM-dd").format(msg.createDate)+ " 还居网"
            tv_date!!.text = date
            tv_content!!.text = msg.content
        }
    }

}
