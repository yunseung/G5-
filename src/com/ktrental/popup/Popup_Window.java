package com.ktrental.popup;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.ktrental.R;
import com.ktrental.common.DEFINE;

public class Popup_Window implements OnDismissListener {

    protected Context       context;
    protected PopupWindow   mWindow;
    protected Drawable      mBackground = null;
    protected WindowManager mWindowManager;
	private View mRootView;
	protected ViewGroup mTrack;
	private ImageView mArrowUp;
	private ImageView mArrowDown;
	private LayoutInflater mInflater;
	protected ScrollView mScroller;
	private OnActionItemClickListener mItemClickListener;
	private OnDismissListener mDismissListener;

	private boolean mDidAction;
    private int mAnimStyle;
    private int rootWidth=0;
    
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    
    public static final int ANIM_GROW_FROM_LEFT = 1;
	public static final int ANIM_GROW_FROM_RIGHT = 2;
	public static final int ANIM_GROW_FROM_CENTER = 3;
	public static final int ANIM_REFLECT = 4;
	public static final int ANIM_AUTO = 5;

    public Popup_Window(Context context) 
        {
        this.context = context;
        mWindow = new PopupWindow(context);
        mWindow.setTouchInterceptor(new OnTouchListener()
            {
            @Override
            public boolean onTouch(View v, MotionEvent event)
                {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE)
                    {
                    mWindow.dismiss();
                    return true;
                    }
                return false;
                }
            });
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setRootViewId(R.layout.popupwindow);
        
        mAnimStyle  = ANIM_AUTO;
        }

    protected void preShow()
        {
        if (mRootView == null) throw new IllegalStateException("setContentView was not called with a view to display.");
        if (mBackground == null) mWindow.setBackgroundDrawable(new BitmapDrawable());
        else mWindow.setBackgroundDrawable(mBackground);
        mWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        mWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mWindow.setTouchable(true);
        mWindow.setFocusable(true);
        mWindow.setOutsideTouchable(true);
        mWindow.setContentView(mRootView);
        }

    public void setBackgroundDrawable(Drawable background)
        {
        mBackground = background;
        }

    public void setContentView(View root)
        {
        mRootView = root;
        mWindow.setContentView(root);
        }

    public void setContentView(int layoutResID)
        {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setContentView(inflator.inflate(layoutResID, null));
        }

    public void setOnDismissListener(PopupWindow.OnDismissListener listener)
        {
        mWindow.setOnDismissListener(listener);
        }

    public void dismiss()
        {
        mWindow.dismiss();
        }

	public void setRootViewId(int id)
	    {
		mRootView	= (ViewGroup) mInflater.inflate(id, null);
		mScroller   = (ScrollView)mRootView.findViewById(R.id.scroller);
		mTrack      = (ViewGroup) mRootView.findViewById(R.id.popup_ll_id);
		mArrowDown 	= (ImageView) mRootView.findViewById(R.id.arrow_down);
		mArrowUp 	= (ImageView) mRootView.findViewById(R.id.arrow_up);
		

		mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		setContentView(mRootView);
	    }

	public void setAnimStyle(int mAnimStyle)
	    {
		this.mAnimStyle = mAnimStyle;
	    }
    public void setHeight(int height)
        {
    	//20131119 2560사이즈적용 
//    	if(DEFINE.DISPLAY.equals("2560"))
    		height *= 2;
        mScroller.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, height));
        }

	public void setOnActionItemClickListener(OnActionItemClickListener listener)
	    {
		mItemClickListener = listener;
	    }

    public void show(View anchor)
        {
        preShow();
        int xPos, yPos, arrowPos;
        mDidAction = false;
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] + anchor.getHeight());
        // mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
        // LayoutParams.WRAP_CONTENT));
        mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        int rootHeight = mRootView.getMeasuredHeight();
        if (rootWidth == 0)
            {
            rootWidth = mRootView.getMeasuredWidth();
            }
        int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        if ((anchorRect.left + rootWidth) > screenWidth)
            {
            xPos = anchorRect.left - (rootWidth - anchor.getWidth());
            xPos = (xPos < 0) ? 0 : xPos;
            arrowPos = anchorRect.centerX() - xPos;
            }
        else
            {
            if (anchor.getWidth() > rootWidth)
                {
                xPos = anchorRect.centerX() - (rootWidth / 2);
                }
            else
                {
                xPos = anchorRect.left;
                }
            arrowPos = anchorRect.centerX() - xPos;
            }
        int dyTop = anchorRect.top;
        int dyBottom = screenHeight - anchorRect.bottom;
        boolean onTop = (dyTop > dyBottom) ? true : false;
        if (onTop)
            {
            if (rootHeight > dyTop)
                {
                yPos = 15;
                LayoutParams l = mScroller.getLayoutParams();
                l.height = dyTop - anchor.getHeight();
                }
            else
                {
                yPos = anchorRect.top - rootHeight;
                }
            }
        else
            {
            yPos = anchorRect.bottom;
            if (rootHeight > dyBottom)
                {
                LayoutParams l = mScroller.getLayoutParams();
                l.height = dyBottom;
                }
            }
        showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowPos);
        setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
        mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
        }

	private void setAnimationStyle(int screenWidth, int requestedX, boolean onTop) {
		int arrowPos = requestedX - mArrowUp.getMeasuredWidth()/2;

		switch (mAnimStyle) {
		case ANIM_GROW_FROM_LEFT:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
			break;
					
		case ANIM_GROW_FROM_RIGHT:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
			break;
					
		case ANIM_GROW_FROM_CENTER:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
		break;
			
		case ANIM_REFLECT:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Reflect : R.style.Animations_PopDownMenu_Reflect);
		break;
		
		case ANIM_AUTO:
			if (arrowPos <= screenWidth/4) {
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
			} else if (arrowPos > screenWidth/4 && arrowPos < 3 * (screenWidth/4)) {
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
			} else {
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
			}	
			break;
		}
	}

	private void showArrow(int whichArrow, int requestedX) {
	
        final View showArrow = (whichArrow == R.id.arrow_up) ? mArrowUp : mArrowDown;
        final View hideArrow = (whichArrow == R.id.arrow_up) ? mArrowDown : mArrowUp;

        final int arrowWidth = mArrowUp.getMeasuredWidth();

        showArrow.setVisibility(View.VISIBLE);

        
        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams)showArrow.getLayoutParams();
       
        param.leftMargin = requestedX - arrowWidth / 2;
        
        hideArrow.setVisibility(View.INVISIBLE);
        
        showArrow.bringToFront();
        showArrow.invalidate();
    }

	public void setOnDismissListener(OnDismissListener listener) {
		setOnDismissListener(this);
		
		mDismissListener = listener;
	}
	
	@Override
	public void onDismiss() {
		if (!mDidAction && mDismissListener != null) {
			mDismissListener.onDismiss();
		}
	}
	public interface OnActionItemClickListener {
		public abstract void onItemClick(Popup_Window source, int pos, int actionId);
	}
	public interface OnDismissListener {
		public abstract void onDismiss();
	}
	
}