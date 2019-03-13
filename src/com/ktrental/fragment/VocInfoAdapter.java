package com.ktrental.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.cm.util.PrintLog;

/**
 * <p class="note">
 * <strong>Class 설명:</strong>
 * </p>
 * <p class="note">
 * <strong>Note:</strong>
 * </p>
 */
public class VocInfoAdapter extends ArrayAdapter<HashMap<String, String>>
{
    /* ******************** Variable Set ********************************* */

    private Context                            context;
    private int                                layout;
    private int                                checkPosition = -1;
    private ArrayList<HashMap<String, String>> list;

    /* ******************** Variable Set End ********************************* */

    /* ******************** Interface Set ********************************* */

    /* ******************** Interface Set End ********************************* */

    /* ******************** Abstract Set ********************************* */

    /* ******************** Abstract Set End ********************************* */

    /* ******************** Override Set ********************************* */

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;

        if (v == null)
        {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(layout, null);
        }

        HashMap<String, String> item = list.get(position);

        LinearLayout ll = (LinearLayout) v.findViewById(R.id.back_id);

        // 순번
        TextView row1 = (TextView) v.findViewById(R.id.row_voc_info1);
        // 접수일
        TextView row2 = (TextView) v.findViewById(R.id.row_voc_info2);
        // 접수번호
        TextView row3 = (TextView) v.findViewById(R.id.row_voc_info3);
        // VOC 구분
        TextView row4 = (TextView) v.findViewById(R.id.row_voc_info4);
        // 접수경로
        TextView row5 = (TextView) v.findViewById(R.id.row_voc_info5);
        // VOC 상태
        TextView row6 = (TextView) v.findViewById(R.id.row_voc_info6);
        // 상품구분(대)
        TextView row7 = (TextView) v.findViewById(R.id.row_voc_info7);
        // 상품구분(중)
        TextView row8 = (TextView) v.findViewById(R.id.row_voc_info8);
        // 상품구분(소)
        TextView row9 = (TextView) v.findViewById(R.id.row_voc_info9);
        // VOC 유형(대)
        TextView row10 = (TextView) v.findViewById(R.id.row_voc_info10);
        // VOC 유형(중)
        TextView row11 = (TextView) v.findViewById(R.id.row_voc_info11);
        // VOC 유형(소)
        TextView row12 = (TextView) v.findViewById(R.id.row_voc_info12);
        // 조치부서(1차)
        TextView row13 = (TextView) v.findViewById(R.id.row_voc_info13);
        // 오토케어 MOT 구분
        TextView row14 = (TextView) v.findViewById(R.id.row_voc_info14);

        row1.setText(checkNullValue(item, "NUMBER"));
        row2.setText(item.get("RCDT"));
        row3.setText(item.get("VBELN"));
        row4.setText(item.get("VCDIV_VT"));
        row5.setText(item.get("RCWEGVT"));
        row6.setText(item.get("HSTUS"));
        row7.setText(item.get("PD_DIVT1"));
        row8.setText(item.get("PD_DIVT2"));
        row9.setText(item.get("PD_DIVT3"));
        row10.setText(item.get("LTYPVT"));
        row11.setText(item.get("MTYPVT"));
        row12.setText(item.get("STYPVT"));
        row13.setText(item.get("ACTORG1T_HQ"));
        row14.setText(item.get("MOTT"));
        
        if (checkPosition == position)
        {
            ll.setBackgroundResource(R.drawable.table_list_s);
        }
        else
        {
            ll.setBackgroundResource(R.drawable.table_list_n);
        }
        
        return v;
    }
    
    @Override
	public HashMap<String, String> getItem(int position) 
	{
    	if(list == null || list.size() == 0)
    	{
    		return null;
    	}
    	
    	return list.get(position);
	}
    
    /* ******************** Override Set End ********************************* */

    /* ******************** Method Set ********************************* */

	public VocInfoAdapter(Context context, int layout, ArrayList<HashMap<String, String>> list)
    {
        super(context, layout, list);
        
        this.context = context;
        this.layout = layout;
        this.list = list;
    }
    
    // 현재 위치 체크
    public void setCheckPosition(int position)
    {
        checkPosition = position;
        notifyDataSetChanged();
    }

    // 현재 위치 가져온다.
    public int getCheckPosition()
    {
        return checkPosition;
    }
    
    /**
     * Null 값인 지 체크해서 공백 또는 값을 반환 한다.
     * @param temp
     * @param key
     * @return
     */
    private String checkNullValue(HashMap<String, String> temp, String key) 
    {
    	if(temp.containsKey(key))
    	{
    		String value = temp.get(key);
    		if(value == null || value.trim().length() == 0)
    		{
    			PrintLog.Print("VOC INFO", key + " is not value");
    			return "";
    		}
    		
    		return value;
    	}
    	
    	return "";
    }
    
    /* ******************** Method Set End ********************************* */

    /* ******************** Server Method Set ********************************* */

    /* ******************** Server Method Set End ********************************* */

    /* ******************** Listener Set ********************************* */

    /* ******************** Listener Set End ********************************* */
}
