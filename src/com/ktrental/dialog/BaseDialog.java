package com.ktrental.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

public class BaseDialog extends Dialog {

	protected View mRootView;

	public BaseDialog(Context context) {
		super(context);
	}

	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);

		LayoutInflater inflater = getLayoutInflater();
		mRootView = inflater.inflate(layoutResID, null);
	}

	@Override
	public void dismiss() {

		unbindDrawables(mRootView);

		super.dismiss();
	}

	private void unbindDrawables(View view) {
		if (view != null) {
			if (view.getBackground() != null) {

				view.getBackground().setCallback(null);
			}
			if (view instanceof ImageView) {

				ImageView iv = (ImageView) view;

				Drawable drawable = iv.getDrawable();

				if (drawable instanceof BitmapDrawable) {
					BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
					Bitmap bitmap = bitmapDrawable.getBitmap();
					if (bitmap != null) {

						bitmap.recycle();
						// bitmap = null;
					}
				}
				iv.setImageDrawable(null);

			}
		}
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			if (!(view instanceof AdapterView))
				((ViewGroup) view).removeAllViews();
		}

		view = null;
	}

}
