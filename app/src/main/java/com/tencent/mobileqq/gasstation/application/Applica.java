package com.tencent.mobileqq.gasstation.application;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.tencent.mobileqq.gasstation.configer.Configer;

import java.io.File;

/**
 * Created by Zhangchen on 2018/2/28.
 */

public class Applica extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);
        initFile(Configer.FILE_MAIN_PATH);
    }

    /**
     * 初始化应用根文件
     */
    private void initFile(String filePath) {
        File defaultMedia = new File(filePath);
        if (!defaultMedia.exists()) {
            defaultMedia.mkdirs();
        }

        File defaultIMG = new File(filePath + "/MSG/TEAM");
        if (!defaultIMG.exists()) {
            defaultIMG.mkdirs();
        }
        //初始图片目录
        File imageFile = new File(Configer.FILE_PIC_PATH);
        if (!imageFile.exists()) {
            imageFile.mkdirs();
        }
    }
}
