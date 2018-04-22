package com.tencent.mobileqq.gasstation.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * Created by Zhangchen on 2018/2/27.
 */

public abstract class BaseActivity extends AppCompatActivity{
    protected Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());
        ButterKnife.bind(this);
        setView();
        setDeal();
    }

    protected abstract int setLayout();
    //初始化控件'
    protected abstract void setView();
    //处理逻辑事件
    protected abstract void setDeal();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //弹出框短时间内容显示
    public void ToaS(Context context, String s){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }
    //弹出框长时间显示
    public void ToaL(Context context,String s){
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
    }
}
