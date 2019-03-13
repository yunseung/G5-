package com.ktrental.popup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

public class FrameDialog extends Dialog {

	private int mLayoutId;
	private FragmentManager mFragmentManager;

	public FrameDialog(Context context, int layoutId,
			FragmentManager fragmentManager) {
		super(context);

		mLayoutId = layoutId;
		mFragmentManager = fragmentManager;
		setContentView(mLayoutId);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
//		FragmentTransaction ft = mFragmentManager.beginTransaction();
//		ft.add(R.id.ll_frame, new MonthProgressFragment(
//				MonthProgressFragment.class.getName(), null));
//		ft.commit();
	}

}
