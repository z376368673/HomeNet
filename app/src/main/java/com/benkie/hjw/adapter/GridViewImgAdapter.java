package com.benkie.hjw.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.bean.Picture;
import com.benkie.hjw.bean.SkillBean;
import com.benkie.hjw.bean.SkillServiceBean;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.utils.Tools;

import java.util.List;

import retrofit2.Call;

/**
 * Created by 37636 on 2018/1/23.
 */

public class GridViewImgAdapter extends ArrayAdapter<Picture> {
    static Context context;
    private boolean isEdit = false;

    public GridViewImgAdapter(@NonNull Context context) {
        super(context, 0);
        this.context = context;
    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_img, null);
            holder = new ViewHolder();
            holder.initView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Picture item = getItem(position);
        holder.initData(item, position);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_img;
        ImageView iv_delect;

        void initView(View convertView) {
            iv_img = convertView.findViewById(R.id.iv_img);
            iv_delect = convertView.findViewById(R.id.iv_delect);
        }

        void initData(final Picture item, int p) {
            if (isEdit) {
                iv_delect.setVisibility(View.VISIBLE);
            } else {
                iv_delect.setVisibility(View.INVISIBLE);
            }
            if (item.getId() == -1) {
                iv_img.setImageResource(R.mipmap.iv_up_img_s);
                iv_delect.setVisibility(View.INVISIBLE);
            } else {
                Tools.loadImg(context, iv_img, item.getPicture());
            }
            iv_delect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BaseDialog.dialogStyle1(context, "您确认删除此照片吗？", "确定", "取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (item.getId() == 0) {
                                remove(item);
                                notifyDataSetChanged();
                            } else {
                                delImg(item);
                            }
                        }
                    });
                }
            });
        }

        /**
         * 删除服务器图片
         *
         * @param item
         */
        private void delImg(final Picture item) {
            Call call = Http.links.delSkillImg(item.getId() + "");
            Http.http.call(context, call, true, new Http.JsonCallback() {
                @Override
                public void onResult(String json, String error) {
                    JSONObject jsObj = JSON.parseObject(json);
                    int msg = jsObj.getIntValue("msg");
                    if (msg == 1) {
                        remove(item);
                        notifyDataSetChanged();
                    } else {
                        onFail("删除失败");
                    }
                }

                @Override
                public void onFail(String error) {
                    ToastUtil.showInfo(context, error);
                }
            });
        }

    }
}
