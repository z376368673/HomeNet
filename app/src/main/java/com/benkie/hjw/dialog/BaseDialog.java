package com.benkie.hjw.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.benkie.hjw.R;
import com.benkie.hjw.bean.Category;
import com.benkie.hjw.bean.PopBean;
import com.benkie.hjw.utils.ToastUtil;

import java.util.List;

/**
 * Created by 37636 on 2018/1/19.
 */

public class BaseDialog {


    /**
     * 确认取消
     *
     * @param context
     * @param info
     */
    public static void dialogStyle1(final Context context, String info, String confirm, String cancel, final View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_style1, null, false);
        TextView tv_content = (TextView) view.findViewById(R.id.dialog_content);
        tv_content.setText(info);
        TextView tv_confirm = (TextView) view.findViewById(R.id.dialog_confirm);
        tv_confirm.setText(confirm);

        TextView tv_cancel = (TextView) view.findViewById(R.id.dialog_cancel);
        tv_cancel.setText(cancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view)
                .setCancelable(true);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        setDialog(dialog);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onClickListener.onClick(v);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 确认取消
     *
     * @param context
     * @param info
     */
    public static void dialogStyle2(final Context context, String info, String confirm, String cancel, final View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_style2, null, false);
        TextView tv_content = (TextView) view.findViewById(R.id.dialog_content);
        tv_content.setText(info);
        TextView tv_confirm = (TextView) view.findViewById(R.id.dialog_confirm);
        tv_confirm.setText(confirm);
        TextView tv_cancel = (TextView) view.findViewById(R.id.dialog_cancel);
        tv_cancel.setText(cancel);

        tv_cancel.setTag(0);
        tv_confirm.setTag(1);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view)
                .setCancelable(true);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        setDialog(dialog);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onClickListener.onClick(v);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onClickListener.onClick(v);
            }
        });
    }

    /**
     * 支付成功
     *
     * @param context
     * @param info
     */
    public static void dialogPaySuccess(final Context context, String info, final View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pay_success, null, false);
        final TextView tv_bt = (TextView) view.findViewById(R.id.tv_bt);
        tv_bt.setText(info);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view)
                .setCancelable(true);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        setDialog(dialog);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                tv_bt.performClick();
            }
        });
        tv_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onClickListener.onClick(v);
            }
        });
    }

    /**
     * 显示消息
     *
     * @param context
     * @param info
     */
    public static void showMag(final Context context, String title, String info, String confirm, final View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_show_msg, null, false);
        ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        TextView dialog_content = (TextView) view.findViewById(R.id.dialog_content);
        TextView tv_bt = (TextView) view.findViewById(R.id.tv_bt);
        dialog_title.setText(title);
        dialog_content.setText(info);
        tv_bt.setText(confirm);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view)
                .setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        setDialog(dialog);
        tv_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onClickListener.onClick(v);
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onClickListener.onClick(v);
            }
        });
    }

    /**
     * 发布项目支付Dialog
     *
     * @param context
     * @param name
     * @param money
     * @param data
     * @param xufei
     * @param onClickListener
     */
    public static void toPublicPayDialog(final Context context, String name, String showDate, String money, String data, String xufei, String xufeiData, final View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_to_public_pay, null, false);
        TextView tv_bt = view.findViewById(R.id.tv_bt);
        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_money = (TextView) view.findViewById(R.id.tv_money);
        TextView tv_showDate = view.findViewById(R.id.tv_showDate);
        TextView tv_data = (TextView) view.findViewById(R.id.tv_data);
        TextView tv_xufeu = (TextView) view.findViewById(R.id.tv_xufeu);
        TextView tv_xufeuDate = (TextView) view.findViewById(R.id.tv_xufeuDate);
        tv_name.setText(name);
        tv_money.setText(money + " 元");
        tv_showDate.setText("/" + showDate + " 天");
        tv_data.setText(data);
        tv_xufeu.setText(xufei + " 元");
        tv_xufeuDate.setText("/" + xufeiData + " 天");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view)
                .setCancelable(true);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        setDialog(dialog);
        tv_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onClickListener != null)
                    onClickListener.onClick(v);
            }
        });
    }


    /**
     * 项目续费
     *
     * @param context
     * @param data
     * @param onClickListener
     */
    public static void toRenewPayDialog(final Context context, String data, String renewMoney, final View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_to_renew_pay, null, false);
        TextView tv_bt = view.findViewById(R.id.tv_bt);
        TextView tv_data = (TextView) view.findViewById(R.id.tv_data);
        TextView tv_xufeu = (TextView) view.findViewById(R.id.tv_xufeu);
        TextView tv_xufeuDate = (TextView) view.findViewById(R.id.tv_xufeuDate);
        tv_data.setText(data);
        tv_xufeu.setText(renewMoney + " 元");
        tv_xufeuDate.setText(" /半年");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view)
                .setCancelable(true);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        setDialog(dialog);
        tv_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onClickListener != null)
                    onClickListener.onClick(v);
            }
        });

    }

    public static void dialogStyle3(final Context context, List<Category> list, final View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_style3, null, false);
        LinearLayout ll_add = view.findViewById(R.id.ll_add);
        ll_add.removeAllViews();
        if (list == null || list.size() == 0) {
            ToastUtil.showInfo(context, "数据错误");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view)
                .setCancelable(true);
        final AlertDialog dialog = builder.create();
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        setDialog(dialog);
        for (int i = 0; i < list.size(); i++) {
            TextView textView = new TextView(context);
            textView.setPadding(0, 50, 0, 50);
            textView.setTextSize(18);
            textView.setTextColor(Color.parseColor("#666666"));
            textView.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams params =   new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(params);
            Category category = list.get(i);
            textView.setText(category.getName());
            textView.setTag(category.getId());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if (onClickListener != null)
                        onClickListener.onClick(view);
                }
            });
            ll_add.addView(textView);
            View line = new View(context);
            line.setLayoutParams(params);
            line.setPadding(0,0,0,1);
            line.setBackgroundColor(context.getResources().getColor(R.color.gray_db));
            ll_add.addView(line);
        }

    }

    /**
     * 统一设置dialog宽度
     *
     * @param dialog
     */
    private static void setDialog(AlertDialog dialog) {
        WindowManager m = dialog.getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = d.getWidth() / 5 * 4; //设置dialog的宽度为当前手机屏幕的宽度
        dialog.getWindow().setAttributes(p);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
    }
}
