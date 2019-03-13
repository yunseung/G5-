package com.ktrental.popup;

import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;

import com.ktrental.R;
import com.ktrental.common.DEFINE;
import com.ktrental.dialog.Menu3_Resist_Dialog;

public class CalendarPopup extends PopupWindows implements OnDismissListener {
	private View mRootView;
	private ImageView mArrowUp;
	private ImageView mArrowDown;
	private LayoutInflater mInflater;
	private ViewGroup mTrack;
	private Context context;

	private OnDismissListener mDismissListener;
	private boolean mDidAction;

	private int mAnimStyle;
	private int rootWidth = 0;

	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;

	public static final int ANIM_GROW_FROM_LEFT = 1;
	public static final int ANIM_GROW_FROM_RIGHT = 2;
	public static final int ANIM_GROW_FROM_CENTER = 3;
	public static final int ANIM_REFLECT = 4;
	public static final int ANIM_AUTO = 5;

	private CalendarCustom calv;
	
    public CalendarCustom getCalv()
        {
        return calv;
        }

    
    public void setCalv(CalendarCustom calv)
        {
        this.calv = calv;
        }

    private int DATE = Integer.MAX_VALUE;
	private int MODE = Integer.MAX_VALUE;

	private ImageView iv_done;
	
	private Menu3_Resist_Dialog mrd;
	private MovementDialog md;

	public CalendarPopup(Context context) {
		super(context);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		setRootViewId(R.layout.calendarpopup);

		mAnimStyle = ANIM_AUTO;

		this.context = context;

		setOnDismissListener(this);
	}
	public CalendarPopup(Context context, Menu3_Resist_Dialog mrd)
	    {
	    this(context);
        this.mrd = mrd;
        }
	
	public CalendarPopup(Context context, MovementDialog md)
        {
        this(context);
        this.md = md;
        }

	public CalendarPopup(Context context, String date, int mode) {
		this(context);

		this.DATE = Integer.parseInt(date);
		this.MODE = mode;
	}

	public void setRootViewId(int id) {
		mRootView = (ViewGroup) mInflater.inflate(id, null);
		mTrack = (ViewGroup) mRootView.findViewById(R.id.tracks);
		mArrowDown = (ImageView) mRootView.findViewById(R.id.arrow_down);
		mArrowUp = (ImageView) mRootView.findViewById(R.id.arrow_up);
		calv = (CalendarCustom) mRootView.findViewById(R.id.calendar_id);
		iv_done = (ImageView) mRootView.findViewById(R.id.inventory_bt_done);

		mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		setContentView(mRootView);
	}

	public void setAnimStyle(int mAnimStyle) {
		this.mAnimStyle = mAnimStyle;
	}
	
	public ImageView getBt_Done()
	    {
	    return calv.getBt_done();
	    }

	public void show(View anchor) {

		calv.setButton(anchor, this, mrd);
		preShow();
		int xPos, yPos, arrowPos;
		mDidAction = false;
		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		Rect anchorRect = new Rect(location[0], location[1], location[0]
				+ anchor.getWidth(), location[1] + anchor.getHeight());
		// mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT));
		mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int rootHeight = mRootView.getMeasuredHeight();
		if (rootWidth == 0) {
			rootWidth = mRootView.getMeasuredWidth();
		}
		int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
		int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
		// automatically get X coord of popup (top left)
		if ((anchorRect.left + rootWidth) > screenWidth) {
			xPos = anchorRect.left - (rootWidth - anchor.getWidth());
			xPos = (xPos < 0) ? 0 : xPos;
			arrowPos = anchorRect.centerX() - xPos;
		} else {
			if (anchor.getWidth() > rootWidth) {
				xPos = anchorRect.centerX() - (rootWidth / 2);
			} else {
				xPos = anchorRect.left;
			}
			arrowPos = anchorRect.centerX() - xPos;
		}
		int dyTop = anchorRect.top;
		int dyBottom = screenHeight - anchorRect.bottom;
		boolean onTop = (dyTop > dyBottom) ? true : false;
		if (onTop) {
			if (rootHeight > dyTop) {
				yPos = 15;
			} else {
				yPos = anchorRect.top - rootHeight;
			}
		} else {
			yPos = anchorRect.bottom;
		}

		// mWindow.setWidth(800);
		showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowPos);
		setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
		int wid = 615;
//		if(DEFINE.DISPLAY.equals("2560"))
//		    {
		    wid = 615*2;
//		    }
		mWindow.setWidth(wid);
		mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
	}

	/**
	 * Show quickaction popup. Popup is automatically positioned, on top or
	 * bottom of anchor view.
	 * 
	 */
	public void show(View anchor, int popWidth, int popHeight, int[] location) {
		int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
		int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
		int valWidth = (screenWidth - popWidth) / 2;
		int valheight = (screenHeight - popHeight) / 2;
		calv.setButton(anchor, this, md);
		preShow();
		int xPos, yPos, arrowPos;
		mDidAction = false;
		// int[] location = new int[2];
		// anchor.getLocationOnScreen(location);
		Rect anchorRect = new Rect(location[0] - valWidth, location[1],
				location[0] + anchor.getWidth() - valWidth, location[1]
						- valheight + anchor.getHeight());
		// mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT));
		mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int rootHeight = mRootView.getMeasuredHeight();
		if (rootWidth == 0) {
			rootWidth = mRootView.getMeasuredWidth();
		}
		// automatically get X coord of popup (top left)
		if ((anchorRect.left + rootWidth) > screenWidth) {
			xPos = anchorRect.left - (rootWidth - anchor.getWidth());
			xPos = (xPos < 0) ? 0 : xPos;
			arrowPos = anchorRect.centerX() - xPos;
		} else {
			if (anchor.getWidth() > rootWidth) {
				xPos = anchorRect.centerX() - (rootWidth / 2);
			} else {
				xPos = anchorRect.left;
			}
			arrowPos = anchorRect.centerX() - xPos;
		}
		int dyTop = anchorRect.top;
		int dyBottom = screenHeight - anchorRect.bottom;
		boolean onTop = (dyTop > dyBottom) ? true : false;
		if (onTop) {
			if (rootHeight > dyTop) {
				yPos = 15;
			} else {
				yPos = anchorRect.top - rootHeight;
			}
		} else {
			yPos = anchorRect.bottom;
		}

		// mWindow.setWidth(800);
		showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowPos);
		setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
		int wid = 615;
//        if(DEFINE.DISPLAY.equals("2560"))
//            {
            wid = 615*2;
//            }
		mWindow.setWidth(wid);
		mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
	}

	private void setAnimationStyle(int screenWidth, int requestedX,
			boolean onTop) {
		int arrowPos = requestedX - mArrowUp.getMeasuredWidth() / 2;
		switch (mAnimStyle) {
		case ANIM_GROW_FROM_LEFT:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left
					: R.style.Animations_PopDownMenu_Left);
			break;
		case ANIM_GROW_FROM_RIGHT:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right
					: R.style.Animations_PopDownMenu_Right);
			break;
		case ANIM_GROW_FROM_CENTER:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center
					: R.style.Animations_PopDownMenu_Center);
			break;

		case ANIM_REFLECT:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Reflect
					: R.style.Animations_PopDownMenu_Reflect);
			break;
		case ANIM_AUTO:
			if (arrowPos <= screenWidth / 4) {
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left
						: R.style.Animations_PopDownMenu_Left);
			} else if (arrowPos > screenWidth / 4
					&& arrowPos < 3 * (screenWidth / 4)) {
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center
						: R.style.Animations_PopDownMenu_Center);
			} else {
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right
						: R.style.Animations_PopDownMenu_Right);
			}
			break;
		}
	}

	private void showArrow(int whichArrow, int requestedX) {
		final View showArrow = (whichArrow == R.id.arrow_up) ? mArrowUp
				: mArrowDown;
		final View hideArrow = (whichArrow == R.id.arrow_up) ? mArrowDown
				: mArrowUp;
		final int arrowWidth = mArrowUp.getMeasuredWidth();
		showArrow.setVisibility(View.VISIBLE);
		ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) showArrow
				.getLayoutParams();
		param.leftMargin = requestedX - arrowWidth / 2;
		hideArrow.setVisibility(View.INVISIBLE);
	}

	public void setOnDismissListener(OnDismissListener listener) {
		setOnDismissListener(this);
		mDismissListener = listener;
	}

	@Override
	public void onDismiss() {
		if (!mDidAction && mDismissListener != null) {
			mDismissListener.onDismiss(
					calv.getYYYY() + calv.getMM() + calv.getDD(), calv.getHh()
							+ calv.getMm());
		}
	}

	public interface OnActionItemClickListener {
		public abstract void onItemClick(CalendarPopup source, int pos,
                                         int actionId);
	}

	public interface OnDismissListener {
		public abstract void onDismiss(String YYMMDD, String hhmm);
	}

	public static class YyyyMmDdHhMm {
		public String YYYY;
		public String MM;
		public String DD;
		public String hh;
		public String mm;
		//myung 20131118 요일추가
		public String EE;
	}

	public ViewGroup getViewGroup()
        {
        return mTrack;
        }


    public void setHour(String string)
        {
        calv.setHour(string);
        }


    public void setMinute(String string)
        {
        calv.setMinute(string);
        }

    public void setStartTime(String string)
        {
        calv.setStartTime(string);
        }
}