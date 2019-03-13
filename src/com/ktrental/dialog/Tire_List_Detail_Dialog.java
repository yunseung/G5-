package com.ktrental.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.model.TableModel;
import com.ktrental.util.OnEventOkListener;

import java.util.HashMap;

public class Tire_List_Detail_Dialog extends DialogC
implements View.OnClickListener,ConnectInterface
{
    private Window w;
    private WindowManager.LayoutParams lp;
    private Context                            context;
    private HashMap<String, String> hm;

    private Button bt_close;
    private Button bt_done;

    private TextView tv_name, tv_carname, tv_carnumber, tv_driver, tv_supplier;
    private TextView tv_date, tv_process, tv_tirename1, tv_tirename2;
    private TextView tv_tire1, tv_tire2, tv_tire3, tv_tire4, tv_tire5, tv_tire6, tv_tire7;
    private TextView tv_process_date, tv_process_time, tv_process_text1, tv_process_text2;

    private ConnectController connectController;
    Context mCtx;

    Handler mHandler;

    private final String BTN_OK_CLOSE = "90";

    public Tire_List_Detail_Dialog(Context context, final HashMap<String, String> hm, Handler _mHandler)
        {
        super(context);
        mCtx = context;
        mHandler = _mHandler;
        connectController = new ConnectController(this, context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tire_list_detail_dialog);
        w = getWindow();
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        lp = (WindowManager.LayoutParams)w.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);
        this.context = context;
        this.hm = hm;

        bt_done = (Button)findViewById(R.id.tire_list_done_id);     bt_done.setOnClickListener(this);
        bt_close = (Button) findViewById(R.id.tire_list_close_id);  bt_close.setOnClickListener(this);

        tv_name = (TextView)findViewById(R.id.tire_detail_customer_name_id);
        tv_carname = (TextView)findViewById(R.id.tire_detail_car_name_id);
        tv_date = (TextView)findViewById(R.id.tire_detail_date_id);
        tv_process = (TextView)findViewById(R.id.tire_detail_process_id);
        tv_process_date = (TextView)findViewById(R.id.tire_detail_process_id_date);
        tv_process_time = (TextView)findViewById(R.id.tire_detail_process_id_time);
        tv_process_text1 = (TextView)findViewById(R.id.tire_detail_process_text1);
        tv_process_text2 = (TextView)findViewById(R.id.tire_detail_process_text2);
        tv_tirename1 = (TextView)findViewById(R.id.tire_detail_tire1_id);
        tv_tirename2 = (TextView)findViewById(R.id.tire_detail_tire2_id);

        tv_tire1 = (TextView)findViewById(R.id.tire_detail_front_left_id);
        tv_tire2 = (TextView)findViewById(R.id.tire_detail_front_right_id);
        tv_tire3 = (TextView)findViewById(R.id.tire_detail_rear1_left_id);
        tv_tire4 = (TextView)findViewById(R.id.tire_detail_rear1_right_id);
        tv_tire5 = (TextView)findViewById(R.id.tire_detail_rear2_left_id);
        tv_tire6 = (TextView)findViewById(R.id.tire_detail_rear2_right_id);
        tv_tire7 = (TextView)findViewById(R.id.tire_detail_spare_id);

        ///////////////////Jonathan/////////////////////////////
        tv_carnumber = (TextView)findViewById(R.id.tire_detail_car_number);
        tv_driver = (TextView)findViewById(R.id.tire_detail_car_driver_name);
        tv_supplier = (TextView)findViewById(R.id.tire_detail_car_supplier);
        ///////////////////Jonathan/////////////////////////////


        Button btn_ok = (Button)findViewById(R.id.tire_list_ok_id);
        if(BTN_OK_CLOSE.equals(hm.get("PRMSTS")))
        {
        	//이미지변경
        	btn_ok.setBackgroundResource(R.drawable.btn01_d);
        	btn_ok.setEnabled(false);
        }
        btn_ok.setEnabled(false);


//        btn_ok.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				showProgress(mCtx.getString(R.string.searching));
//				connectController.getZMO_1130_WR01(hm.get("AUFNR"));
//			}
//		});


        setItem();
        }

    private void setItem()
        {
        tv_name.setText(hm.get("NAME1"));
        tv_carname.setText(hm.get("CARCDNM"));
        tv_date.setText(hm.get("RECDT"));
        tv_process.setText(hm.get("PRMSTSNM"));
        tv_tirename1.setText(hm.get("MAKTX"));
        tv_tirename2.setText(hm.get("STIRGBNM"));

        ///////////////////Jonathan/////////////////////////////
        tv_carnumber.setText(hm.get("INVNR"));
        tv_driver.setText(hm.get("ENAME"));
        tv_supplier.setText(hm.get("LIFNRNM"));
        ///////////////////Jonathan/////////////////////////////

        tv_tire1.setText(hm.get("FROLFT"));
        tv_tire2.setText(hm.get("FRORHT"));
        tv_tire3.setText(hm.get("BAKLFT1"));
        tv_tire4.setText(hm.get("BAKRHT1"));
        tv_tire5.setText(hm.get("BAKLFT2"));
        tv_tire6.setText(hm.get("BAKRHT2"));
        tv_tire7.setText("");//스페어

        //2017-08-04. hjt date,time,txt1,txt2 추가
        tv_process_date.setText(hm.get("DATE"));
        tv_process_time.setText(hm.get("TIME"));
        tv_process_text1.setText(hm.get("TXT1"));
        tv_process_text2.setText(hm.get("TXT2"));
        }

    @Override
    public void onClick(View v)
        {
        switch(v.getId())
            {
            case R.id.tire_list_close_id: //닫기 
                dismiss();
                break;
            case R.id.tire_list_done_id:
                dismiss();
                break;
            }
        }



	@Override
	public void connectResponse(String FuntionName, String resultText,
			String MTYPE, int resulCode, TableModel tableModel) {
		// TODO Auto-generated method stub
		hideProgress();
		if("S".equals(MTYPE))
		{

			showEventPopup2(new OnEventOkListener() {

				@Override
				public void onOk() {
					// TODO Auto-generated method stub
					dismiss();
					mHandler.sendEmptyMessage(0);
				}
			}, resultText);
		}
		else
		{

			connectController.duplicateLogin(mContext);

			showEventPopup2(new OnEventOkListener() {

				@Override
				public void onOk() {
					// TODO Auto-generated method stub
					dismiss();
				}
			}, resultText);
		}


	}

	@Override
	public void reDownloadDB(String newVersion) {
		// TODO Auto-generated method stub

	}



}
