package com.ktrental.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ktrental.popup.EventPopup2;
import com.ktrental.util.OnEventOkListener;

/**
 * ListView 의 Adapter 를 만들때 사용하는 class
 * 
 * 기본적으로 사용되는 Context, 아이템 Array, 리소스를 불러올 LayoutInflater를 BaseCommonAdapter
 * 사용자에게 제공함으로써 쉽고 빠르게 어덥터 구현이 용이하다. newView 와 bindView를 나누어 Adapter 구현시 기본이 되는
 * getView에서 사용되는 item view를 재사용을 나누어 개발을 하도록 구현 및 새로만들 뷰와 데이터 세팅에 해당되는 부분을 나누어
 * BaseCommonAdapter 사용자가 편하게 사용할수 있다.
 * 
 * @author Hong
 * 
 * 2014-01-20 KDH 뭐가 용이 한지도모르겠네 화면당 한개의 아답터가 있는 것이 훨씬 더 편하고 용이하다.
 * bind , newView는 baseAdapter가아니라 CursorAdpater로 이미 안드로이드 API에서 제공해주는것인데
 * 왜 이렇게 만들었는지 이해를 못하겠다. 유지보수 하는사람은 개죽음이네.
 * 
 * @param <T>
 */
public abstract class BaseCommonAdapter<T> extends BaseAdapter {
	protected Context mContext;
	protected List<T> mItemArray;
	protected LayoutInflater mInflater;
	protected ArrayList<View> mRecycleViews = new ArrayList<View>();
	
	public BaseCommonAdapter(Context context) {
		mItemArray = new ArrayList<T>();  //이쪽에서 Array Object 가 22개 들어오는데... [0]MaintenenceModel  
		//address, carName carNumber 는 있는데, name = "나인스"  NAME1  = null 어디서 받아오는지 모르겠다. 14.06.15 Jonathan
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setData(List<T> datas) {
		if (datas != null) {
			mItemArray.clear();
			mItemArray.addAll(datas);
			notifyDataSetChanged();
		}
	}

	public List<T> getDatas() {
		return mItemArray;
	}

	@Override
	public int getCount() {
		return mItemArray.size();
	}

	@Override
	public T getItem(int position) {
		T obj;
		if (position >= 0 && position < getCount())
			obj = mItemArray.get(position);
		else
			obj = null;
		return obj;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View rootView, ViewGroup viewgroup) {
		View localView;
		if (rootView == null) {
			localView = newView(mContext, position, viewgroup);
			mRecycleViews.add(rootView);
		} else
			localView = rootView;
		bindView(localView, mContext, position);
		return localView;
	}

	/**
	 * getView ViewHolder 를 사용하여 만든 view 에 data 를 할당한다.
	 * 
	 * @param rootView
	 * @param context
	 * @param position
	 */
	protected abstract void bindView(View rootView, Context context,
			int position);

	/**
	 * getView 에서 새로운 row 를 생성할 때 호출된다.
	 * 
	 * @param context
	 * @param position
	 * @param viewgroup
	 * @return
	 */
	protected abstract View newView(Context context, int position,
			ViewGroup viewgroup);

	protected void releaseResouces() {
		recursiveRecycle(mRecycleViews);
	}

	@SuppressWarnings("deprecation")
	protected static void recursiveRecycle(View root) {
		if (root == null)
			return;

		if (root.getBackground() != null) {

			root.getBackground().setCallback(null);
		}
		root.setBackgroundDrawable(null);

		if (root instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) root;
			int count = group.getChildCount();
			for (int i = 0; i < count; i++) {
				recursiveRecycle(group.getChildAt(i));
			}

			if (!(root instanceof AdapterView)) {
				group.removeAllViews();
			}

		}

		if (root instanceof ImageView) {
			ImageView iv = (ImageView) root;

			// ((ImageView) root).setImageDrawable(null);
			Drawable drawable = iv.getDrawable();
			if (drawable instanceof BitmapDrawable) {

				BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
				Bitmap bitmap = bitmapDrawable.getBitmap();

				if (bitmap != null) {

					// bitmap.recycle();
					bitmap = null;
				}
			} else {
				iv.setImageDrawable(null);
			}
			iv.setImageDrawable(null);
		}

		root = null;

		return;
	}

	protected static void recursiveRecycle(List<View>

	recycleList) {
		for (View ref : recycleList) {
			recursiveRecycle(ref);
		}
	}

	protected void showEventPopup2(OnEventOkListener onEventPopupListener,
			String body) {
		EventPopup2 eventPopup = new EventPopup2(mContext, body,
				onEventPopupListener);

		eventPopup.show();

	}
}
