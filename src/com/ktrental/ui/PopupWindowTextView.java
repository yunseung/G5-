package com.ktrental.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class PopupWindowTextView extends TextView {

	private OnLayoutListener mOnLayoutListener;

	private int[] mInventoryLocation = null;

	public OnLayoutListener getOnLayoutListener() {
		return mOnLayoutListener;
	}

	public void setOnLayoutListener(OnLayoutListener onLayoutListener) {
		mOnLayoutListener = onLayoutListener;
	}

	public interface OnLayoutListener {
		void onLayout();
	}

	public PopupWindowTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PopupWindowTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public PopupWindowTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		mInventoryLocation = new int[2];
		getLocationOnScreen(mInventoryLocation);

	}

	public int[] getInventoryLocation() {
		return mInventoryLocation;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		mInventoryLocation = new int[2];
		getLocationOnScreen(mInventoryLocation);
		if (mOnLayoutListener != null)
			mOnLayoutListener.onLayout();
	}

	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onScrollChanged(int horiz, int vert, int oldHoriz,
			int oldVert) {
		// TODO Auto-generated method stub
		super.onScrollChanged(horiz, vert, oldHoriz, oldVert);
	}

	@Override
	public boolean onPreDraw() {
		return super.onPreDraw();

	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasWindowFocus);
		mInventoryLocation = new int[2];
		getLocationOnScreen(mInventoryLocation);
	}

}
