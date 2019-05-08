package com.ktrental.product;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Menu2_Adapter extends ArrayAdapter<HashMap<String, String>> {

	private Context context;
	private int layout;
	private int checkPosition = -1;
	private ArrayList<HashMap<String, String>> list;

	public Menu2_Adapter(Context context, int layout, ArrayList<HashMap<String, String>> list) {
		super(context, layout, list);
		this.context = context;
		this.layout = layout;
		this.list = list;

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(layout, parent, false);
		}

		HashMap<String, String> item = list.get(position);

		LinearLayout ll = (LinearLayout) v.findViewById(R.id.back_id);

		// Jonathan 14.06.27 진행상태 추가를 위해
		Set<String> set = item.keySet();
		Iterator<String> it = set.iterator();
		String key;

		while (it.hasNext()) {
			key = it.next();
			kog.e("Jonathan", "정비접수내역  :" + key + "  value = " + item.get(key));
		}

		TextView row0 = (TextView) v.findViewById(R.id.row_id0);
		TextView row1 = (TextView) v.findViewById(R.id.row_id1);
		TextView row2 = (TextView) v.findViewById(R.id.row_id2);
		TextView row3 = (TextView) v.findViewById(R.id.row_id3);
		TextView row4 = (TextView) v.findViewById(R.id.row_id4);
		TextView row5 = (TextView) v.findViewById(R.id.row_id5);
		TextView row6 = (TextView) v.findViewById(R.id.row_id6);
		TextView row7 = (TextView) v.findViewById(R.id.row_id7);
		TextView row8 = (TextView) v.findViewById(R.id.row_id8);
		// myung 20131211 ADD
		TextView row9 = (TextView) v.findViewById(R.id.row_id9);
		// 2014-02-06 KDH 주문접수번호
		TextView row10 = (TextView) v.findViewById(R.id.row_id10);

		TextView row11 = (TextView) v.findViewById(R.id.row_id11);
		// VOC 내역 추가
		// KangHyunJin ADD (20151208)
		TextView row12 = (TextView) v.findViewById(R.id.row_id12);

		TextView row13 = (TextView) v.findViewById(R.id.row_id13);

		// yunseung 2019.05.07 IoT정비 추가.
		TextView rowIot = (TextView) v.findViewById(R.id.row_iot);
		rowIot.setText(item.get("ATVYN").equals("A") ? "IoT정비" : "");

		// 7 요청 // 8 운전자 // 9번 전화번호
		row7.setText("");
		row8.setText("");
		row9.setText("");

		row0.setText("" + (position + 1));

		// Jonathan 14.06.27 진행상태 추가사항 시작
		row1.setText(item.get("CUSNAM")); // 고객명
		row2.setBackgroundColor(0x00000000);
		if("R".equals(item.get("COL")))
		{
			row2.setBackgroundColor(Color.parseColor("#FF4848"));
		}
		else if("P".equals(item.get("COL")))
		{
			row2.setBackgroundColor(Color.parseColor("#FFB4B4"));
		}
		else if("".equals(item.get("COL")))
		{
			row2.setBackgroundColor(0x00000000);
		}


		row2.setText(item.get("INVNR")); // 차량번호
		row3.setText(item.get("SNDCUS")); // 고개안내
		row4.setText(item.get("TXT")); // 정비유형

		row5.setText(item.get("PRMSTSNM")); // 진행상태
		String strRECDT = item.get("RECDT");
		String strRECTM = item.get("RECTM");
		strRECDT = strRECDT.substring(0, 4) + "." + strRECDT.substring(4, 6) + "." + strRECDT.substring(6);
		strRECTM = strRECTM.substring(0, 2) + ":" + strRECTM.substring(2, 4);
		row6.setText(strRECDT + "  " + strRECTM); // 접수일시
		row7.setText(item.get("AUFNR")); // 정비접수번호
		row8.setText(item.get("REQDES")); // 고객요청사항
		row9.setText(item.get("PROC_TXT")); // 매니저 조치사항.
		// row9.setText(item.get("DCEMG")); // 긴급여부 //Jonathan 14.07.03 긴급여부 빼고,
		// 매니저 조치사항 추가

		if (!item.get("DRVNAM").isEmpty()) {
			row10.setText(item.get("DRVNAM")); // 운전자명
		} else {
			row10.setText(item.get("REQNAM")); // 요청자명
		}

		String tel = item.get("DRVHP");
		String req_tel = item.get("REQHP");
		if (!item.get("DRVHP").isEmpty()) {
			if (tel != null && tel.length() > 4) {
				row11.setText(tel.subSequence(0, tel.length() - 3) + context.getString(R.string.callog_star)); // 운전자
																												// 휴대폰번호
			}
		} else {
			if (req_tel != null && req_tel.length() > 4) {
				row11.setText(req_tel.subSequence(0, req_tel.length() - 3) + context.getString(R.string.callog_star)); // 운전자
																														// 휴대폰번호
			} else {
				row11.setText(" "); // Jonathan 14.07.07 일반일 경우, 운전자 연락처가 "운전자
									// 연락처" 라고 뜨는데, null값으로 변경.
			}
		}

		row12.setText(item.get("VOCNUM"));
		System.out.println("VOC Menu2_Adapter" + item);

		if (item.get("VIPMA").equals("V")) {
			row13.setText("롯데그룹VIP");
			row13.setBackgroundColor(Color.parseColor("#FFA7A7"));
		} else if (item.get("VIPMA").equals("Y")) {
			row13.setBackgroundColor(0x00000000);
			row13.setText("VIP");
		} else {
			row13.setBackgroundColor(0x00000000);
			row13.setText("");
		}

		// Jonathan 14.06.27 진행상태 추가사항 끝

		// row1.setText(item.get("CUSNAM")); // 고객명
		// row2.setText(item.get("INVNR")); // 차량번호
		// // row3.setText(item.get("PRMSTSNM")); // 진행상태
		// 2014-02-21 KDH 고객안내로 변경
		// row3.setText(item.get("SNDCUS")); // 고객안내
		// row4.setText(item.get("TXT")); // 정비유형
		// row5.setText(item.get("DCEMG")); // 긴급여부
		// String strRECDT = item.get("RECDT");
		// String strRECTM = item.get("RECTM");
		// strRECDT = strRECDT.substring(0, 4)+"."+strRECDT.substring(4,
		// 6)+"."+strRECDT.substring(6);
		// strRECTM = strRECTM.substring(0, 2)+":"+strRECTM.substring(2, 4);
		// row6.setText(strRECDT+" "+strRECTM); // 접수일시
		// row7.setText(item.get("REQDES")); // 고객요청사항
		// row8.setText(item.get("DRVNAM")); // 운전지먕
		// String tel = item.get("DRVHP");
		// kog.e("KDH", "position = "+position+" tel = "+tel);
		// if(tel != null && tel.length() > 4)
		// {
		// row9.setText(tel.subSequence(0,
		// tel.length()-3)+context.getString(R.string.callog_star)); // 운전자
		// 휴대폰번호
		// }
		// row10.setText(item.get("AUFNR")); //주문접수번호

		if (checkPosition == position) {
			ll.setBackgroundResource(R.drawable.table_list_s);
		} else {
			ll.setBackgroundResource(R.drawable.table_list_n);
		}

		if ("긴급".equals(item.get("TXT"))) {

			kog.e("Jonathan", "빨간색");
			ll.setBackgroundResource(R.drawable.table_list_r);
		}
		
		notifyDataSetChanged();

		return v;
	}

	public String getDateFormat(String date) {
		StringBuffer sb = new StringBuffer(date);
		if (date.length() == 8) {
			sb.insert(4, ".");
			int last = sb.length() - 2;
			sb.insert(last, ".");
		}
		return sb.toString();
	}

	public void setCheckPosition(int position) {
		checkPosition = position;
		notifyDataSetChanged();
	}

	public int getCheckPosition() {
		return checkPosition;
	}

}
