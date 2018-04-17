package com.benkie.hjw.ui.mine

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import com.alibaba.fastjson.JSON
import com.benkie.hjw.R
import com.benkie.hjw.bean.MessageInfo
import com.benkie.hjw.bean.SkillServiceBean
import com.benkie.hjw.db.DataHpler
import com.benkie.hjw.net.Http
import com.benkie.hjw.ui.BaseActivity
import com.benkie.hjw.utils.ToastUtil
import com.handmark.pulltorefresh.library.PullToRefreshBase
import kotlinx.android.synthetic.main.activity_news.*
import java.text.SimpleDateFormat


class NewsActivity : BaseActivity() ,PullToRefreshBase.OnRefreshListener2<ListView>{
    override fun onPullDownToRefresh(refreshView: PullToRefreshBase<ListView>?) {
        pullRefreshView.onRefreshComplete();
    }

    override fun onPullUpToRefresh(refreshView: PullToRefreshBase<ListView>?) {
        pullRefreshView.onRefreshComplete();
    }

    lateinit var adapter: MsgAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        headView.setBtBack(this)
        headView.setTitle("系统消息");
        adapter = MsgAdapter()
        iv_no.setVisibility(View.VISIBLE)
        pullRefreshView.setVisibility(View.GONE)
        pullRefreshView.mode = PullToRefreshBase.Mode.BOTH
        pullRefreshView.setOnRefreshListener(this)
        var lv:ListView = pullRefreshView.refreshableView
        lv!!.adapter = adapter
        lv.setDivider(ColorDrawable(Color.parseColor("#f0f0f0")))
        lv.setDividerHeight(25)
        lv!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val msg = parent.adapter.getItem(position) as MessageInfo
            val intent = Intent(mActivity, MessageActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable("Msg", msg)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        getinitData()
    }

    /**
     * 获取数据
     */
    private fun getinitData() {
        val call = Http.links.getMsg(DataHpler.getUserInfo().userid)
        Http.http.call(mActivity,call, true, object : Http.JsonCallback {
            override fun onResult(json: String, error: String) {
                var jsObj = JSON.parseObject(json);
                val msg = jsObj.getIntValue("msg")
                if (msg == 1) {
                    var list = JSON.parseArray(jsObj.getString("info"), MessageInfo::class.java)
                    if (list != null&&list.size>0) {
                        iv_no.setVisibility(View.GONE)
                        pullRefreshView.setVisibility(View.VISIBLE)
                        adapter.addAll(list)
                        adapter.notifyDataSetChanged()
                    }else{
                        iv_no.setVisibility(View.VISIBLE)
                        pullRefreshView.setVisibility(View.GONE)
                    }
                }else{
                    onFail("获取数据失败")
                }
            }

            override fun onFail(error: String) {
                ToastUtil.showInfo(mActivity,error);
            }
        })
    }

    inner class MsgAdapter : ArrayAdapter<MessageInfo>(mActivity, 0) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val holder: ViewHolder
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.item_news, null)
                holder = ViewHolder(convertView)
                convertView!!.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
            }
            val messageInfo = getItem(position)
            holder.setData(messageInfo)
            return convertView
        }
    }

    internal class ViewHolder(view: View) {
        var text: TextView? = null
        var num: TextView? = null

        init {
            num = view.findViewById(R.id.num)
            text = view.findViewById(R.id.text)
        }

        fun setData(data: MessageInfo?) {
            text!!.text = data!!.title
            val date = SimpleDateFormat("yyyy-MM-dd").format(data.createDate)
            num!!.text = date
        }
    }
}
