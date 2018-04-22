package com.tencent.mobileqq.gasstation.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.blankj.utilcode.util.FileUtils;
import com.google.gson.Gson;
import com.tencent.mobileqq.gasstation.R;
import com.tencent.mobileqq.gasstation.activity.imageutils.CropImageActivity;
import com.tencent.mobileqq.gasstation.bean.Location;
import com.tencent.mobileqq.gasstation.bean.UserInfoBean;
import com.tencent.mobileqq.gasstation.configer.Configer;
import com.tencent.mobileqq.gasstation.db.UserInfoDb;
import com.tencent.mobileqq.gasstation.inteface.JSOnclickInterface;
import com.tencent.mobileqq.gasstation.utila.AtKeyBoardUp;
import com.tencent.mobileqq.gasstation.utila.JSInterface;

import org.kymjs.kjframe.ui.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Zhangchen on 2018/3/5.
 */

public class MainWebActivity extends BaseActivity implements JSOnclickInterface {

    private static final String TAG = "WebViewH5Activity";

    private static final int FLAG_CHOOSE_IMG = 5;// 从相册中选择
    private static final int FLAG_CHOOSE_PHONE = 6;// 拍照
    private static final int FLAG_MODIFY_FINISH = 7;// 结果
    public static final File FILE_LOCAL = new File(Configer.FILE_PIC_PATH);
    private static String localTempImageFileName;
    private String path;
    private Dialog dialog;
    private LocationClient mLocationClient;
    private BDLocationListener mBDLocaListener;

    private UserInfoDb db;

    @BindView(R.id.main_webview)
    WebView webView;

    private WebSettings webSettings;

    @Override
    protected int setLayout() {
        return R.layout.activity_mainweb;
    }

    @Override
    protected void setView() {
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        AtKeyBoardUp.assistActivity(this); //防止软件盘遮挡网页输入框内容
    }

    @SuppressLint("AddJavascriptInterface")
    @Override
    protected void setDeal() {

        webView.setWebViewClient(webViewClient);
//        webView.loadUrl("file:///android_asset/test.html");
        webView.loadUrl(Configer.LOGINHTTP);

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setBlockNetworkImage(false);//同步请求图片

        saveCacheData(webSettings);


        /**
         * 打开js交互接口
         */
        webView.addJavascriptInterface(new JSInterface(webView), "gasstation");

        webView.setWebChromeClient(new WebChromeClient());


        webView.loadUrl("javascript:show()");
    }

    private void saveCacheData(WebSettings webSettings) {
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);//允许DCOM
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @OnClick({})
    public void onClick(View view) {
        String content = "s";
        webView.loadUrl("javascript:show()");
    }

    WebViewClient webViewClient = new WebViewClient() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            view.loadUrl(url);
            return false;
        }
    };

    /**
     * js中调用本地相册
     */
    @Override
    public void onClickCamers() {
        getPhoto();
    }

    /**
     * js中地理位置获取
     */
    @Override
    public void onClickLocation(double x,double y) {
        if (isAvilible(this, "com.baidu.BaiduMap")) {
            ToaS(this, "即将用百度地图打开导航");
            Uri mUri = Uri.parse("geo:" + 116.243614 + "," + 39.900469 + "?q=" + "中国北京市石景山区雕塑园南街");
            Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
            startActivity(mIntent);
        } else if (isAvilible(this, "com.autonavi.minimap")) {
            ToaS(this, "即将用高德地图打开导航");
            Uri mUri = Uri.parse("geo:" + 116.243614 + "," + 39.900469 + "?q=" + "中国北京市石景山区雕塑园南街");
            Intent intent = new Intent("android.intent.action.VIEW", mUri);
            startActivity(intent);
        } else {
            ToaS(this, "请安装第三方地图方可导航");
            return;
        }
    }

    //获取经纬度
    @Override
    public String onClickGetLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        mLocationClient = new LocationClient(this);
        mBDLocaListener = new MyBDLocationListener();
        mLocationClient.registerLocationListener(mBDLocaListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("db0911");
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        option.setNeedDeviceDirect(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        return  null;
    }

    /**
     * 存储账号信息
     */
    @Override
    public void onClickSaveCookei(String s) {
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setPassword("123");
        userInfoBean.setUserid("hahah");
        db = new UserInfoDb(MainWebActivity.this);
        List<UserInfoBean> data = db.getData(UserInfoBean.class);
        if (data != null && data.size() > 0) {
            db.upData(userInfoBean, "id=0");
        } else {
            db.saveData(userInfoBean);
        }
    }

    /**
     * 注册成功弹窗
     */
    @Override
    public void onClickRegistDialog() {

    }

    /**
     * 清除缓存
     */
    @Override
    public void onClickDeleteCookei() {
        db = new UserInfoDb(MainWebActivity.this);
        db.deleteUserInfo(UserInfoBean.class,"id=0");
    }

    /**
     * 打开拍照功能
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private void getPhoto() {
        dialog = new Dialog(MainWebActivity.this, R.style.MyDialog);
        View v = LayoutInflater.from(MainWebActivity.this).inflate(R.layout.item_photodialog, null);
        final LinearLayout lin_main = (LinearLayout) v.findViewById(R.id.lin_main);
        Button bt_photo = (Button) v.findViewById(R.id.bt_photo);
        Button bt_img = (Button) v.findViewById(R.id.bt_imgs);
        Button bt_finish = (Button) v.findViewById(R.id.bt_finish);

        final View view = lin_main;
        view.animate().translationY(0).setDuration(500);

        bt_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 调用系统的拍照功能
                openGamera();
            }
        });
        bt_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openPhoto();
            }
        });
        bt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", lin_main.getHeight() + 500);
                animator.setDuration(500);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }

                });
                animator.start();
            }
        });

        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        dialog.setContentView(v, layoutParams);
        dialog.show();
    }

    /**
     * 调用相机
     */
    private void openGamera() {
        if (ContextCompat.checkSelfPermission(MainWebActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(MainWebActivity.this,
                    new String[]{Manifest.permission.CAMERA}, 1);//1 can be another integer
        }


        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                localTempImageFileName = String.valueOf((new Date()).getTime())
                        + ".png";
                File filePath = FILE_LOCAL;
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }
                Intent intent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(filePath, localTempImageFileName);
                if (FileUtils.createOrExistsFile(f)) {
//                     localTempImgDir和localTempImageFileName是自己定义的名字
                    Uri u = Uri.fromFile(f);
                    intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                    startActivityForResult(intent, FLAG_CHOOSE_PHONE);
                } else {
                    ViewInject.toast("拍照失败");
                    finish();
                }
            } catch (Exception e) {
                //
            }
        }
    }

    /**
     * 打开相册
     */
    private void openPhoto() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, FLAG_CHOOSE_IMG);
    }

    /**
     * 回调事件处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FLAG_CHOOSE_IMG && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (!TextUtils.isEmpty(uri.getAuthority())) {
                    Cursor cursor = getContentResolver().query(uri,
                            new String[]{MediaStore.Images.Media.DATA},
                            null, null, null);
                    if (null == cursor) {
                        return;
                    }
                    cursor.moveToFirst();
                    String path = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    cursor.close();
                    Intent intent = new Intent(MainWebActivity.this, CropImageActivity.class);
                    intent.putExtra("path", path);
                    startActivityForResult(intent, FLAG_MODIFY_FINISH);
                } else {
                    Intent intent = new Intent(this, CropImageActivity.class);
                    intent.putExtra("path", uri.getPath());
                    startActivityForResult(intent, FLAG_MODIFY_FINISH);
                }
            }
        } else if (requestCode == FLAG_CHOOSE_PHONE && resultCode == RESULT_OK) {
            File f = new File(FILE_LOCAL, localTempImageFileName);
            boolean orExistsDir = FileUtils.createOrExistsFile(f);
            if (orExistsDir) {
                Intent it = new Intent(MainWebActivity.this, CropImageActivity.class);
                it.putExtra("path", f.getAbsolutePath());
                startActivityForResult(it, FLAG_MODIFY_FINISH);
            } else {
                ViewInject.toast("没有获取到照片，请重新拍照");
                finish();
            }

        } else if (requestCode == FLAG_MODIFY_FINISH && resultCode == RESULT_OK) {
            if (data != null) {
                path = data.getStringExtra("path");
                File file = new File(path);
                boolean orExistsDir = FileUtils.createOrExistsFile(file);
                if (!orExistsDir) {
                    ViewInject.toast("没有获取到照片，请重新选取");
                    return;
                }
                webView.loadUrl("javascript:alertMessage(\" " + path + "\")");
            }
        }
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    private boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyBDLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null){
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String address = location.getAddrStr();
                Log.e(TAG, "address:" + address + " latitude:" + latitude
                        + " longitude:" + longitude + "---");
                String jingdu = String.valueOf(location.getLongitude());
                String weidu = String.valueOf(location.getLatitude());
                Location locatdata = new Location(jingdu,weidu);
                Gson gson = new Gson();
                String data = gson.toJson(locatdata);
                Log.d(TAG,data);
                if (mLocationClient.isStarted()) {
                    // 获得位置之后停止定位
                    mLocationClient.stop();
                }
            }
        }
    }
}
