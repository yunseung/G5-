package com.ktrental.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ktrental.R;
import com.ktrental.common.DEFINE;
import com.ktrental.util.kog;

public class FingerPaint_View extends View {

	private Paint mPaint;
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Path mPath;
	private Paint mBitmapPaint;
	
	boolean isDraw;
	
	//myung 20131121 ADD 2560 대응
//	private int tempX = 1004;
//	private int tempY = 406;

	public FingerPaint_View(Context c, AttributeSet attr) {
		super(c, attr);
		
		isDraw = false;
		
		//myung 20131121 ADD 2560 대응
//		if(DEFINE.DISPLAY.equals("2560")){
//			tempX *= 2;
//			tempY *= 2;
//		}
		
		mPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(0xFF000000);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(12);
	}

	Bitmap background;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.sign_bg);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		//myung 20131121 UPDATE 2560 대응
//		if(DEFINE.DISPLAY.equals("2560")){
//			tempX *= 2;
//			tempY *= 2;
//		}
//		setMeasuredDimension(tempX, tempY);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// Bitmap background = BitmapFactory.decodeResource(getResources(),
		// R.drawable.sign01);

		if(mBitmap != null){
			mBitmap.recycle();
		}
		mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
//		if(DEFINE.DISPLAY.equals("2560")){
//			BitmapFactory.Options options = new BitmapFactory.Options();
//			options.inSampleSize = 4;
//			Bitmap resized = Bitmap.createScaledBitmap(mBitmap, tempX, tempY, true);
//			mCanvas = new Canvas(resized);
//		}else
			mCanvas = new Canvas(mBitmap);
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT);
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		canvas.drawPath(mPath, mPaint);
	}

	public Bitmap getBitmap() {
		if(isDraw)
			return mBitmap;
		else
			return null;
	}
	
	public void reset(){
//		mBitmap = Bitmap.createBitmap(tempX, tempY, Bitmap.Config.ARGB_8888);
		mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
	}

	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;

	private void touch_start(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
		
		
	}

	private void touch_up() {
		mPath.lineTo(mX, mY);
		mCanvas.drawPath(mPath, mPaint);
		if(!mPath.isEmpty())
			isDraw = true;
		mPath.reset();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touch_start(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touch_up();
			invalidate();
			break;
		}
		return true;
	}

	
}
