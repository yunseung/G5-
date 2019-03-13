package com.ktrental.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.beans.PM033;
import com.ktrental.dialog.Tire_Picture_Dialog;
import com.ktrental.util.kog;

import java.util.ArrayList;

public class Tire_Picture_Dialog_Adapter_PM033 extends BaseCommonAdapter<PM033> {

	private int layout;
	
//	private int checkPosition = -1;
	
	private ArrayList<PM033> list;
	
	//Jonathan 150408
//	private ArrayList<PM033> list_PM033;
//	private ArrayList<PM056> list_PM056;
	
	private Tire_Picture_Dialog tpd;

//	private int []front_int = { 0, 1, 2, 3, 4, 9 };
//	private int []rear_int = { 0, 1, 2, 5, 6, 7, 8, 9 };
//zcodev
//zcodevt
	
//	private ArrayList<Integer> front;
//	private ArrayList<Integer> rear;

	private int MODE;
	
	public int curPos;
	public int BackPos;
	
	

	public Tire_Picture_Dialog_Adapter_PM033(Context context, int layout, ArrayList<PM033> list, Tire_Picture_Dialog tpd, int mode)
	{
		super(context);
		mContext = context;
		this.layout = layout;
		mItemArray = list;
		this.list = list;
		this.tpd = tpd;
		this.MODE = mode;
		/*
		front = new ArrayList<Integer>();
		for(int i = 0;i<front_int.length;i++)
		{
			front.add(front_int[i]);
		}
		rear = new ArrayList<Integer>();
		for(int i = 0;i<rear_int.length;i++)
		{
			rear.add(rear_int[i]);
		}
		*/
	}
	
	
//	public Tire_Picture_Dialog_Adapter(Context context, int layout, ArrayList<PM033> list, Tire_Picture_Dialog tpd, int mode, String a)
//	{
//		super(context);
//		mContext = context;
//		this.layout = layout;
//		mItemArray = list;
//		this.list_PM033 = list;
//		this.tpd = tpd;
//		this.MODE = mode;
//		/*
//		front = new ArrayList<Integer>();
//		for(int i = 0;i<front_int.length;i++)
//		{
//			front.add(front_int[i]);
//		}
//		rear = new ArrayList<Integer>();
//		for(int i = 0;i<rear_int.length;i++)
//		{
//			rear.add(rear_int[i]);
//		}
//		*/
//	}

	public View getView(final int position, View convertView, ViewGroup parent)
	{
		View v = convertView;

		if (v == null)
		{
			LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(layout, null);
		}

//		final int final_position = position;
		final PM033 item = list.get(position);

		LinearLayout ll = (LinearLayout)v.findViewById(R.id.row_back_id);
		final CheckBox _checkbox = (CheckBox) v.findViewById(R.id.tire_picture_row_id1);
		
		TextView row2 = (TextView) v.findViewById(R.id.tire_picture_row_id2);
		
		final RadioGroup row3 = (RadioGroup) v.findViewById(R.id.tire_picture_row_id3);
		final RadioButton rb1 = (RadioButton)v.findViewById(R.id.tire_picture_row_rb1);
		final RadioButton rb2 = (RadioButton)v.findViewById(R.id.tire_picture_row_rb2);
		ImageView row4 = (ImageView) v.findViewById(R.id.tire_picture_row_id4);
		
		/*
		if(item.CHECKED) 
		{
			rb1.setEnabled(true);
			rb2.setEnabled(true);
		} 
		else{
			rb1.setEnabled(false);
			rb2.setEnabled(false);
		}
		*/
		
		/*
		if(item.CHECKED) row1.setChecked(true);
		else row1.setChecked(false);
		*/
		
		/*
		if(item.ONESIDE_WEAR) rb1.setChecked(true);
		else rb2.setChecked(true);
		*/
		
		ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				kog.e("KDH", "curpos = "+curPos);
				BackPos = position;
				
//				item.CHECKED = !item.CHECKED;
//				_checkbox.setChecked(item.CHECKED);
//				
//				if(!item.CHECKED)
//				{
//					tpd.deletePic(item.PATH);
//					item.PATH = "";
//				}
//				
				if(TextUtils.isEmpty(item.PATH))
				{
					tpd.showShot(0,BackPos);
				}
				else
				{
					tpd.showShot(1,BackPos);
				}
				notifyDataSetChanged();
				
			}
		});

		_checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				curPos = position;
//				switch(MODE)
//				{
//				case 1: if(!front.contains(final_position))
//				{
//					row1.setChecked(false);
//				}  
//				break;
//				case 2: if(!rear.contains(final_position))
//				{
//					row1.setChecked(false); 
//				}
//				break;
//				}

				/*
				if(isChecked)
				{
					rb1.setEnabled(true); //rb1.setButtonDrawable(R.drawable.radio_selector);
					rb2.setEnabled(true); //rb1.setButtonDrawable(R.drawable.radio_d_selector);
					checkPosition = final_position;
					
				} 
				else
				{
					rb2.setChecked(true);
					rb1.setEnabled(false); //rb1.setButtonDrawable(R.drawable.radio_d);
					rb2.setEnabled(false); //rb1.setButtonDrawable(R.drawable.radio_d);
					item.CHECKED = isChecked;
					tpd.deletePic(item.PATH);
					item.PATH = "";
				}
				*/
				item.CHECKED = isChecked;
				rb1.setEnabled(isChecked); //rb1.setButtonDrawable(R.drawable.radio_selector);
				rb2.setEnabled(isChecked); //rb1.setButtonDrawable(R.drawable.radio_d_selector);
//				checkPosition = final_position;
				
				if(!isChecked)
				{
					tpd.deletePic(item.PATH);
					item.PATH = "";
				}
				
				if(item.PATH.equals("")) 
				{ 
					tpd.showShot(0,curPos); 
				}
				else
				{ 
					tpd.showShot(1,curPos); 
				}
			}
		});
		
		
		
		/*
		row2.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{ 
				checkPosition = final_position;
				if(item.PATH.equals("")) 
				{
					tpd.showShot(0,final_position); 
				}
				else                     
				{
					tpd.showShot(1,final_position); 
				}
				notifyDataSetChanged();
			}
		});
		*/

		row3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				switch(checkedId)
				{
				case R.id.tire_picture_row_rb1:
					item.ONESIDE_WEAR = true;
					break;
				case R.id.tire_picture_row_rb2:
					item.ONESIDE_WEAR = false;
					break;
				}
			}
		});

		row2.setText(item.ZCODEVT);

		rb1.setEnabled(item.CHECKED);
		rb2.setEnabled(item.CHECKED);
		_checkbox.setChecked(item.CHECKED);
		rb1.setChecked(item.ONESIDE_WEAR);
		
		kog.e("KDH", "CHECKED = "+item.CHECKED);
		
		//myung 20131202 UPDATE 계기판의 편마모 체크 라디오 박스 제거 필요.
		if(item.ZCODEVT.equals("차량번호판")||item.ZCODEVT.equals("기타")||item.ZCODEVT.equals("계기판"))
			//    	 if(item.ZCODEVT.equals("차량번호판")||item.ZCODEVT.equals("기타")||item.ZCODEVT.equals("계기판(주행거리)"))
		{
			row3.setVisibility(View.INVISIBLE);
		}
		else
		{
			row3.setVisibility(View.VISIBLE);
		}

		if(!item.PATH.equals("")) 
		{ 
			row4.setVisibility(View.VISIBLE); 
		}
		else
		{
			row4.setVisibility(View.INVISIBLE); 
		}

		if (BackPos == position) 
		{ 
			ll.setBackgroundResource(R.drawable.table_list_s); 
		}
		else
		{ 
			ll.setBackgroundResource(R.drawable.table_list_n); 
		}
		
		v.setTag(item);
		
		return v;
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup)
	{
		return null;
	}
	/*
	public void setCheckPosition(int position) 
	{
		checkPosition = position;
		notifyDataSetChanged();
	}
	 */

	public int getCheckPosition()
	{
		return curPos;
	}
	public int getSelectPosition()
	{
		return BackPos;
	}
}
