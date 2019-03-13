package com.ktrental.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ktrental.util.OnSelectedItem;
import com.ktrental.viewholder.SelectedViewHolder;

public abstract class BaseSelectAdapter<T> extends BaseCommonAdapter<T>
		implements View.OnClickListener {

	protected OnSelectedItem mOnSeletedItem;
	protected int mSelectedPosition = -1;
	protected int mBackPosition = -1;

	protected ListView mListView;

	protected View mSeletedView;

	protected abstract void setSelectedBackground(boolean isSelected,
			View contentView);

	public BaseSelectAdapter(Context context, ListView listView) {
		super(context);
		mListView = listView;
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {
		// TODO Auto-generated method stub
		SelectedViewHolder selectedViewHolder = (SelectedViewHolder) rootView
				.getTag();
		if (selectedViewHolder != null) {
			selectedViewHolder.setPosition(position);

			if (mSelectedPosition == position)
				setSelectedBackground(true, rootView);
			else
				setSelectedBackground(false, rootView);
		}
	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {

		if (mSeletedView != null) {
			mSeletedView.setOnClickListener(this);
		}

		return viewgroup;
	}

	public void initScrollPosition(boolean initFlag) {

		if (mListView != null) {
			int last = mListView.getLastVisiblePosition();

			int first = mListView.getFirstVisiblePosition();
			if (mSelectedPosition >= last || first >= mSelectedPosition) {
				if (initFlag)
					mListView.smoothScrollToPosition(mSelectedPosition);
				else
					mListView.setSelection(mSelectedPosition);
				notifyDataSetChanged();
			}
		}
	}

	public void checkItem(int position) {
		mSelectedPosition = position;

		int last = mListView.getLastVisiblePosition();

		int first = mListView.getFirstVisiblePosition();

		if (position >= last || first <= position) {
			mListView.smoothScrollToPosition(position);
		}

		notifyDataSetChanged();
	}

	@Override 
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (mSeletedView != null) {

			int seletedId = mSeletedView.getId();

			if (seletedId == arg0.getId()) {

				if (arg0.getTag() != null) {
					if (arg0.getTag() instanceof SelectedViewHolder) {

						SelectedViewHolder selectedModel = (SelectedViewHolder) arg0
								.getTag();

						checkItem(selectedModel.getPosition());
						if (mBackPosition != mSelectedPosition) {
							if (mOnSeletedItem != null) {

								mOnSeletedItem
										.OnSeletedItem(getItem(selectedModel
												.getPosition()));
							}
							mBackPosition = mSelectedPosition;
						}
					}
				}
			}

		}
	}

	public OnSelectedItem getOnSeletedItem() {
		return mOnSeletedItem;
	}

	public void setOnSeletedItem(OnSelectedItem aOnSeletedItem) {
		this.mOnSeletedItem = aOnSeletedItem;
	}

	public int getmSelectedPosition() {
		return mSelectedPosition;
	}

	public void setmSelectedPosition(int mSelectedPosition) {
		checkItem(mSelectedPosition);
		if (mBackPosition != mSelectedPosition) {
			if (mOnSeletedItem != null) {

				mOnSeletedItem.OnSeletedItem(getItem(mSelectedPosition));
			}
			mBackPosition = mSelectedPosition;
		}

		notifyDataSetChanged();
	}

}
