package com.ktrental.product;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ktrental.R;

public class Menu_Layout extends RelativeLayout implements View.OnClickListener {

	Context context;
	RelativeLayout back;
	LinearLayout menu_back;
	TextView menu1;
	TextView menu2;
	TextView menu3;
//	TextView menu4;

	public Menu_Layout(Context context) {
		super(context);
		initMarbleView(context);
	}

	public Menu_Layout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initMarbleView(context);
	}

	@Override
	protected void onAnimationStart() {
		super.onAnimationStart();
	}

	@Override
	protected void onAnimationEnd() {
		clearAnimation();
		super.onAnimationEnd();
	}

	private void initMarbleView(Context context) {
		this.context = context;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		View v = li.inflate(R.layout.menu_layout, this, false);
		addView(v);
		back = (RelativeLayout) findViewById(R.id.back);
		menu_back = (LinearLayout) findViewById(R.id.menu_back);
		// menu1 = (TextView) findViewById(R.id.menu1);
		// menu1.setOnClickListener(this);
		menu2 = (TextView) findViewById(R.id.menu2);
		menu2.setOnClickListener(this);
		menu3 = (TextView) findViewById(R.id.menu3);
		menu3.setOnClickListener(this);
//		menu4 = (TextView) findViewById(R.id.menu4);
//		menu4.setOnClickListener(this);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		Animation fade_in = AnimationUtils.loadAnimation(context,
				R.anim.slidemenu_fadein);
		back.setAnimation(fade_in);
		startLayoutAnimation();

		Animation slide_in = AnimationUtils.loadAnimation(context,
				R.anim.slidemenu_in_left);
		menu_back.setAnimation(slide_in);
		startLayoutAnimation();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.menu1:
		// Toast.makeText(context, "메뉴1", 0).show();
		// break;
		case R.id.menu2:
			Toast.makeText(context, "메뉴2", 0).show();
			break;
		case R.id.menu3:
			Toast.makeText(context, "메뉴3", 0).show();
			break;
//		case R.id.menu4:
//			Toast.makeText(context, "메뉴4", 0).show();
//			break;
		}

	}

}
