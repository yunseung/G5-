package com.ktrental.popup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.common.DEFINE;
import com.ktrental.ui.PopupWindowButton;

public class BaseTextPopup extends QuickAction implements OnItemClickListener,
		OnClickListener {

	private ArrayList<String> mTextArray = new ArrayList<String>();

	LinkedHashMap<String, String> mTextMap = new LinkedHashMap<String, String>();

	private String mPopupName;

	private final int id = 0x0008;

	private OnSelectedPopupItem mOnSelectedItem;

	private boolean isShowFlag = false;

	private int mPosition = -1; // 현재 리스트 포지션

	private int mLayoutId = -1;

	public OnSelectedPopupItem getOnSelectedItem() {
		return mOnSelectedItem;
	}

	public void setOnSelectedItem(OnSelectedPopupItem aOnSelectedItem) {
		this.mOnSelectedItem = aOnSelectedItem;
	}

	public interface OnSelectedPopupItem {
		void onSelectedItem(int position, String popName);
	}

	public BaseTextPopup(Context context,
			LinkedHashMap<String, String> textMap, String popName, int LayoutId) {
		super(context);

		mTextMap = textMap;
		mPopupName = popName;
		mLayoutId = LayoutId;
		init();
	}

	public BaseTextPopup(Context context, ArrayList<String> textArray,
			String popName, int LayoutId) {
		super(context);

		mTextArray = textArray;
		mPopupName = popName;
		mLayoutId = LayoutId;
		init();
	}

	public BaseTextPopup(Context context, ArrayList<String> textArray,
			String popName) {
		super(context);

		mTextArray = textArray;
		mPopupName = popName;
		init();
	}

	public BaseTextPopup(Context context,
			LinkedHashMap<String, String> textMap, String popName) {
		super(context);

		mTextMap = textMap;

		mPopupName = popName;
		init();
	}

	private void init() {

		// LinearLayout linearLayout = new LinearLayout(mContext);
		// linearLayout.setOrientation(VERTICAL);
		// linearLayout.setLayoutParams(new LayoutParams(
		// LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		//
		// if (mTextArray.isEmpty()) {
		//
		// Iterator<String> it = mTextMap.keySet().iterator();
		//
		// while (it.hasNext()) {
		// String strKey = "";
		// String strValue = "";
		//
		// addTextView(linearLayout, strValue);
		//
		// }
		//
		// } else {
		// for (int i = 0; i < mTextArray.size(); i++) {
		// addTextView(linearLayout, mTextArray.get(i));
		// }
		// }
		if (mLayoutId == -1)
			addLayout(R.layout.text_array_popup_layout);
		else
			addLayout(mLayoutId);

		ListView listView = (ListView) mTrack.findViewById(R.id.lv_text);
		listView.setOnItemClickListener(this);

		int size = 0;

		if (mTextArray.isEmpty()) {
			size = mTextMap.size();
		} else {
			size = mTextArray.size();
		}

		//myung 20131126 Update 2560대응
//		if(DEFINE.DISPLAY.equals("2560"))
			size *= 2;
		listView.setLayoutParams(new LinearLayout.LayoutParams(200, 50 * size));

		listView.setTag(mPopupName);
		listView.setItemsCanFocus(false);
		TextArrayAdapter adapter = new TextArrayAdapter();

		listView.setAdapter(adapter);

		setOnDismissListener(this);

	}

	public void show(View anchor) {
		isShowFlag = true;
		super.show(anchor);
	}

	public class TextArrayAdapter extends BaseAdapter {

		public TextArrayAdapter() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {

			int count = 0;

			if (mTextArray.isEmpty())
				count = mTextMap.size();
			else
				count = mTextArray.size();

			return count;
		}

		@Override
		public String getItem(int arg0) {

			String value = null;

			if (mTextArray.isEmpty()) {

				Iterator<String> it = mTextMap.keySet().iterator();

				int i = 0;

				while (it.hasNext()) {
					String strKey = "";
					// String strValue = "";

					strKey = it.next();
					// strValue = mTextMap.get(strKey);

					if (i == arg0) {
						value = strKey;
						break;
					}

					i++;
				}
			} else
				value = mTextArray.get(arg0);
			return value;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {

			ViewHolder viewHolder = null;

			if (arg1 == null) {
				arg1 = mInflater.inflate(R.layout.text_popup_row, arg2, false);

				viewHolder = new ViewHolder();
				// arg1.setLayoutParams(new AbsListView.LayoutParams(
				// AbsListView.LayoutParams.WRAP_CONTENT, 50));
				viewHolder.tvData = (TextView) arg1.findViewById(R.id.tv_data);
				viewHolder.rlRoot = (RelativeLayout) arg1
						.findViewById(R.id.rl_root);
				arg1.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) arg1.getTag();
			}

			viewHolder.tvData.setOnClickListener(BaseTextPopup.this);
			viewHolder.rlRoot.setOnClickListener(BaseTextPopup.this);

			viewHolder.tvData.setTag(arg0);
			viewHolder.rlRoot.setTag(arg0);

			viewHolder.tvData.setText(getItem(arg0));

			return arg1;

			// TextView textview ;
			// if(arg1 == null){
			// textview= newTextView();
			//
			// }else
			// {
			// textview = (TextView)arg1;
			// }
			//
			// return textview;
		}

		private TextView newTextView() {
			TextView tv = new TextView(mContext);
			tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));

			tv.setTextSize(20);
			tv.setTextColor(mContext.getResources()
					.getColor(R.color.cal_sunday));

			return tv;
		}

		private class ViewHolder {
			TextView tvData;
			RelativeLayout rlRoot;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		String popName = (String) arg0.getTag();

		switch (arg0.getId()) {
		case R.id.lv_text:

			break;

		default:
			break;
		}

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.tv_data:
			selectedItem(view);
			break;
		case R.id.rl_root:
			selectedItem(view);
			break;
		default:
			break;
		}

	}

	private void selectedItem(View view) {
		int pos = (Integer) view.getTag();

		if (mOnSelectedItem != null) {
			mOnSelectedItem.onSelectedItem(pos, mPopupName);
			dismiss();
		}
	}

	@Override
	public void onDismiss() {
		isShowFlag = false;
//		Log.d("", "onDismiss = " + isShowFlag);
		super.onDismiss();
	}

	public boolean isShow() {
		return isShowFlag;
	}

	public void show(View anchor, int position, int popWidth, int popHeight) {
		mPosition = position;
		super.show(anchor, popWidth, popHeight);
	}
	
	PopupWindowButton pwb;
	public void show(View anchor, int position, int popWidth, int popHeight, PopupWindowButton pwb)
	    {
        mPosition = position;
        this.pwb = pwb;
        super.show(anchor, popWidth, popHeight);
	    }

	public void show(View anchor, int position, int popWidth, int popHeight,
			int[] location) {
		mPosition = position;
		super.show(anchor, popWidth, popHeight, location);
	}

}
