package com.ktrental.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ktrental.R;
import com.ktrental.adapter.PartsTransfer_Cars_Dialog_Adapter;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.connect.Connector;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.common.DEFINE;
import com.ktrental.model.TableModel;
import com.ktrental.util.kog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class MaintenanceCheckListDialog2 extends DialogC implements ConnectInterface, View.OnClickListener {
	private Window w;
	private WindowManager.LayoutParams lp;
	private Context context;
//	private Button search;
//	private ListView listview;


	String TABLE_NAME = "O_ITAB1";
	String ZCODE_KEY = "CM010";
	String ZCODE_VAL = "M006";
	String ZCODEH = "ZCODEH";
	String ZCODEV = "ZCODEV";
	String ZCODEVT = "ZCODEVT";


	private PartsTransfer_Cars_Dialog_Adapter pcda;
	private ProgressDialog pd;
	private HashMap<String, String> mSelectedMap = null;

	Connector connector;
	private ConnectController                  connectController;


	private ArrayList<HashMap<String, String>> array_hash;

//	private Popup_Window_PM013 ppm013popup;

	private TextView title_id;
	private Button bt_done;

	private ScrollView sv_maintenance_checklist;
	private RelativeLayout rl_maintenance_checklist;
	private LinearLayout ll_maintenance_checklist;
	private Button bt_screencapture_checklist;


	private Button bt_maintenance_checklist_close;



	private TextView tv_invnr;	//차량번호
	private TextView tv_maktx;	// 차종
	private TextView tv_dlsm1;	// 고객
	private TextView tv_incml;	// 주행거리
	private TextView tv_gstri;	// 점검일자
	private TextView tv_sojae;	// 차량소재지

	private TextView tv_good01;	// 엔진오일 양호
	private TextView tv_excg01;	// 엔진오일 교환
	private TextView tv_outqty01;	// 엔진오일 수량

	private TextView tv_good02;	// 에어크리너 양호
	private TextView tv_excg02;	// 에어크리너 교환
	private TextView tv_outqty02;	// 에어크리너 수량

	private TextView tv_good03;	// 오일필터 양호
	private TextView tv_excg03;	// 오일필터 교환
	private TextView tv_outqty03;	// 오일필터 수량

	private TextView tv_good04;	// 밋션오일 양호
	private TextView tv_excg04;	// 밋션오일 교환
	private TextView tv_outqty04;	// 밋션오일 수량

	private TextView tv_good05;	// 파워오일 양호
	private TextView tv_excg05;	// 파워오일 교환
	private TextView tv_outqty05;	// 파워오일 수량

	private TextView tv_good06;	// 브레이크오일 양호
	private TextView tv_excg06;	// 브레이크오일 교환
	private TextView tv_outqty06;	// 브레이크오일 수량

	private TextView tv_good07;	// 전조등/안개등 양호
	private TextView tv_excg07;	// 전조등/안개등 교환
	private TextView tv_outqty07;	// 전조등/안개등 수량

	private TextView tv_good08;	// 방향지시등/미등 양호
	private TextView tv_excg08;	// 방향지시등/미등 교환
	private TextView tv_outqty08;	// 방향지시등/미등 수량

	private TextView tv_good09;	// 후진등/번호판 양호
	private TextView tv_excg09;	// 후진등/번호판 교환
	private TextView tv_outqty09;	// 후진등/번호판 수량

	private TextView tv_good10;	// 브레이크등 양호
	private TextView tv_excg10;	// 브레이크등 교환
	private TextView tv_outqty10;	// 브레이크등 수량

	private TextView tv_good11;	// 실내등 양호
	private TextView tv_excg11;	// 실내등 교환
	private TextView tv_outqty11;	// 실내등 수량

	private TextView tv_good12;	// 라이닝점검 양호
	private TextView tv_excg12;	// 라이닝점검 교환
	private TextView tv_outqty12;	// 라이닝점검 수량

	private TextView tv_good13;	// 배터리 점검 양호
	private TextView tv_excg13;	// 배터리 점검 교환
	private TextView tv_outqty13;	// 배터리 점검 수량

	private TextView tv_good14;	// 실내크리너(향균) 양호
	private TextView tv_excg14;	// 실내크리너(향균) 교환
	private TextView tv_outqty14;	// 실내크리너(향균) 수량

	private TextView tv_good15;	// 냉각수(부동액) 양호
	private TextView tv_excg15;	// 냉각수(부동액) 교환
	private TextView tv_outqty15;	// 냉각수(부동액) 수량

	private TextView tv_good16;	// 워셔액 점검 양호
	private TextView tv_excg16;	// 워셔액 점검 교환
	private TextView tv_outqty16;	// 워셔액 점검 수량

	private TextView tv_good17;	// 와이퍼(브러쉬)점검 양호
	private TextView tv_excg17;	// 와이퍼(브러쉬)점검 교환
	private TextView tv_outqty17;	// 와이퍼(브러쉬)점검 수량

	private TextView tv_good18;	// 에어컨/히터 양호
	private TextView tv_excg18;	// 에어컨/히터 교환
	private TextView tv_outqty18;	// 에어컨/히터 수량

	private TextView tv_good19;	// 타이어마모/공기압 점검 양호
	private TextView tv_excg19;	// 타이어마모/공기압 점검 교환
	private TextView tv_outqty19;	// 타이어마모/공기압 점검 수량

	private EditText et_ccmbi;	//점검세부내용
	private TextView tv_usrid;	//점검자 연락처
	private TextView tv_ename;	//점검자 성명
	private ImageView iv_sign;	//고객확인


	private ImageView iv_confirm_check;



	private String TEL_NUMBER = "";



	boolean isCheck;

	public MaintenanceCheckListDialog2(Context context, String aufnr) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.maintenance_checklist_dialog2);
		w = getWindow();
		w.setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		lp = (WindowManager.LayoutParams) w.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.context = context;
		pd = new ProgressDialog(context);
		pd.setCancelable(false);
		pd.setMessage("검색중입니다.");
		connectController = new ConnectController(this, context);
		
		
		title_id = (TextView)findViewById(R.id.title_id);
		sv_maintenance_checklist = (ScrollView)findViewById(R.id.sv_maintenance_checklist);
		rl_maintenance_checklist = (RelativeLayout)findViewById(R.id.rl_maintenance_checklist);
		ll_maintenance_checklist = (LinearLayout)findViewById(R.id.ll_maintenance_checklist);

		bt_screencapture_checklist = (Button)findViewById(R.id.bt_screencapture_checklist);
		bt_screencapture_checklist.setOnClickListener(this);
		
		
		bt_maintenance_checklist_close = (Button)findViewById(R.id.bt_maintenance_checklist_close);
		bt_maintenance_checklist_close.setOnClickListener(this);
		
		
		
		tv_invnr = (TextView)findViewById(R.id.tv_invnr);
		tv_maktx = (TextView)findViewById(R.id.tv_maktx);
		tv_dlsm1 = (TextView)findViewById(R.id.tv_dlsm1);
		tv_incml = (TextView)findViewById(R.id.tv_incml);
		tv_gstri = (TextView)findViewById(R.id.tv_gstri);
		tv_sojae = (TextView)findViewById(R.id.tv_sojae);
		
		tv_good01 = (TextView)findViewById(R.id.tv_good01);
		tv_excg01 = (TextView)findViewById(R.id.tv_excg01);
		tv_outqty01 = (TextView)findViewById(R.id.tv_outqty01);
		
		tv_good02 = (TextView)findViewById(R.id.tv_good02);
		tv_excg02 = (TextView)findViewById(R.id.tv_excg02);
		tv_outqty02 = (TextView)findViewById(R.id.tv_outqty02);
		
		tv_good03 = (TextView)findViewById(R.id.tv_good03);
		tv_excg03 = (TextView)findViewById(R.id.tv_excg03);
		tv_outqty03 = (TextView)findViewById(R.id.tv_outqty03);
		
		tv_good04 = (TextView)findViewById(R.id.tv_good04);
		tv_excg04 = (TextView)findViewById(R.id.tv_excg04);
		tv_outqty04 = (TextView)findViewById(R.id.tv_outqty04);

		tv_good05 = (TextView)findViewById(R.id.tv_good05);
		tv_excg05 = (TextView)findViewById(R.id.tv_excg05);
		tv_outqty05 = (TextView)findViewById(R.id.tv_outqty05);
		
		tv_good06 = (TextView)findViewById(R.id.tv_good06);
		tv_excg06 = (TextView)findViewById(R.id.tv_excg06);
		tv_outqty06 = (TextView)findViewById(R.id.tv_outqty06);
		
		tv_good07 = (TextView)findViewById(R.id.tv_good07);
		tv_excg07 = (TextView)findViewById(R.id.tv_excg07);
		tv_outqty07 = (TextView)findViewById(R.id.tv_outqty07);
		
		tv_good08 = (TextView)findViewById(R.id.tv_good08);
		tv_excg08 = (TextView)findViewById(R.id.tv_excg08);
		tv_outqty08 = (TextView)findViewById(R.id.tv_outqty08);
		
		tv_good09 = (TextView)findViewById(R.id.tv_good09);
		tv_excg09 = (TextView)findViewById(R.id.tv_excg09);
		tv_outqty09 = (TextView)findViewById(R.id.tv_outqty09);
		
		tv_good10 = (TextView)findViewById(R.id.tv_good10);
		tv_excg10 = (TextView)findViewById(R.id.tv_excg10);
		tv_outqty10 = (TextView)findViewById(R.id.tv_outqty10);
		
		tv_good11 = (TextView)findViewById(R.id.tv_good11);
		tv_excg11 = (TextView)findViewById(R.id.tv_excg11);
		tv_outqty11 = (TextView)findViewById(R.id.tv_outqty11);
		
		tv_good12 = (TextView)findViewById(R.id.tv_good12);
		tv_excg12 = (TextView)findViewById(R.id.tv_excg12);
		tv_outqty12 = (TextView)findViewById(R.id.tv_outqty12);
		
		tv_good13 = (TextView)findViewById(R.id.tv_good13);
		tv_excg13 = (TextView)findViewById(R.id.tv_excg13);
		tv_outqty13 = (TextView)findViewById(R.id.tv_outqty13);
		
		tv_good14 = (TextView)findViewById(R.id.tv_good14);
		tv_excg14 = (TextView)findViewById(R.id.tv_excg14);
		tv_outqty14 = (TextView)findViewById(R.id.tv_outqty14);
		
		tv_good15 = (TextView)findViewById(R.id.tv_good15);
		tv_excg15 = (TextView)findViewById(R.id.tv_excg15);
		tv_outqty15 = (TextView)findViewById(R.id.tv_outqty15);
		
		tv_good16 = (TextView)findViewById(R.id.tv_good16);
		tv_excg16 = (TextView)findViewById(R.id.tv_excg16);
		tv_outqty16 = (TextView)findViewById(R.id.tv_outqty16);
		
		tv_good17 = (TextView)findViewById(R.id.tv_good17);
		tv_excg17 = (TextView)findViewById(R.id.tv_excg17);
		tv_outqty17 = (TextView)findViewById(R.id.tv_outqty17);
		
		tv_good18 = (TextView)findViewById(R.id.tv_good18);
		tv_excg18 = (TextView)findViewById(R.id.tv_excg18);
		tv_outqty18 = (TextView)findViewById(R.id.tv_outqty18);
		
		tv_good19 = (TextView)findViewById(R.id.tv_good19);
		tv_excg19 = (TextView)findViewById(R.id.tv_excg19);
		tv_outqty19 = (TextView)findViewById(R.id.tv_outqty19);
		
		et_ccmbi = (EditText)findViewById(R.id.et_ccmbi);
		tv_usrid = (TextView)findViewById(R.id.tv_usrid);
		tv_ename = (TextView)findViewById(R.id.tv_ename);
		iv_sign = (ImageView)findViewById(R.id.iv_sign);

		iv_confirm_check = (ImageView)findViewById(R.id.iv_confirm_check);
		
		
		
		
		
//		connectController.getZMO_1070_RD04("44000654047");
//		connectController.getZMO_1070_RD04("44000569120");
		connectController.getZMO_1070_RD04(aufnr);
		
		// Jonathan 14.08.04 추가.
//		if (context instanceof Activity) {
//			setOwnerActivity((Activity) context);
//		}


	}
	
	
	
	
	
	public static Bitmap loadBitmapFromView(View v, int width, int height) {
	    Bitmap b = Bitmap.createBitmap(width , height, Bitmap.Config.ARGB_8888);                
	    Canvas c = new Canvas(b);
	    v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
	    v.draw(c);
	    return b;
	}
	
	
	
	
	

	public Bitmap takeScreenshot() {

	    View rootView = getWindow().getDecorView().findViewById(R.id.ll_maintenance_checklist);
	    kog.e("Jonathan", "Jonathan rootView " + rootView.getId());
	    rootView.setDrawingCacheEnabled(true);
	    return rootView.getDrawingCache();

	}

	public void saveBitmap(Bitmap bitmap) {

	    String root = Environment.getExternalStorageDirectory().toString();
//	    File newDir = new File(root + "/0LotteRentalChecklist");
	    File newDir = new File( Environment.getExternalStorageDirectory()
				+ File.separator + DEFINE.APP_NAME + File.separator);
	    
//	    String path 
		String fotoname = +System.currentTimeMillis() + ".jpg";
	    
	    
	    newDir.mkdirs();
	    Random gen = new Random();
	    int n = 10000;
	    n = gen.nextInt(n);
//	    String fotoname = "Photo-" + n + ".jpg";
	    File file = new File(newDir, fotoname);
	    if (file.exists())
	        file.delete();
	    try {
	        FileOutputStream fos = new FileOutputStream(file);
	        bitmap.compress(CompressFormat.JPEG, 100, fos);
	        fos.flush();
	        fos.close();
	        Toast.makeText(context,
	                "캡쳐되었습니다.", Toast.LENGTH_SHORT).show();
	        
	        
	        Intent intent = new Intent(Intent.ACTION_SEND);
	        intent.putExtra("sms_body", "롯데렌탈 순회정비차량 점검표");
	        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(newDir, fotoname)));
	        intent.putExtra("address", TEL_NUMBER);
	        intent.setType("image/png"); 
	        context.startActivity(Intent.createChooser(intent,"Send"));



	        
	        

	    } catch (FileNotFoundException e) {
	        Log.e("GREC", e.getMessage(), e);
	    } catch (IOException e) {
	        Log.e("GREC", e.getMessage(), e);
	    }

	}
	
	
	
	
	

	public void setTitle(String str) {
		title_id.setText(str);
	}

	public void setDone(String str) {
//		bt_done.setText(str);
	}

	@Override
	public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode,
			TableModel tableModel) {
		
		HashMap<String, String> ES_CIRSHEET = new HashMap<String, String>();
		HashMap<String, String> ES_TEL = new HashMap<String, String>();
		
		
		hideProgress();


//		if(!MTYPE.equals("S"))
//        {
//	        EventPopupC epc = new EventPopupC(context);
//	        epc.show(resultText); return;
//        } 
    

		if (FuntionName.equals("ZMO_1070_RD04")) 
		{
			
			TEL_NUMBER = "";
			
			 kog.e("Jonathan", "  순회정비차량 점검표 여기들어와?");
			 DecimalFormat format = new DecimalFormat("##");
			 
			
			ES_CIRSHEET = tableModel.getStruct("ES_CIRSHEET");
			ES_TEL = tableModel.getStruct("ES_TEL");
			
			
			TEL_NUMBER = ES_TEL.get("TEL_NUMBER").toString();
			
			
			Set <String> set  = ES_TEL.keySet();
			Iterator <String> it = set.iterator();
			String key;
			
			while(it.hasNext()) 
			{
				key = it.next();
				kog.e("Jonathan", "ES_TEL key ===  " + key + "    value  === " + ES_TEL.get(key));
			}
			
			
			
			tv_invnr.setText(ES_CIRSHEET.get("INVNR").toString());
			tv_maktx.setText(ES_CIRSHEET.get("MAKTX").toString());
			tv_dlsm1.setText(ES_CIRSHEET.get("DLSM1").toString());
			tv_incml.setText(ES_CIRSHEET.get("INCML").toString());
			tv_gstri.setText(ES_CIRSHEET.get("GSTRI").toString());
			tv_sojae.setText(ES_CIRSHEET.get("SOJAE").toString());
			
			tv_good01.setText(ES_CIRSHEET.get("GOOD01").toString());
			tv_excg01.setText(ES_CIRSHEET.get("EXCG01").toString());
			try {
				tv_outqty01.setText(format.format(Float.valueOf(ES_CIRSHEET.get("OUTQTY01").toString())));
			} catch (NumberFormatException e){
				tv_outqty01.setText("0");
			}

			tv_good02.setText(ES_CIRSHEET.get("GOOD02").toString());
			tv_excg02.setText(ES_CIRSHEET.get("EXCG02").toString());
			try {
				tv_outqty02.setText(format.format(Float.valueOf(ES_CIRSHEET.get("OUTQTY02").toString())));
			} catch (NumberFormatException e){
				tv_outqty02.setText("0");
			}

			tv_good03.setText(ES_CIRSHEET.get("GOOD03").toString());
			tv_excg03.setText(ES_CIRSHEET.get("EXCG03").toString());
			try {
				tv_outqty03.setText(format.format(Float.valueOf(ES_CIRSHEET.get("OUTQTY03").toString())));
			} catch (NumberFormatException e){
				tv_outqty03.setText("0");
			}

			tv_good04.setText(ES_CIRSHEET.get("GOOD04").toString());
			tv_excg04.setText(ES_CIRSHEET.get("EXCG04").toString());
			try {
				tv_outqty04.setText(format.format(Float.valueOf(ES_CIRSHEET.get("OUTQTY04").toString())));
			} catch (NumberFormatException e){
				tv_outqty04.setText("0");
			}

			tv_good05.setText(ES_CIRSHEET.get("GOOD05").toString());
			tv_excg05.setText(ES_CIRSHEET.get("EXCG05").toString());
			try {
				tv_outqty05.setText(format.format(Float.valueOf(ES_CIRSHEET.get("OUTQTY05").toString())));
			} catch (NumberFormatException E){
				tv_outqty05.setText("0");
			}

			tv_good06.setText(ES_CIRSHEET.get("GOOD06").toString());
			tv_excg06.setText(ES_CIRSHEET.get("EXCG06").toString());
			try {
				tv_outqty06.setText(format.format(Float.valueOf(ES_CIRSHEET.get("OUTQTY06").toString())));
			} catch (NumberFormatException E){
				tv_outqty06.setText("0");
			}

			tv_good07.setText(ES_CIRSHEET.get("GOOD07").toString());
			tv_excg07.setText(ES_CIRSHEET.get("EXCG07").toString());
			try {
				tv_outqty07.setText(format.format(Float.valueOf(ES_CIRSHEET.get("OUTQTY07").toString())));
			} catch (NumberFormatException E){
				tv_outqty07.setText("0");
			}

			tv_good08.setText(ES_CIRSHEET.get("GOOD08").toString());
			tv_excg08.setText(ES_CIRSHEET.get("EXCG08").toString());
			try {
				tv_outqty08.setText(format.format(Float.valueOf(ES_CIRSHEET.get("OUTQTY08").toString())));
			} catch (NumberFormatException E){
				tv_outqty08.setText("0");
			}

			tv_good09.setText(ES_CIRSHEET.get("GOOD09").toString());
			tv_excg09.setText(ES_CIRSHEET.get("EXCG09").toString());
			try {
				tv_outqty09.setText(format.format(Float.valueOf(ES_CIRSHEET.get("OUTQTY09").toString())));
			} catch (NumberFormatException E){
				tv_outqty09.setText("0");
			}

			tv_good10.setText(ES_CIRSHEET.get("GOOD10").toString());
			tv_excg10.setText(ES_CIRSHEET.get("EXCG10").toString());
			try {
				tv_outqty10.setText(format.format(Float.valueOf(ES_CIRSHEET.get("OUTQTY10").toString())));
			} catch (NumberFormatException E){
				tv_outqty10.setText("0");
			}

			tv_good11.setText(ES_CIRSHEET.get("GOOD11").toString());
			tv_excg11.setText(ES_CIRSHEET.get("EXCG11").toString());
			try {
				tv_outqty11.setText(format.format(Float.valueOf(ES_CIRSHEET.get("OUTQTY11").toString())));
			} catch (NumberFormatException E){
				tv_outqty11.setText("0");
			}

			tv_good12.setText(ES_CIRSHEET.get("GOOD12ODCF").toString());
			tv_excg12.setText(ES_CIRSHEET.get("EXCG12ODCF").toString());
			try {
				tv_outqty12.setText(format.format(Float.valueOf(ES_CIRSHEET.get("OUTQTY12").toString())));
			} catch (NumberFormatException E){
				tv_outqty12.setText("0");
			}

			tv_good13.setText(ES_CIRSHEET.get("GOOD12ODCR").toString());
			tv_excg13.setText(ES_CIRSHEET.get("EXCG12ODCR").toString());
			try {
				tv_outqty13.setText(format.format(Float.valueOf(ES_CIRSHEET.get("OUTQTY13").toString())));
			} catch (NumberFormatException E){
				tv_outqty13.setText("0");
			}

			tv_good14.setText(ES_CIRSHEET.get("GOOD14").toString());
			tv_excg14.setText(ES_CIRSHEET.get("EXCG14").toString());
			try {
				tv_outqty14.setText(format.format(Float.valueOf(ES_CIRSHEET.get("OUTQTY14").toString())));
			} catch (NumberFormatException E){
				tv_outqty14.setText("0");
			}

			tv_good15.setText(ES_CIRSHEET.get("GOOD15").toString());
			tv_excg15.setText(ES_CIRSHEET.get("EXCG15").toString());
			try {
				tv_outqty15.setText(format.format(Float.valueOf(ES_CIRSHEET.get("OUTQTY15").toString())));
			} catch (NumberFormatException E){
				tv_outqty15.setText("0");
			}

			tv_good16.setText(ES_CIRSHEET.get("GOOD16").toString());
			tv_excg16.setText(ES_CIRSHEET.get("EXCG16").toString());
			try {
				tv_outqty16.setText(format.format(Float.valueOf(ES_CIRSHEET.get("OUTQTY16").toString())));
			} catch (NumberFormatException E){
				tv_outqty16.setText("0");
			}

			tv_good17.setText(ES_CIRSHEET.get("GOOD17").toString());
			tv_excg17.setText(ES_CIRSHEET.get("EXCG17").toString());
			try {
				tv_outqty17.setText(format.format(Float.valueOf(ES_CIRSHEET.get("OUTQTY17").toString())));
			} catch (NumberFormatException E){
				tv_outqty17.setText("0");
			}

			tv_good18.setText(ES_CIRSHEET.get("GOOD18").toString());
			tv_excg18.setText(ES_CIRSHEET.get("EXCG18").toString());
			try {
				tv_outqty18.setText(format.format(Float.valueOf(ES_CIRSHEET.get("OUTQTY18").toString())));
			} catch (NumberFormatException E){
				tv_outqty18.setText("0");
			}

			tv_good19.setText(ES_CIRSHEET.get("GOOD19").toString());
			tv_excg19.setText(ES_CIRSHEET.get("EXCG19").toString());
			try {
				tv_outqty19.setText(format.format(Float.valueOf(ES_CIRSHEET.get("OUTQTY19").toString())));
			} catch (NumberFormatException E){
				tv_outqty19.setText("0");
			}

			et_ccmbi.setText(ES_CIRSHEET.get("CCMBI").toString());
			tv_usrid.setText(ES_CIRSHEET.get("USRID").toString());
			tv_ename.setText(ES_CIRSHEET.get("ENAME").toString());


			if("".equals(ES_CIRSHEET.get("RCHECK").toString().replace(" ","")))
			{
				iv_confirm_check.setImageResource(R.drawable.check_off);

			}
			else
			{
				iv_confirm_check.setImageResource(R.drawable.check_on);
			}

			
			
			String signUrl = ES_CIRSHEET.get("SIGN_T").toString().substring(39);
			
			kog.e("Jonathan", "signurl :: " + "http://ext.lotterental.net:8001/ktrerp/" + signUrl);
			new DownLoadImageTask(iv_sign).execute("http://ext.lotterental.net:8001/ktrerp/" + signUrl);
//			new DownLoadImageTask(iv_sign).execute("http://ext.lotterental.net:8001/ktrerp/upload/20170202/1771/4401702007.jpg");
//												    http://ext.lotterental.net:8001/ktrerp/p/upload/20170201/1333/4401702250.jpg
//			new DownLoadImageTask(iv_sign).execute("https://www.google.com/images/srpr/logo11w.png");
//			new DownLoadImageTask(iv_sign).execute("http://java.sogeti.nl/JavaBlog/wp-content/uploads/2009/04/android_icon_256.png");


//			
//			et_ccmbi = (EditText)findViewById(R.id.et_ccmbi);
//			tv_usrid = (TextView)findViewById(R.id.tv_usrid);
//			tv_ename = (TextView)findViewById(R.id.tv_ename);
//			iv_sign = (ImageView)findViewById(R.id.iv_sign);
//			
			
			
		}
	}
	
	
	
	
	 private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap>{
	        ImageView imageView = null;

	        public DownLoadImageTask(ImageView imageView){
	            this.imageView = imageView;
	        }

	        /*
	            doInBackground(Params... params)
	                Override this method to perform a computation on a background thread.
	         */
	        protected Bitmap doInBackground(String...urls){
	            String urlOfImage = urls[0];
	            Bitmap logo = null;
	            try{
	                InputStream is = new URL(urlOfImage).openStream();
	                /*
	                    decodeStream(InputStream is)
	                        Decode an input stream into a bitmap.
	                 */
	                logo = BitmapFactory.decodeStream(is);
	            }catch(Exception e){ // Catch the download exception
	                e.printStackTrace();
	            }
	            return logo;
	        }

	        /*
	            onPostExecute(Result result)
	                Runs on the UI thread after doInBackground(Params...).
	         */
	        protected void onPostExecute(Bitmap result){
	            imageView.setImageBitmap(result);
	            
	            connectController.duplicateLogin(mContext);
	            
	        }
	    }
	
	

	// 키보드내리기
//	Handler pre = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//			imm.hideSoftInputFromWindow(et_carnum.getWindowToken(), 0);
//		}
//	};

	@Override
	public void reDownloadDB(String newVersion) {
	}

	public HashMap<String, String> getSelectedAddress() {
		return mSelectedMap;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.bt_screencapture_checklist:
			
//			Bitmap bitmap = takeScreenshot();
//	        saveBitmap(bitmap);
	        
	        
//			Bitmap bitmap = loadBitmapFromView(ll_maintenance_checklist, 713, 956);
//	        saveBitmap(bitmap);
	        
	        
	        ScrollView iv = (ScrollView) findViewById(R.id.sv_maintenance_checklist);
	        Bitmap bitmap = Bitmap.createBitmap(
	              iv.getChildAt(0).getWidth(), 
	              iv.getChildAt(0).getHeight(), 
	              Bitmap.Config.ARGB_8888);
	        Canvas c = new Canvas(bitmap);
	        iv.getChildAt(0).draw(c);
	        saveBitmap(bitmap);
	        
			break;
		
		
			
			
		case R.id.bt_maintenance_checklist_close:
			
			dismiss();
			
			break;
			
			
			
		
		
		
		}
	}



	public PartsTransfer_Cars_Dialog_Adapter getPcda() {
		return pcda;
	}

	public ArrayList<HashMap<String, String>> getArray_hash() {
		return array_hash;
	}
}
