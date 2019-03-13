package com.ktrental.calendar;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
/**
 * fling() 조절
 * @author kgh
 *
 */
public class FlickingSimpleOnGestureListener extends SimpleOnGestureListener {

	private static final int SWIPE_MIN_DISTANCE = 5;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	public static final int RIGHT_TO_LEFT = 0;
	public static final int LEFT_TO_RIGHT = 1;
	
	private OnSwipListener mOnSwipListener;
	
	public void setOnSwipListener(OnSwipListener aOnSwipListener){
		mOnSwipListener = aOnSwipListener;
	}
	
	public interface OnSwipListener{
		/**
		 * 플리킹이 되면 현재 옮겨지는 플리킹에 해당하는 좌우 혹은 우좌 값을  리턴 및 콜백해준다.
		 * 
		 * @param int 콜백해주워야 하는 좌에서 우 혹은 우에서 좌 값을 미리 선언된  RIGHT_TO_LEFT 또는 LEFT_TO_RIGHT 값을 리턴.
		 */
		void onSwip(int aSwip);
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		
		try {
			
			float rightToleft = (e1.getX() - e2.getX());
			float leftToright = (e2.getX() - e1.getX());

			// right to left
			if (rightToleft > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

				mOnSwipListener.onSwip(RIGHT_TO_LEFT);				
				return true;
			}
			// left to right
			else if (leftToright > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				mOnSwipListener.onSwip(LEFT_TO_RIGHT);				
				return true;
			}
		} catch (Exception e) {
		
		}
		return false;
	}

}