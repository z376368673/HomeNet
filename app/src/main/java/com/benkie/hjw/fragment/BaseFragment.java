package com.benkie.hjw.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.benkie.hjw.net.Http;

import butterknife.ButterKnife;

/**
 * Created by 37636 on 2018/1/20.
 */

public class BaseFragment extends Fragment implements View.OnClickListener{
    protected View view;
    protected Activity mActivity;
    protected Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        context = getContext();

    }


    boolean isLogin(){
        return true;
    }
    @Override
    public void onClick(View view) {

    }
}
