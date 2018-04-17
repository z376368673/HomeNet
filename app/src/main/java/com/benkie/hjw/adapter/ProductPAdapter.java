package com.benkie.hjw.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.bean.HomeProductBean;
import com.benkie.hjw.bean.Picture;
import com.benkie.hjw.bean.TypeBean;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.utils.Tools;
import com.benkie.hjw.wxapi.WXPay;

import java.util.List;

import retrofit2.Call;

/**
 * Created by 37636 on 2018/1/23.
 */

public class ProductPAdapter extends ArrayAdapter<HomeProductBean> {
    static Activity context;

    public ProductPAdapter(@NonNull Activity context) {
        super(context, 0);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_product_p, null);
            holder = new ViewHolder();
            holder.initView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeProductBean item = getItem(position);
        holder.initData(item, position);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_recom;
        ImageView iv_img;
        TextView tv_name;
        TextView tv_date;
        TextView tv_type;
        TextView tv_xufei;
        TextView tv_read;
        TextView tv_zan;
        TextView tv_operation;

        void initView(View convertView) {
            iv_recom = convertView.findViewById(R.id.iv_recom);
            iv_img = convertView.findViewById(R.id.iv_img);
            tv_name = convertView.findViewById(R.id.tv_name);
            tv_date = convertView.findViewById(R.id.tv_date);
            tv_type = convertView.findViewById(R.id.tv_type);
            tv_xufei = convertView.findViewById(R.id.tv_xufei);
            tv_read = convertView.findViewById(R.id.tv_read);
            tv_zan = convertView.findViewById(R.id.tv_zan);
            tv_operation = convertView.findViewById(R.id.tv_operation);
        }

        void initData(final HomeProductBean item, int p) {

            Picture picture = item.getImgs();
            if (picture != null) {
                Tools.loadImg(context, iv_img, picture.getPicture());
                List<TypeBean> services = picture.getService();
                tv_type.setText(getServiceString(services));
            } else {
                iv_img.setImageResource(R.mipmap.iv_up_img_p);
            }
            tv_name.setText(item.getName());

            if (item.getMdate() <= 15) {
                tv_xufei.setVisibility(View.VISIBLE);
                tv_date.setText("展示剩余天数：" + item.getMdate() + " 天");
            } else if (item.getMdate() <= 0) {
                tv_xufei.setVisibility(View.VISIBLE);
                tv_date.setText("请及时续费,以免项目下架");
            } else {
                tv_xufei.setVisibility(View.INVISIBLE);
                tv_date.setText("展示剩余天数：" + item.getMdate() + " 天");
            }
            tv_read.setText(item.getReadCount() + "");
            tv_zan.setText(item.getItemPoint()+"");
            tv_operation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Tools.isFastClick())
                        if (adapterInterface!=null){
                            adapterInterface.operation(item);
                        }

                }
            });
            tv_xufei.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Tools.isFastClick())
                    if (adapterInterface!=null){
                        adapterInterface.toRenewMoney(item);
                    }

                }
            });
            if (item.getTag()==1){
                iv_recom.setVisibility(View.VISIBLE);
            }else {
                iv_recom.setVisibility(View.GONE);
            }
        }

        public String getServiceString(List<TypeBean> services) {
            String str = "";
            for (int i = 0; i < services.size(); i++) {
                str += services.get(i).getName() + "/";
            }
            if (str.length() > 1) {
                str = str.substring(0, str.length() - 1);
            }
            return str;
        }

    }

    public void setAdapterInterface(AdapterInterface adapterInterface) {
        this.adapterInterface = adapterInterface;
    }

    AdapterInterface adapterInterface;
    public interface  AdapterInterface{
        public void toRenewMoney(HomeProductBean item);
        public void operation(HomeProductBean item);
    }
}
