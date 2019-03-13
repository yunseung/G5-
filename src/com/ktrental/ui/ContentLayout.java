package com.ktrental.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class ContentLayout extends FrameLayout {

	private boolean touchFlag = true;

	private OnContentSlide mContentSlide;

	public interface OnContentSlide {
		void onContentSlide();
	}
	
	public void setOnContentSlide(OnContentSlide aContentSlide){
		mContentSlide = aContentSlide;
	}

	public ContentLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ContentLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ContentLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		if (!touchFlag) {
			if (mContentSlide != null)
				mContentSlide.onContentSlide();
			return false;
		}

		return super.dispatchTouchEvent(ev);
	}

	public boolean isTouchFlag() {
		return touchFlag;
	}

	public void setTouchFlag(boolean touchFlag) {
		this.touchFlag = touchFlag;
	}

}
