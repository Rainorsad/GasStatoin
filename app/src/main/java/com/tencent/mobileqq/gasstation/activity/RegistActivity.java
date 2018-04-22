package com.tencent.mobileqq.gasstation.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.blankj.utilcode.util.FileUtils;
import com.bumptech.glide.Glide;
import com.tencent.mobileqq.gasstation.R;
import com.tencent.mobileqq.gasstation.activity.imageutils.CropImageActivity;
import com.tencent.mobileqq.gasstation.configer.Configer;
import com.tencent.mobileqq.gasstation.view.MyItemOrition;

import org.kymjs.kjframe.ui.ViewInject;

import java.io.File;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Zhangchen on 2018/3/1.
 */

public class RegistActivity extends BaseActivity{

    private static final int FLAG_CHOOSE_IMG = 5;// 从相册中选择
    private static final int FLAG_CHOOSE_PHONE = 6;// 拍照
    private static final int FLAG_MODIFY_FINISH = 7;// 结果
    public static final File FILE_LOCAL = new File(Configer.FILE_PIC_PATH);
    private static String localTempImageFileName;
    private String path;
    private int imgindex = 0;//0表示未选择，1234表示对应选择的位置

    @BindView(R.id.rela_titlemain)
    RelativeLayout relaTitlemain;
    @BindView(R.id.img_exit)
    ImageView imgExit;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    //注册主体
    @BindView(R.id.lin_registmain)
    LinearLayout linRegistmain;
    @BindView(R.id.edit_name)
    EditText getEditRegistname; //注册账号
    @BindView(R.id.edit_shoppass)
    EditText editShoppass; //商家注册密码
    @BindView(R.id.view_shoppass)
    View viewShopPass;
    @BindView(R.id.edit_shoptconfirmpass)
    EditText editShopconfirmpass; //商家确认密码
    @BindView(R.id.view_shopconfirmpass)
    View viewShopConfirmPass;
    @BindView(R.id.edit_nickname)
    EditText editNickName; //司机姓名
    @BindView(R.id.view_nickname)
    View viewNickname;
    @BindView(R.id.edit_idcard)
    EditText editIdCard; //司机身份证号
    @BindView(R.id.view_idcard)
    View viewIdCard;
    @BindView(R.id.edit_carcard)
    EditText editCarCard; //司机车牌
    @BindView(R.id.view_carcard)
    View viewCarCard;
    @BindView(R.id.regist_tvtype)
    TextView registTvtype; //注册类型
    @BindView(R.id.regist_imgselecttype)
    ImageSwitcher registImgselecttype; //类型选择按钮
    @BindView(R.id.regist_but)
    Button registBut;  //注册确认按钮
    @BindView(R.id.edit_diverpass)
    EditText editDiverPass; //司机密码
    @BindView(R.id.view_diverpass)
    View viewDiverPass;
    @BindView(R.id.edit_diverconfirmpass)
    EditText editDiverConfirmPass; //司机确认密码
    @BindView(R.id.view_diverconfirmpass)
    View viewDiverConfirmPass;
    @BindView(R.id.lin_divercode)
    LinearLayout linDiverCode; //司机验证码主体
    @BindView(R.id.edit_divercode)
    EditText editDiverCode; //司机验证码
    @BindView(R.id.but_divercode)
    Button butDiverCode; //司机验证码按钮
    @BindView(R.id.regist_agreement)
    LinearLayout registAgreement; //选择协议主体
    @BindView(R.id.check_agreement)
    CheckBox checkAgreement; //选择协议按钮
    @BindView(R.id.text_agreement)
    TextView tvAgreement; //协议文本

    //注册次体
    @BindView(R.id.lin_registsecond)
    LinearLayout linRegistSecond; //注册次体模块
    @BindView(R.id.imgone)
    ImageView imgOne;
    @BindView(R.id.imgtwo)
    ImageView imgTwo;
    @BindView(R.id.registlinsecond)
    LinearLayout registLinSecond; //第二部分图片模块
    @BindView(R.id.imgthree)
    ImageView imgThree;
    @BindView(R.id.imgfour)
    ImageView imgFour;
    @BindView(R.id.edit_shopphone)
    EditText editRegistPhone; //注册手机号
    @BindView(R.id.edit_shopcode)
    EditText editShopCode; //验证码
    @BindView(R.id.but_shopcode)
    Button butShopCode; //获取验证码按钮
    @BindView(R.id.regist_sure)
    Button registSure; //确认按钮

    private int imgIndex=1;//imageswitch下标表示，1为向右箭头，2为向下箭头
    private PopupWindow popupWindow;
    private Dialog dialog;//照片选择弹框

    @Override
    protected int setLayout() {
        return R.layout.activity_regist;
    }

    @Override
    protected void setView() {
        tvTitle.setText("立即注册");
        imgSwitch();
        diverModel();
    }

    @Override
    protected void setDeal() {

    }

    @OnClick({R.id.img_exit, R.id.regist_imgselecttype, R.id.regist_but,R.id.but_shopcode,R.id.regist_sure
            ,R.id.but_divercode,R.id.imgone,R.id.imgtwo,R.id.imgthree,R.id.imgfour})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_exit:
                //退回操作
                this.finish();
                break;
            case R.id.regist_imgselecttype:
                //注册选择类型
                if (imgIndex == 1){
                    imgIndex = 2;
                    registImgselecttype.setBackgroundResource(R.mipmap.arrow_up);
                    showPopuWindow();
                }else {
                    imgIndex = 1;
                    registImgselecttype.setBackgroundResource(R.mipmap.arrow_down_b);
                    popupWindow.dismiss();
                }
                break;
            case R.id.regist_but:
                //注册下一步
                registSecondModel();
                break;
            case R.id.but_shopcode:
                //商家注册获得验证码
                shopCode();
                break;
            case R.id.but_divercode:
                //司机注册获得验证码
                diverCode();
                break;
            case R.id.regist_sure:
                //完成注册
                break;
            case R.id.imgone:
                getPhoto(1);
                break;
            case R.id.imgtwo:
                getPhoto(2);
                break;
            case R.id.imgthree:
                getPhoto(3);
                break;
            case R.id.imgfour:
                getPhoto(4);
                break;
        }
    }

    /**
     * 司机验证码
     */
    private void diverCode() {
        TextTimeCount time = new TextTimeCount(60000, butDiverCode);
        time.start();
    }

    /**
     * 商家验证码
     */
    private void shopCode() {
        TextTimeCount time = new TextTimeCount(60000, butShopCode);
        time.start();
    }

    /**
     * 司机身份注册
     */
    private void diverModel(){
        editShoppass.setVisibility(View.GONE);
        viewShopPass.setVisibility(View.GONE);
        editShopconfirmpass.setVisibility(View.GONE);
        viewShopConfirmPass.setVisibility(View.GONE);
        editNickName.setVisibility(View.VISIBLE);
        viewNickname.setVisibility(View.VISIBLE);
        editIdCard.setVisibility(View.VISIBLE);
        viewIdCard.setVisibility(View.VISIBLE);
        editCarCard.setVisibility(View.VISIBLE);
        viewCarCard.setVisibility(View.VISIBLE);
        editDiverPass.setVisibility(View.VISIBLE);
        viewDiverPass.setVisibility(View.VISIBLE);
        editDiverConfirmPass.setVisibility(View.VISIBLE);
        viewDiverConfirmPass.setVisibility(View.VISIBLE);
        linDiverCode.setVisibility(View.VISIBLE);
        registAgreement.setVisibility(View.VISIBLE);
    }

    /**
     * imgSwitch事件处理
     */
    private void imgSwitch() {
        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);

        if (registImgselecttype == null) {
            registImgselecttype.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    ImageView img = new ImageView(RegistActivity.this);
                    return img;
                }
            });
            registImgselecttype.setInAnimation(in);
            registImgselecttype.setOutAnimation(out);
        }
    }

    /**
     * 注册第二部分模块
     */
    private void registSecondModel() {
        linRegistmain.setVisibility(View.GONE);
        linRegistSecond.setVisibility(View.VISIBLE);

        if (registTvtype.getText().toString().equals("物流公司")){
            registLinSecond.setVisibility(View.GONE);
        }else {
            registLinSecond.setVisibility(View.VISIBLE);
        }
    }

    /**
     * popuwindow展示
     */
    private void showPopuWindow(){

        if (popupWindow == null) {
            MyItemOrition ms = new MyItemOrition(MyItemOrition.VERTICAL);
            ms.setColor(0xFFD3D3D3);
            ms.setHeight(2);
            RecyclerView recyclerView = new RecyclerView(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(ms);

            recyclerView.setAdapter(new ModelAdapter());
            popupWindow = new PopupWindow(recyclerView, registTvtype.getWidth(), 230);
        }
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_spinner_listview_background));
        popupWindow.showAsDropDown(registTvtype,0,5);


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                imgIndex = 1;
                registImgselecttype.setBackgroundResource(R.mipmap.arrow_down_b);
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 打开拍照功能
     * @param i
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private void getPhoto(int i) {
        imgindex = i;
        dialog = new Dialog(RegistActivity.this, R.style.MyDialog);
        View v = LayoutInflater.from(RegistActivity.this).inflate(R.layout.item_photodialog, null);
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
        if (ContextCompat.checkSelfPermission(RegistActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(RegistActivity.this,
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
                    Intent intent = new Intent(RegistActivity.this, CropImageActivity.class);
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
                Intent it = new Intent(RegistActivity.this, CropImageActivity.class);
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
                switch (imgindex){
                    case 1:
                        Glide.with(RegistActivity.this).load(file).centerCrop().into(imgOne);
                        break;
                    case 2:
                        Glide.with(RegistActivity.this).load(file).centerCrop().into(imgTwo);
                        break;
                    case 3:
                        Glide.with(RegistActivity.this).load(file).centerCrop().into(imgThree);
                        break;
                    case 4:
                        Glide.with(RegistActivity.this).load(file).centerCrop().into(imgFour);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 验证码倒计时
     */
    class TextTimeCount extends CountDownTimer {
        private Button tv;

        public TextTimeCount(long millisInFuture, Button tv) {
            super(millisInFuture, 1000);
            this.tv = tv;
        }

        @Override
        public void onFinish() {// 计时完毕
            tv.setText("获取验证码");
            tv.setTextColor(getResources().getColor(R.color.A1A1A1));
            tv.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            tv.setClickable(false);//防止重复点击
            tv.setTextColor(getResources().getColor(R.color.red));
            tv.setText( millisUntilFinished / 1000 + "秒" );
        }
    }

    class ModelAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_textview,parent,false);
            ModelHolder holder = new ModelHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ModelHolder modelHolder = (ModelHolder) holder;
            if (position == 0){
                modelHolder.tv.setText("加油站");
            }else if (position == 1){
                modelHolder.tv.setText("加气站");
            }else if (position == 2){
                modelHolder.tv.setText("物流公司");
            }

            modelHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    registTvtype.setText(modelHolder.tv.getText().toString());
                    imgIndex = 1;
                    registImgselecttype.setBackgroundResource(R.mipmap.arrow_down_b);
                    popupWindow.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return 3;
        }

        class ModelHolder extends RecyclerView.ViewHolder {
            TextView tv;
            public ModelHolder(View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.item_tv);
            }
        }
    }
}
