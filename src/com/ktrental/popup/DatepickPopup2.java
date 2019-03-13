package com.ktrental.popup;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;

import com.ktrental.R;
import com.ktrental.custom.Cal_Custom2;
import com.ktrental.fragment.InventoryControlFragment;

public class DatepickPopup2 extends PopupWindows implements OnDismissListener {
	private View mRootView;
	private ImageView mArrowUp;
	private ImageView mArrowDown;
	private LayoutInflater mInflater;
	private ViewGroup mTrack;
//	private ScrollView mScroller;
	private OnActionItemClickListener mItemClickListener;
	private OnDismissListener mDismissListener;
	
//	private List<ActionItem> actionItems = new ArrayList<ActionItem>();
	
	private boolean mDidAction;
	
	private int mChildPos;
    private int mInsertPos;
    private int mAnimStyle;
//    private int mOrientation;
    private int rootWidth=0;
    
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    
    public static final int ANIM_GROW_FROM_LEFT = 1;
	public static final int ANIM_GROW_FROM_RIGHT = 2;
	public static final int ANIM_GROW_FROM_CENTER = 3;
	public static final int ANIM_REFLECT = 4;
	public static final int ANIM_AUTO = 5;
	
	
	//휠
	private Context context;
    private Calendar today;
    
    private InventoryControlFragment icf;
    private Cal_Custom2 calv;
    private int DATE = Integer.MAX_VALUE;
    private int MODE = Integer.MAX_VALUE;
    
    public DatepickPopup2(Context context) 
        {
        super(context);
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setRootViewId(R.layout.datepickpopup2);

        mAnimStyle  = ANIM_AUTO;
        mChildPos   = 0;
        }

    public DatepickPopup2(Context context, String date, int mode) 
        {
        super(context);
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mAnimStyle  = ANIM_AUTO;
        mChildPos   = 0;

        this.DATE = Integer.parseInt(date);
        this.MODE = mode;
        
        setRootViewId(R.layout.datepickpopup2);
        }
    
	public void setRootViewId(int id) {
		mRootView	= (ViewGroup) mInflater.inflate(id, null);

		//휠
        today = Calendar.getInstance(); 
		mArrowDown 	= (ImageView) mRootView.findViewById(R.id.arrow_down);
		mArrowUp 	= (ImageView) mRootView.findViewById(R.id.arrow_up);
		calv = (Cal_Custom2)mRootView.findViewById(R.id.calendar_id);
//		calv.setFregment(icf);
		calv.setMode(MODE);
		calv.setDate(DATE);
//		Log.i("#","####날짜비교1/ "+MODE+"/"+DATE);

		mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		setContentView(mRootView);
	}

	public void setAnimStyle(int mAnimStyle)
	    {
		this.mAnimStyle = mAnimStyle;
	    }
	

	public void setOnActionItemClickListener(OnActionItemClickListener listener)
	    {
		mItemClickListener = listener;
	    }

	public void show(View anchor)
	    {
        calv.setButton(anchor);
		preShow();
		int xPos, yPos, arrowPos;
		mDidAction 			= false;		
		int[] location 		= new int[2];	
		anchor.getLocationOnScreen(location);
		Rect anchorRect 	= new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] + anchor.getHeight());
		//mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int rootHeight 		= mRootView.getMeasuredHeight();
		if (rootWidth == 0) { rootWidth		= mRootView.getMeasuredWidth(); }
		int screenWidth 	= mWindowManager.getDefaultDisplay().getWidth();
		int screenHeight	= mWindowManager.getDefaultDisplay().getHeight();
		//automatically get X coord of popup (top left)
		if ((anchorRect.left + rootWidth) > screenWidth)
		    {
			xPos 		= anchorRect.left - (rootWidth-anchor.getWidth());			
			xPos 		= (xPos < 0) ? 0 : xPos;
			arrowPos 	= anchorRect.centerX()-xPos;
		    }
		else{
			if (anchor.getWidth() > rootWidth)   { xPos = anchorRect.centerX() - (rootWidth/2); }
			else                                 { xPos = anchorRect.left; }
			arrowPos = anchorRect.centerX()-xPos;
		    }
		int dyTop			= anchorRect.top;
		int dyBottom		= screenHeight - anchorRect.bottom;
		boolean onTop		= (dyTop > dyBottom) ? true : false;
		if (onTop) 
		    {
			if (rootHeight > dyTop)
			    {
				yPos 			= 15;
//				LayoutParams l 	= mScroller.getLayoutParams();
//				l.height		= dyTop - anchor.getHeight();
			    }
			else{
				yPos = anchorRect.top - rootHeight;
			    }
		    }
		else{
			yPos = anchorRect.bottom;
			if (rootHeight > dyBottom)
			    { 
//				LayoutParams l 	= mScroller.getLayoutParams();
//				l.height		= dyBottom;
			    }
		    }
		showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowPos);
		setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
		mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
	    }

	private void setAnimationStyle(int screenWidth, int requestedX, boolean onTop)
	    {
		int arrowPos = requestedX - mArrowUp.getMeasuredWidth()/2;
		switch (mAnimStyle) 
		    {
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
			    if (arrowPos <= screenWidth/4)
			        {
			        mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
			        }
			    else if (arrowPos > screenWidth/4 && arrowPos < 3 * (screenWidth/4))
			        {
			        mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
			        }
			    else{
				    mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
			        }	
			    break;
		    }
	    }

	private void showArrow(int whichArrow, int requestedX)
	    {
        final View showArrow = (whichArrow == R.id.arrow_up) ? mArrowUp : mArrowDown;
        final View hideArrow = (whichArrow == R.id.arrow_up) ? mArrowDown : mArrowUp;
        final int arrowWidth = mArrowUp.getMeasuredWidth();
        showArrow.setVisibility(View.VISIBLE);
        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams)showArrow.getLayoutParams();
        param.leftMargin = requestedX - arrowWidth / 2;
        hideArrow.setVisibility(View.INVISIBLE);
	    }

	public void setOnDismissListener(OnDismissListener listener)
	    {
		setOnDismissListener(this);
		mDismissListener = listener;
	    }
	
	@Override
	public void onDismiss()
	    {
		if (!mDidAction && mDismissListener != null)
		    {
		    mDismissListener.onDismiss();
		    }
	    }

	public interface OnActionItemClickListener
	    {
		public abstract void onItemClick(DatepickPopup2 source, int pos, int actionId);
	    }

	public interface OnDismissListener
	    {
		public abstract void onDismiss();
	    }
	
}