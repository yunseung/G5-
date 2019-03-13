package com.ktrental.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ktrental.R;
import com.ktrental.model.NoticeModel;
import com.ktrental.util.kog;
import com.ktrental.viewholder.SelectedViewHolder;
/**
 * </br> 공지를 화면 정보 리스트를 보여주는 Adapter 이다. </br>
 * 
 * @author hongsungil
 */
public class NoticeAdapter extends BaseSelectAdapter<NoticeModel> {

	private int POSITION = 0;
	Context mContext;
	
	public NoticeAdapter(Context context, ListView listView) {
		super(context, listView);
		// TODO Auto-generated constructor stub
		mSelectedPosition = 0;
		mContext = context;
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {

		NoticeViewHolder holder = (NoticeViewHolder) rootView.getTag();

		kog.e("Jonathan",  " Position 은 ?/ " +  position);
		
		NoticeModel model = getItem(position);

		if (model != null) {

			String title = model.getTitle();
			String day = model.getDay();
			String author = model.getAuthor();

			holder.tvNoticeTitle.setText(title);

			holder.tvDay.setText(day);
			holder.tvAuthor.setText(author);
		}

		super.bindView(rootView, context, position);
	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {

		View rootView = mInflater
				.inflate(R.layout.notice_row, viewgroup, false);

		NoticeViewHolder holder = new NoticeViewHolder(position);

		holder.tvNoticeTitle = (TextView) rootView
				.findViewById(R.id.tv_list_title);
		holder.tvDay = (TextView) rootView.findViewById(R.id.tv_list_day);
		holder.tvAuthor = (TextView) rootView.findViewById(R.id.tv_list_author);

		rootView.setTag(holder);
		
		//Jonathan 14.08.22
//		holder.tvNoticeTitle.setOnClickListener(this);
		

		mSeletedView = rootView;

		super.newView(context, position, (ViewGroup) rootView);

		return rootView;
	}

	@Override
	protected void setSelectedBackground(boolean isSelected, View contentView) {
		// TODO Auto-generated method stub

		if (isSelected) {
			contentView.setBackgroundResource(R.drawable.left_list_bg_s);
		} else {
			contentView.setBackgroundResource(R.drawable.left_list_bg_n);
		}

	}

	public class NoticeViewHolder extends SelectedViewHolder {

		TextView tvNoticeTitle;
		TextView tvDay;
		TextView tvAuthor;

		public NoticeViewHolder(int _position) {
			super(_position);
			// TODO Auto-generated constructor stub
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_list_title:
			Toast.makeText(mContext, "눌렀습니다.", 1).show();
			
			break;

		default:
			break;
		}
		
		
		super.onClick(v);
	}
	
	
	public void setPosition(int position)
	{
		this.POSITION = position;
		notifyDataSetChanged();
	}
	
	
}
