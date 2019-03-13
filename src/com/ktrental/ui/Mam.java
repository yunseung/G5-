package com.ktrental.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ktrental.R;

public class Mam extends RelativeLayout {

	private ImageView cover;
	private ImageView gradation;
	private float DEGREE;
	private TextView tv;

	public Mam(Context context, AttributeSet attrs) {
		super(context, attrs);
		cover = new ImageView(context);
		gradation = new ImageView(context);
		tv = new TextView(context);
		setDegree(100);
	}

	public void setDegree(float degree) {
		DEGREE = degree;
		showGraph();
	}

	public void showGraph() {
		cover.setImageResource(R.drawable.circle_garph01);

		Bitmap gra = BitmapFactory.decodeResource(getResources(),
				R.drawable.circle_garph02);

		int width = gra.getWidth();
		int height = gra.getHeight();

		Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bm);
		RectF rectf = new RectF(0, 0, width, height);
		c.drawBitmap(gra, 0, 0, new Paint());
		Paint paint = new Paint();
		paint.setColor(Color.parseColor("#565656"));
		c.drawArc(rectf, 270, ((360 - (float) (3.6) * DEGREE)) * -1, true,
				paint);
		gradation.setImageBitmap(bm);

		LayoutParams tv_param = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// tv_param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		tv_param.addRule(RelativeLayout.CENTER_IN_PARENT);
		// tv_param.setMargins(66, 67, 0, 0);
		tv.setLayoutParams(tv_param);
		tv.setText((int) DEGREE + " %");

		if (DEGREE < 99) {
			tv.setPadding(0, -8, 0, 0);
			tv.setTextSize(28);
		} else {
			tv.setPadding(0, -2, 0, 0);
			tv.setTextSize(24);
		}

		removeAllViews();
		addView(gradation);
		addView(cover);
		addView(tv);

		gra = null;
		bm = null;
		paint = null;
	}

}
