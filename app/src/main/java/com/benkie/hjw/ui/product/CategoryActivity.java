package com.benkie.hjw.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.adapter.ChannelAdapter;
import com.benkie.hjw.bean.Category;
import com.benkie.hjw.bean.Channel;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.listener.ItemDragHelperCallBack;
import com.benkie.hjw.listener.OnChannelDragListener;
import com.benkie.hjw.listener.OnChannelListener;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.utils.LogUtils;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.view.HeadView;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

import static com.benkie.hjw.bean.Channel.TYPE_MY_CHANNEL;

/**
 * Created by 37636 on 2018/1/27.
 */

public class CategoryActivity extends BaseActivity implements OnChannelDragListener {

    public static String TEXT = "channel";

    private HeadView headView;
    private RecyclerView mRecyclerView;
    private List<Channel> mDatas = new ArrayList<>();
    private ChannelAdapter mAdapter;
    private ItemTouchHelper mHelper;
    private OnChannelListener mOnChannelListener;
    private List<Channel> likeList;

    public void setOnChannelListener(OnChannelListener onChannelListener) {
        mOnChannelListener = onChannelListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        headView = (HeadView) findViewById(R.id.headView);
        //headView.setBtBack(this);
        headView.setBtBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        headView.setTitle("全部分类");
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //getAllData();
        getMyLikes();
    }

    private void saveLikes() {
        if (mAdapter != null) {
            List<Channel> channels = mAdapter.getMyChannel();
//            String json = JSON.toJSONString(channels);
//            DataHpler.setLikeType(json);
            saveMyLikes(channels);
        }
    }

    /**
     * 获取我喜欢的分类
     */
    private void saveMyLikes(List<Channel> channels) {
        if (channels != null) {
            String str = "";
            for (int i = 0; i < channels.size(); i++) {
                str += channels.get(i).getId() + ",";
            }
            if (str.length() > 1) {
                str = str.substring(0, str.length() - 1);
            }
            if (TextUtils.isEmpty(str))
                str = "0";
            Log.e("saveMyLikes", str);
            Call call = Http.links.updateCategory(DataHpler.getUserInfo().getUserid(), str);
            Http.http.call(mActivity, call, true, new Http.JsonCallback() {
                @Override
                public void onResult(String json, String error) {
                    JSONObject jsObj = JSON.parseObject(json);
                    int msg = jsObj.getIntValue("msg");
                    if (msg == 1) {
                        Intent intent = new Intent();
                        intent.setAction("com.benkie.public");
                        intent.putExtra("errCode", String.valueOf(2));
                        sendBroadcast(intent);
                        onFail("保存成功");
                        finish();
                    } else {
                        onFail(jsObj.getString("errorInfo"));
                    }
                }

                @Override
                public void onFail(String error) {
                    ToastUtil.showInfo(mActivity, error);
                }
            });
        }
    }

    /**
     * 获取我喜欢的分类
     */
    private void getMyLikes() {
        Call call = Http.links.userItemCategory(DataHpler.getUserInfo().getUserid());
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    List<Category> likeCat = JSON.parseArray(jsObj.getString("data"), Category.class);
                    List<Category> otherCat = JSON.parseArray(jsObj.getString("more"), Category.class);
                    processLogic(dataToChnnel(likeCat), dataToChnnel(otherCat));
                } else {
                    onFail("获取数据失败");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }

    @Override
    public void onBackPressed() {

        BaseDialog.dialogStyle2(mActivity, "你确认保存此修改吗？", "保存", "放弃", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                if (tag == 1) {
                    saveLikes();
                } else {
                    finish();
                }
            }
        });
    }

    /**
     * 获取全部分类
     *
     * @param likeList
     */
    private void getAllData(final List<Channel> likeList) {
        Call call = Http.links.allProductType();
        Http.http.call(mActivity, call, true, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                JSONObject jsObj = JSON.parseObject(json);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    List<Category> otherList = JSON.parseArray(jsObj.getString("data"), Category.class);
                    List<Channel> channelList = dataToChnnel(otherList);
                    //List<Channel> likeList = JSON.parseArray(DataHpler.getLikeType(), Channel.class);
                    removeData(channelList, likeList);
                    processLogic(likeList, channelList);
                } else {
                    onFail("获取数据失败");
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(mActivity, error);
            }
        });
    }

    /**
     * 从data1 中移除  data2;
     *
     * @param data1
     * @param data2
     * @return
     */
    private List<Channel> removeData(List<Channel> data1, List<Channel> data2) {
        List<Channel> channelList = new ArrayList<>();
        if (data1 == null) return new ArrayList<>();
        if (data2 == null) return data1;
        for (int i = 0; i < data2.size(); i++) {
            for (int j = 0; j < data1.size(); j++) {
                if (data2.get(i).getId() == data1.get(j).getId()) {
                    channelList.add(data1.get(j));
                }
            }
        }
        data1.removeAll(channelList);
        return data1;
    }


    private List<Channel> dataToChnnel(List<Category> data) {
        List<Channel> channelList = new ArrayList<>();
        if (data != null)
            for (int i = 0; i < data.size(); i++) {
                Category category = data.get(i);
                Channel channel = new Channel(category.getName(), category.getId());
                channelList.add(channel);
            }
        return channelList;
    }

    private void setDataType(List<Channel> datas, int type) {
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setItemType(type);
        }
    }

    private void processLogic(List<Channel> likeData, List<Channel> otherData) {
        mDatas.add(new Channel(Channel.TYPE_MY, "我喜欢的", ""));
        List<Channel> selectedDatas = new ArrayList<>();
        selectedDatas.add(new Channel(1, "推荐", "0"));
        selectedDatas.addAll(likeData);
        List<Channel> unselectedDatas = new ArrayList<>();
        unselectedDatas.addAll(otherData);

        setDataType(selectedDatas, TYPE_MY_CHANNEL);
        setDataType(unselectedDatas, Channel.TYPE_OTHER_CHANNEL);
        mDatas.addAll(selectedDatas);
        mDatas.add(new Channel(Channel.TYPE_OTHER, "更多项目", ""));
        mDatas.addAll(unselectedDatas);

        mAdapter = new ChannelAdapter(mDatas);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = mAdapter.getItemViewType(position);
                return itemViewType == TYPE_MY_CHANNEL || itemViewType == Channel.TYPE_OTHER_CHANNEL ? 1 : 4;
            }
        });
        ItemDragHelperCallBack callBack = new ItemDragHelperCallBack(this);
        mHelper = new ItemTouchHelper(callBack);
        mAdapter.setOnChannelDragListener(this);
        //attachRecyclerView
        mHelper.attachToRecyclerView(mRecyclerView);
        mAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                saveLikes();
//                TextView textView = (TextView) view;
//                String channel = textView.getText().toString();
//                ToastUtil.showInfo(mActivity, channel);
//                setResult(RESULT_OK, new Intent().putExtra(TEXT, channel));
//                finish();


            }
        });
    }

    @Override
    public void onStarDrag(BaseViewHolder baseViewHolder) {
        //开始拖动
        LogUtils.e("onStarDrag", "开始拖动");
        mHelper.startDrag(baseViewHolder);
    }

    @Override
    public void onItemMove(int starPos, int endPos) {
//        if (starPos < 0||endPos<0) return;
        //我的频道之间移动
        if (mOnChannelListener != null)
            mOnChannelListener.onItemMove(starPos - 1, endPos - 1);//去除标题所占的一个index
        onMove(starPos, endPos);
    }

    private void onMove(int starPos, int endPos) {
        Channel startChannel = mDatas.get(starPos);
        //先删除之前的位置
        mDatas.remove(starPos);
        //添加到现在的位置
        mDatas.add(endPos, startChannel);
        mAdapter.notifyItemMoved(starPos, endPos);
    }

    @Override
    public void onMoveToMyChannel(int starPos, int endPos) {
        //移动到我的频道
        onMove(starPos, endPos);

        if (mOnChannelListener != null)
            mOnChannelListener.onMoveToMyChannel(starPos - 1 - mAdapter.getMyChannelSize(), endPos - 1);
    }

    @Override
    public void onMoveToOtherChannel(int starPos, int endPos) {
        //移动到推荐频道
        onMove(starPos, endPos);
        if (mOnChannelListener != null)
            mOnChannelListener.onMoveToOtherChannel(starPos - 1, endPos - 2 - mAdapter.getMyChannelSize());
    }

}
