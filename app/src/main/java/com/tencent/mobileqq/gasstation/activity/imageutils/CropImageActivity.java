package com.tencent.mobileqq.gasstation.activity.imageutils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tencent.mobileqq.gasstation.R;


public class CropImageActivity extends Activity implements OnClickListener {

	private CropImageView mImageView;
	private Bitmap mBitmap;

	private CropImage mCrop;
	private Button mSave, mCancel, rotateLeft, rotateRight;
	private String mPath;
	public int screenWidth = 0;
	public int screenHeight = 0;

	private ProgressBar mProgressBar;

	public static final int SHOW_PROGRESS = 2000;

	public static final int REMOVE_PROGRESS = 2001;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case SHOW_PROGRESS:
				mProgressBar.setVisibility(View.VISIBLE);
				break;
			case REMOVE_PROGRESS:
				mHandler.removeMessages(SHOW_PROGRESS);
				mProgressBar.setVisibility(View.INVISIBLE);
				break;
			}

		}
	};

	public static int mWidth;

	public static int mHeight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		float scale = getIntent().getFloatExtra("scale", (float) 1.0);

		mWidth = getWindowManager().getDefaultDisplay().getWidth();
		mHeight = (int) (mWidth / getIntent().getFloatExtra("scale",
				(float) 1.0));

		Log.i("CropImageActivity", scale + " " + mWidth + " " + mHeight);
		
		
		setContentView(R.layout.gl_modify_avatar);

		init();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mBitmap != null) {
			mBitmap = null;
		}
	}

	private void init() {
		getWindowWH();
		mPath = getIntent().getStringExtra("path");
		mImageView = (CropImageView) findViewById(R.id.gl_modify_avatar_image);
		mSave = (Button) this.findViewById(R.id.gl_modify_avatar_save);
		mCancel = (Button) this.findViewById(R.id.gl_modify_avatar_cancel);
		rotateLeft = (Button) this
				.findViewById(R.id.gl_modify_avatar_rotate_left);
		rotateRight = (Button) this
				.findViewById(R.id.gl_modify_avatar_rotate_right);
		mSave.setOnClickListener(this);
		mCancel.setOnClickListener(this);
		rotateLeft.setOnClickListener(this);
		rotateRight.setOnClickListener(this);
		try {
			mBitmap = createBitmap(mPath, screenWidth, screenHeight);
			if (mBitmap == null) {
				Toast.makeText(CropImageActivity.this, "没有找到图片", Toast.LENGTH_SHORT).show();
				finish();
			} else {
				resetImageView(mBitmap);
			}
		} catch (Exception e) {
			Toast.makeText(CropImageActivity.this, "没有找到图片", Toast.LENGTH_SHORT).show();
			finish();
		}
		addProgressbar();
	}


	private void getWindowWH() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
	}

	private void resetImageView(Bitmap b) {
		mImageView.clear();
		mImageView.setImageBitmap(b);
		mImageView.setImageBitmapResetBase(b, true);
		mCrop = new CropImage(this, mImageView, mHandler);
		mCrop.crop(b);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gl_modify_avatar_cancel:
			// mCrop.cropCancel();
			finish();
			break;
		case R.id.gl_modify_avatar_save:
			String path = mCrop.saveToLocal(mCrop.cropAndSave());
			Intent intent = new Intent();
			intent.putExtra("path", path);
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.gl_modify_avatar_rotate_left:
			mCrop.startRotate(270.f);
			break;
		case R.id.gl_modify_avatar_rotate_right:
			mCrop.startRotate(90.f);
			break;

		}
	}

	protected void addProgressbar() {
		mProgressBar = new ProgressBar(this);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		addContentView(mProgressBar, params);
		mProgressBar.setVisibility(View.INVISIBLE);
	}

	public Bitmap createBitmap(String path, int w, int h) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;
			int srcHeight = opts.outHeight;
			int destWidth = 0;
			int destHeight = 0;
			double ratio = 0.0;
			if (srcHeight == -1 || srcWidth == -1){
				ExifInterface exifInterface = new ExifInterface(path);
				int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度
				int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
				srcWidth = width;
				srcHeight = height;
			}
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			newOpts.inSampleSize = (int) ratio + 1;
			newOpts.inJustDecodeBounds = false;
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			return BitmapFactory.decodeFile(path, newOpts);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}
}