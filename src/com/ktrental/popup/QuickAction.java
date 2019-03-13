package com.ktrental.popup;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.common.DEFINE;

/**
 * QuickAction dialog, shows action list as icon and text like the one in
 * Gallery3D app. Currently supports vertical and horizontal layout.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 * 
 *         Contributors: - Kevin Peck <kevinwpeck@gmail.com>
 */
public class QuickAction extends PopupWindows implements OnDismissListener {

	protected View mRootView;
	private ImageView mArrowUp;
	private ImageView mArrowDown;
	protected LayoutInflater mInflater;
	protected ViewGroup mTrack;
	protected ScrollView mScroller;
	private OnActionItemClickListener mItemClickListener;
	private List<ActionItem> actionItems = new ArrayList<ActionItem>();
	private boolean mDidAction;
	private int mChildPos;
	private int mInsertPos;
	private int mAnimStyle;
	private int mOrientation;
	private int rootWidth = 0;
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;
	public static final int ANIM_GROW_FROM_LEFT = 1;
	public static final int ANIM_GROW_FROM_RIGHT = 2;
	public static final int ANIM_GROW_FROM_CENTER = 3;
	public static final int ANIM_REFLECT = 4;
	public static final int ANIM_AUTO = 5;

	/**
	 * Constructor for default vertical layout
	 * 
	 * @param context
	 *            Context
	 */

	private boolean isShow = false;

	public QuickAction(Context context) {
		this(context, VERTICAL);
	}

	/**
	 * Constructor allowing orientation override
	 * 
	 * @param context
	 *            Context
	 * @param orientation
	 *            Layout orientation, can be vartical or horizontal
	 */
	public QuickAction(Context context, int orientation) {
		super(context);
		mOrientation = orientation;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (mOrientation == HORIZONTAL) {
			setRootViewId(R.layout.popup_horizontal);
		} else {
			setRootViewId(R.layout.popup_vertical);
		}
		mAnimStyle = ANIM_AUTO;
		mChildPos = 0;
		// initView(mTrack);
	}

	/**
	 * Get action item at an index
	 * 
	 * @param index
	 *            Index of item (position from callback)
	 * 
	 * @return Action Item at the position
	 */
	public ActionItem getActionItem(int index) {
		return actionItems.get(index);
	}

	/**
	 * Set root view.
	 * 
	 * @param id
	 *            Layout resource id
	 */
	public void setRootViewId(int id) {
		mRootView = (ViewGroup) mInflater.inflate(id, null);
		mTrack = (ViewGroup) mRootView.findViewById(R.id.tracks);
		mArrowDown = (ImageView) mRootView.findViewById(R.id.arrow_down);
		mArrowUp = (ImageView) mRootView.findViewById(R.id.arrow_up);
		mScroller = (ScrollView) mRootView.findViewById(R.id.scroller);
		// This was previously defined on show() method, moved here to prevent
		// force close that occured
		// when tapping fastly on a view to show quickaction dialog.
		// Thanx to zammbi (github.com/zammbi)
		mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		setContentView(mRootView);
	}

	public void setHeight(int height) {
		// myung 20131126 2560대응
//		 if(DEFINE.DISPLAY.equals("2560"))
		 height *= 2;
		 mScroller.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, height));
	}

	public void setArrowGone() {
		mArrowDown.setVisibility(View.GONE);
		mArrowUp.setVisibility(View.GONE);
	}

	/**
	 * Set animation style
	 * 
	 * @param mAnimStyle
	 *            animation style, default is set to ANIM_AUTO
	 */
	public void setAnimStyle(int mAnimStyle) {
		this.mAnimStyle = mAnimStyle;
	}

	/**
	 * Set listener for action item clicked.
	 * 
	 * @param listener
	 *            Listener
	 */
	public void setOnActionItemClickListener(OnActionItemClickListener listener) {
		mItemClickListener = listener;
	}

	/**
	 * Add action item
	 * 
	 * @param action
	 *            {@link ActionItem}
	 */
	public void addActionItem(ActionItem action) {
		actionItems.add(action);
		String title = action.getTitle();
		Drawable icon = action.getIcon();
		View container;
		if (mOrientation == HORIZONTAL) {
			container = mInflater.inflate(R.layout.action_item_horizontal, null);
		} else {
			container = mInflater.inflate(R.layout.action_item_vertical, null);
		}
		ImageView img = (ImageView) container.findViewById(R.id.iv_icon);
		TextView text = (TextView) container.findViewById(R.id.tv_title);
		if (icon != null) {
			img.setImageDrawable(icon);
		} else {
			img.setVisibility(View.GONE);
		}
		if (title != null) {
			text.setText(title);
		} else {
			text.setVisibility(View.GONE);
		}
		final int pos = mChildPos;
		final int actionId = action.getActionId();
		container.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mItemClickListener != null) {
					mItemClickListener.onItemClick(QuickAction.this, pos, actionId);
				}
				if (!getActionItem(pos).isSticky()) {
					mDidAction = true;
					dismiss();
				}
			}
		});
		container.setFocusable(true);
		container.setClickable(true);
		if (mOrientation == HORIZONTAL && mChildPos != 0) {
			View separator = mInflater.inflate(R.layout.horiz_separator, null);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.FILL_PARENT);
			separator.setLayoutParams(params);
			separator.setPadding(5, 0, 5, 0);
			mTrack.addView(separator, mInsertPos);
			mInsertPos++;
		}
		mTrack.addView(container, mInsertPos);
		mChildPos++;
		mInsertPos++;
	}

	/**
	 * Show quickaction popup. Popup is automatically positioned, on top or
	 * bottom of anchor view.
	 * 
	 */
	public void show(View anchor) {
		isShow = true;
		preShow();
		int xPos, yPos, arrowPos;
		mDidAction = false;
		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(),
				location[1] + anchor.getHeight());
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
				LayoutParams l = mScroller.getLayoutParams();
				l.height = dyTop - anchor.getHeight();
			} else {
				yPos = anchorRect.top - rootHeight;
			}
		} else {
			yPos = anchorRect.bottom;
			if (rootHeight > dyBottom) {
				LayoutParams l = mScroller.getLayoutParams();
				l.height = dyBottom;
			}
		}
		showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowPos);
		setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
		mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
	}

	/**
	 * Show quickaction popup. Popup is automatically positioned, on top or
	 * bottom of anchor view.
	 * 
	 */
	public void show(View anchor, int[] location) {
		isShow = true;
		preShow();
		int xPos, yPos, arrowPos;
		mDidAction = false;
		// int[] location = new int[2];
		// anchor.getLocationOnScreen(location);
		Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(),
				location[1] + anchor.getHeight());
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
				LayoutParams l = mScroller.getLayoutParams();
				l.height = dyTop - anchor.getHeight();
			} else {
				yPos = anchorRect.top - rootHeight;
			}
		} else {
			yPos = anchorRect.bottom;
			if (rootHeight > dyBottom) {
				LayoutParams l = mScroller.getLayoutParams();
				l.height = dyBottom;
			}
		}
		showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowPos);
		setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
		mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
	}

	/**
	 * Show quickaction popup. Popup is automatically positioned, on top or
	 * bottom of anchor view.
	 * 
	 */
	public void show(View anchor, int popWidth, int popHeight) {
		isShow = true;
		int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
		int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
		int valWidth = (screenWidth - popWidth) / 2;
		int valheight = (screenHeight - popHeight) / 2;
		preShow();
		int xPos, yPos, arrowPos;
		mDidAction = false;
		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		Rect anchorRect = new Rect(location[0] - valWidth, location[1], location[0] + anchor.getWidth() - valWidth,
				location[1] - valheight + anchor.getHeight());
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
			// if (anchor.getWidth() > rootWidth) {
			// xPos = anchorRect.centerX() - (rootWidth / 2);
			// } else {
			// xPos = anchorRect.left;
			// }
			xPos = anchorRect.centerX() - (rootWidth / 2);
			arrowPos = anchorRect.centerX() - xPos;
		}
		int dyTop = anchorRect.top;
		int dyBottom = screenHeight - anchorRect.bottom;
		boolean onTop = (dyTop > dyBottom) ? true : false;
		if (onTop) {
			if (rootHeight > dyTop) {
				yPos = 15;
				LayoutParams l = mScroller.getLayoutParams();
				l.height = dyTop - anchor.getHeight();
			} else {
				yPos = anchorRect.top - rootHeight;
			}
		} else {
			yPos = anchorRect.bottom;
			if (rootHeight > dyBottom) {
				LayoutParams l = mScroller.getLayoutParams();
				l.height = dyBottom;
			}
		}
		showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowPos);
		setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
		mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
	}

	/**
	 * Show quickaction popup. Popup is automatically positioned, on top or
	 * bottom of anchor view.
	 * 
	 */
	public void show(View anchor, int popWidth, int popHeight, int[] location) {
		isShow = true;
		int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
		int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
		int valWidth = (screenWidth - popWidth) / 2;
		int valheight = (screenHeight - popHeight) / 2;
		preShow();
		int xPos, yPos, arrowPos;
		mDidAction = false;
		// int[] location = new int[2];
		// anchor.getLocationOnScreen(location);
		Rect anchorRect = new Rect(location[0] - valWidth, location[1], location[0] + anchor.getWidth() - valWidth,
				location[1] - valheight + anchor.getHeight());
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
			// if (anchor.getWidth() > rootWidth) {
			// xPos = anchorRect.centerX() - (rootWidth / 2);
			// } else {
			// xPos = anchorRect.left;
			// }
			xPos = anchorRect.centerX() - (rootWidth / 2);
			arrowPos = anchorRect.centerX() - xPos;
		}
		int dyTop = anchorRect.top;
		int dyBottom = screenHeight - anchorRect.bottom;
		boolean onTop = (dyTop > dyBottom) ? true : false;
		if (onTop) {
			if (rootHeight > dyTop) {
				yPos = 15;
				LayoutParams l = mScroller.getLayoutParams();
				l.height = dyTop - anchor.getHeight();
			} else {
				yPos = anchorRect.top - rootHeight - 13;
			}
		} else {
			yPos = anchorRect.bottom;
			if (rootHeight > dyBottom) {
				LayoutParams l = mScroller.getLayoutParams();
				l.height = dyBottom;
			}
		}
		showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowPos);
		setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
		mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
	}

	/**
	 * Set animation style
	 * 
	 * @param screenWidth
	 *            screen width
	 * @param requestedX
	 *            distance from left edge
	 * @param onTop
	 *            flag to indicate where the popup should be displayed. Set TRUE
	 *            if displayed on top of anchor view and vice versa
	 */
	private void setAnimationStyle(int screenWidth, int requestedX, boolean onTop) {
		int arrowPos = requestedX - mArrowUp.getMeasuredWidth() / 2;
		switch (mAnimStyle) {
		case ANIM_GROW_FROM_LEFT:
			mWindow.setAnimationStyle(
					(onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
			break;
		case ANIM_GROW_FROM_RIGHT:
			mWindow.setAnimationStyle(
					(onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
			break;
		case ANIM_GROW_FROM_CENTER:
			mWindow.setAnimationStyle(
					(onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
			break;
		case ANIM_REFLECT:
			mWindow.setAnimationStyle(
					(onTop) ? R.style.Animations_PopUpMenu_Reflect : R.style.Animations_PopDownMenu_Reflect);
			break;
		case ANIM_AUTO:
			if (arrowPos <= screenWidth / 4) {
				mWindow.setAnimationStyle(
						(onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
			} else if (arrowPos > screenWidth / 4 && arrowPos < 3 * (screenWidth / 4)) {
				mWindow.setAnimationStyle(
						(onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
			} else {
				mWindow.setAnimationStyle(
						(onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
			}
			break;
		}
	}

	/**
	 * Show arrow
	 * 
	 * @param whichArrow
	 *            arrow type resource id
	 * @param requestedX
	 *            distance from left screen
	 */
	private void showArrow(int whichArrow, int requestedX) {
		final View showArrow = (whichArrow == R.id.arrow_up) ? mArrowUp : mArrowDown;
		final View hideArrow = (whichArrow == R.id.arrow_up) ? mArrowDown : mArrowUp;
		final int arrowWidth = mArrowUp.getMeasuredWidth();
		showArrow.setVisibility(View.VISIBLE);
		ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) showArrow.getLayoutParams();
		param.leftMargin = requestedX - arrowWidth / 2;
		hideArrow.setVisibility(View.INVISIBLE);
	}

	/**
	 * Set listener for window dismissed. This listener will only be fired if
	 * the quicakction dialog is dismissed by clicking outside the dialog or
	 * clicking on sticky item.
	 */
	public void setOnDismissListener() {
		setOnDismissListener(this);
	}

	@Override
	public void onDismiss() {
		// isShow = false;
		// mWindow.dismiss();
		// if (!mDidAction) {
		// }
		super.onDismiss();
	}

	@Override
	public void dismiss() {
		isShow = false;
		super.dismiss();
	}

	public boolean IsShown() {
		return isShow;
	}

	/**
	 * Listener for item click
	 * 
	 */
	public interface OnActionItemClickListener {

		public abstract void onItemClick(QuickAction source, int pos, int actionId);
	}

	/*
	 * @Override public void onClick(View v) { // TODO Auto-generated method
	 * stub switch (v.getId()) { case R.id.btn_num_0: setInput("0", false);
	 * break; case R.id.btn_num_1: setInput("1", false); break; case
	 * R.id.btn_num_2: setInput("2", false); break; case R.id.btn_num_3:
	 * setInput("3", false); break; case R.id.btn_num_4: setInput("4", false);
	 * break; case R.id.btn_num_5: setInput("5", false); break; case
	 * R.id.btn_num_6: setInput("6", false); break; case R.id.btn_num_7:
	 * setInput("7", false); break; case R.id.btn_num_8: setInput("8", false);
	 * break; case R.id.btn_num_9: setInput("9", false); break; case
	 * R.id.btn_exit: this.dismiss(); break; case R.id.btn_delete: setInput("0",
	 * true); break; default: break; } }
	 * 
	 * public void setInput(String num, boolean delFlag) { if (!delFlag) { num =
	 * mBtnInput.getText().toString() + num; mBtnInput.setText(num); } else {
	 * String text = mBtnInput.getText().toString(); if (text.length() > 0) {
	 * StringBuilder sb = new StringBuilder(text); sb.deleteCharAt(sb.length() -
	 * 1); text = sb.toString(); mBtnInput.setText(text); } } }
	 * 
	 * private Button mBtnInput;
	 * 
	 * private void initView(View contentView) {
	 * 
	 * mBtnInput = (Button) contentView.findViewById(R.id.btn_input);
	 * mBtnInput.setOnClickListener(this);
	 * contentView.findViewById(R.id.btn_input).setOnClickListener(this);
	 * contentView.findViewById(R.id.btn_num_0).setOnClickListener(this);
	 * contentView.findViewById(R.id.btn_num_1).setOnClickListener(this);
	 * contentView.findViewById(R.id.btn_num_2).setOnClickListener(this);
	 * contentView.findViewById(R.id.btn_num_3).setOnClickListener(this);
	 * contentView.findViewById(R.id.btn_num_4).setOnClickListener(this);
	 * contentView.findViewById(R.id.btn_num_5).setOnClickListener(this);
	 * contentView.findViewById(R.id.btn_num_6).setOnClickListener(this);
	 * contentView.findViewById(R.id.btn_num_7).setOnClickListener(this);
	 * contentView.findViewById(R.id.btn_num_8).setOnClickListener(this);
	 * contentView.findViewById(R.id.btn_num_9).setOnClickListener(this);
	 * contentView.findViewById(R.id.btn_exit).setOnClickListener(this);
	 * contentView.findViewById(R.id.btn_delete).setOnClickListener(this);
	 * 
	 * }
	 */
	protected void addLayout(int layoutId) {
		View childLayout = mInflater.inflate(layoutId, null);
		mTrack.addView(childLayout);
	}
}
