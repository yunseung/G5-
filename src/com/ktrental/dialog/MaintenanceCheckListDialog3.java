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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class MaintenanceCheckListDialog3 extends DialogC implements ConnectInterface, View.OnClickListener {
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
    private ConnectController connectController;


    private ArrayList<HashMap<String, String>> array_hash;
    private ArrayList<RepairPartList> array_repairParts = new ArrayList<>();

//	private Popup_Window_PM013 ppm013popup;

    private TextView title_id;
    private Button bt_done;

    private ScrollView sv_maintenance_checklist;
    private RelativeLayout rl_maintenance_checklist;
    private LinearLayout ll_maintenance_checklist;
    private Button bt_screencapture_checklist;


    private Button bt_maintenance_checklist_close;

    private class RepairPartList {
        private TextView tv_index;
        private TextView tv_good;
        private TextView tv_outqty;
        private TextView tv_price;
        private TextView tv_vat;
        private TextView tv_total_price;

        public RepairPartList(TextView index, TextView good, TextView outqty, TextView price, TextView vat, TextView totalPrice) {
            this.tv_index = index;
            this.tv_good = good;
            this.tv_outqty = outqty;
            this.tv_price = price;
            this.tv_vat = vat;
            this.tv_total_price = totalPrice;
        }
    }


    private TextView tv_invnr;    //차량번호
    private TextView tv_maktx;    // 차종
    private TextView tv_dlsm1;    // 고객
    private TextView tv_incml;    // 주행거리
    private TextView tv_reg_date; // 차량등록일자
    private TextView tv_repair_reserve_date; // 차량등록일자
    private TextView tv_repair_complete_date; // 차량등록일자
    private TextView tv_round_car;
    private TextView tv_repair_engineer;


    private TextView tv_index01;
    private TextView tv_good01;    // 작업내용
    private TextView tv_outqty01;    // 항목 수량
    private TextView tv_price01; // 항목 금액
    private TextView tv_vat01; // 부가세 금액
    private TextView tv_total_price01; // 항목 금액 * 수량


    private TextView tv_index02;
    private TextView tv_good02;    // 작업내용
    private TextView tv_outqty02;    // 항목 수량
    private TextView tv_price02; // 항목 금액
    private TextView tv_vat02; // 부가세 금액
    private TextView tv_total_price02; // 항목 금액 * 수량

    private TextView tv_index03;
    private TextView tv_good03;    // 작업내용
    private TextView tv_outqty03;    // 항목 수량
    private TextView tv_price03; // 항목 금액
    private TextView tv_vat03; // 부가세 금액
    private TextView tv_total_price03; // 항목 금액 * 수량

    private TextView tv_index04;
    private TextView tv_good04;    // 작업내용
    private TextView tv_outqty04;    // 항목 수량
    private TextView tv_price04; // 항목 금액
    private TextView tv_vat04; // 부가세 금액
    private TextView tv_total_price04; // 항목 금액 * 수량

    private TextView tv_index05;
    private TextView tv_good05;    // 작업내용
    private TextView tv_outqty05;    // 항목 수량
    private TextView tv_price05; // 항목 금액
    private TextView tv_vat05; // 부가세 금액
    private TextView tv_total_price05; // 항목 금액 * 수량

    private TextView tv_index06;
    private TextView tv_good06;    // 작업내용
    private TextView tv_outqty06;    // 항목 수량
    private TextView tv_price06; // 항목 금액
    private TextView tv_vat06; // 부가세 금액
    private TextView tv_total_price06; // 항목 금액 * 수량

    private TextView tv_index07;
    private TextView tv_good07;    // 작업내용
    private TextView tv_outqty07;    // 항목 수량
    private TextView tv_price07; // 항목 금액
    private TextView tv_vat07; // 부가세 금액
    private TextView tv_total_price07; // 항목 금액 * 수량

    private TextView tv_index08;
    private TextView tv_good08;    // 작업내용
    private TextView tv_outqty08;    // 항목 수량
    private TextView tv_price08; // 항목 금액
    private TextView tv_vat08; // 부가세 금액
    private TextView tv_total_price08; // 항목 금액 * 수량

    private TextView tv_index09;
    private TextView tv_good09;    // 작업내용
    private TextView tv_outqty09;    // 항목 수량
    private TextView tv_price09; // 항목 금액
    private TextView tv_vat09; // 부가세 금액
    private TextView tv_total_price09; // 항목 금액 * 수량

    private TextView tv_index10;
    private TextView tv_good10;    // 작업내용
    private TextView tv_outqty10;    // 항목 수량
    private TextView tv_price10; // 항목 금액
    private TextView tv_vat10; // 부가세 금액
    private TextView tv_total_price10; // 항목 금액 * 수량

    private TextView tv_index11;
    private TextView tv_good11;    // 작업내용
    private TextView tv_outqty11;    // 항목 수량
    private TextView tv_price11; // 항목 금액
    private TextView tv_vat11; // 부가세 금액
    private TextView tv_total_price11; // 항목 금액 * 수량

    private TextView tv_index12;
    private TextView tv_good12;    // 작업내용
    private TextView tv_outqty12;    // 항목 수량
    private TextView tv_price12; // 항목 금액
    private TextView tv_vat12; // 부가세 금액
    private TextView tv_total_price12; // 항목 금액 * 수량

    private TextView tv_total1;
    private TextView tv_vat;
    private TextView tv_total2;

    private TextView tv_ename;    //점검자 성명
    private ImageView iv_sign;    //고객확인

    private TextView tv_year;
    private TextView tv_month;
    private TextView tv_day;

    private LinearLayout mCheckingArea, mSignArea;


    private ImageView iv_confirm_check;


    private String TEL_NUMBER = "";


    boolean isCheck;

    public MaintenanceCheckListDialog3(Context context, String reqNo) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.maintenance_checklist_dialog3);
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


        title_id = (TextView) findViewById(R.id.title_id);
        sv_maintenance_checklist = (ScrollView) findViewById(R.id.sv_maintenance_checklist);
        rl_maintenance_checklist = (RelativeLayout) findViewById(R.id.rl_maintenance_checklist);
        ll_maintenance_checklist = (LinearLayout) findViewById(R.id.ll_maintenance_checklist);

        bt_screencapture_checklist = (Button) findViewById(R.id.bt_screencapture_checklist);
        bt_screencapture_checklist.setOnClickListener(this);


        bt_maintenance_checklist_close = (Button) findViewById(R.id.bt_maintenance_checklist_close);
        bt_maintenance_checklist_close.setOnClickListener(this);


        tv_invnr = (TextView) findViewById(R.id.tv_invnr);
        tv_maktx = (TextView) findViewById(R.id.tv_maktx);
        tv_dlsm1 = (TextView) findViewById(R.id.tv_dlsm1);
        tv_incml = (TextView) findViewById(R.id.tv_incml);
        tv_reg_date = (TextView) findViewById(R.id.tv_reg_date);
        tv_repair_reserve_date = (TextView) findViewById(R.id.tv_repair_reserve_date);
        tv_repair_complete_date = (TextView) findViewById(R.id.tv_repair_complete_date);
        tv_round_car = (TextView) findViewById(R.id.tv_round_car);
        tv_repair_engineer = (TextView) findViewById(R.id.tv_repair_engineer);

        tv_index01 = (TextView) findViewById(R.id.tv_index1);
        tv_good01 = (TextView) findViewById(R.id.tv_good01);
        tv_outqty01 = (TextView) findViewById(R.id.tv_outqty01);
        tv_price01 = (TextView) findViewById(R.id.tv_price01);
        tv_vat01 = (TextView) findViewById(R.id.tv_vat01);
        tv_total_price01 = (TextView) findViewById(R.id.tv_total_price01);
        array_repairParts.add(new RepairPartList(tv_index01, tv_good01, tv_outqty01, tv_price01, tv_vat01, tv_total_price01));


        tv_index02 = (TextView) findViewById(R.id.tv_index2);
        tv_good02 = (TextView) findViewById(R.id.tv_good02);
        tv_outqty02 = (TextView) findViewById(R.id.tv_outqty02);
        tv_price02 = (TextView) findViewById(R.id.tv_price02);
        tv_vat02 = (TextView) findViewById(R.id.tv_vat02);
        tv_total_price02 = (TextView) findViewById(R.id.tv_total_price02);
        array_repairParts.add(new RepairPartList(tv_index02, tv_good02, tv_outqty02, tv_price02, tv_vat02, tv_total_price02));

        tv_index03 = (TextView) findViewById(R.id.tv_index3);
        tv_good03 = (TextView) findViewById(R.id.tv_good03);
        tv_outqty03 = (TextView) findViewById(R.id.tv_outqty03);
        tv_price03 = (TextView) findViewById(R.id.tv_price03);
        tv_vat03 = (TextView) findViewById(R.id.tv_vat03);
        tv_total_price03 = (TextView) findViewById(R.id.tv_total_price03);
        array_repairParts.add(new RepairPartList(tv_index03, tv_good03, tv_outqty03, tv_price03, tv_vat03, tv_total_price03));

        tv_index04 = (TextView) findViewById(R.id.tv_index4);
        tv_good04 = (TextView) findViewById(R.id.tv_good04);
        tv_outqty04 = (TextView) findViewById(R.id.tv_outqty04);
        tv_price04 = (TextView) findViewById(R.id.tv_price04);
        tv_vat04 = (TextView) findViewById(R.id.tv_vat04);
        tv_total_price04 = (TextView) findViewById(R.id.tv_total_price04);
        array_repairParts.add(new RepairPartList(tv_index04, tv_good04, tv_outqty04, tv_price04, tv_vat04, tv_total_price04));

        tv_index05 = (TextView) findViewById(R.id.tv_index5);
        tv_good05 = (TextView) findViewById(R.id.tv_good05);
        tv_outqty05 = (TextView) findViewById(R.id.tv_outqty05);
        tv_price05 = (TextView) findViewById(R.id.tv_price05);
        tv_vat05 = (TextView) findViewById(R.id.tv_vat05);
        tv_total_price05 = (TextView) findViewById(R.id.tv_total_price05);
        array_repairParts.add(new RepairPartList(tv_index05, tv_good05, tv_outqty05, tv_price05, tv_vat05, tv_total_price05));


        tv_index06 = (TextView) findViewById(R.id.tv_index6);
        tv_good06 = (TextView) findViewById(R.id.tv_good06);
        tv_outqty06 = (TextView) findViewById(R.id.tv_outqty06);
        tv_price06 = (TextView) findViewById(R.id.tv_price06);
        tv_vat06 = (TextView) findViewById(R.id.tv_vat06);
        tv_total_price06 = (TextView) findViewById(R.id.tv_total_price06);
        array_repairParts.add(new RepairPartList(tv_index06, tv_good06, tv_outqty06, tv_price06, tv_vat06, tv_total_price06));


        tv_index07 = (TextView) findViewById(R.id.tv_index7);
        tv_good07 = (TextView) findViewById(R.id.tv_good07);
        tv_outqty07 = (TextView) findViewById(R.id.tv_outqty07);
        tv_price07 = (TextView) findViewById(R.id.tv_price07);
        tv_vat07 = (TextView) findViewById(R.id.tv_vat07);
        tv_total_price07 = (TextView) findViewById(R.id.tv_total_price07);
        array_repairParts.add(new RepairPartList(tv_index07, tv_good07, tv_outqty07, tv_price07, tv_vat07, tv_total_price07));

        tv_index08 = (TextView) findViewById(R.id.tv_index8);
        tv_good08 = (TextView) findViewById(R.id.tv_good08);
        tv_outqty08 = (TextView) findViewById(R.id.tv_outqty08);
        tv_price08 = (TextView) findViewById(R.id.tv_price08);
        tv_vat08 = (TextView) findViewById(R.id.tv_vat08);
        tv_total_price08 = (TextView) findViewById(R.id.tv_total_price08);
        array_repairParts.add(new RepairPartList(tv_index08, tv_good08, tv_outqty08, tv_price08, tv_vat08, tv_total_price08));

        tv_index09 = (TextView) findViewById(R.id.tv_index9);
        tv_good09 = (TextView) findViewById(R.id.tv_good09);
        tv_outqty09 = (TextView) findViewById(R.id.tv_outqty09);
        tv_price09 = (TextView) findViewById(R.id.tv_price09);
        tv_vat09 = (TextView) findViewById(R.id.tv_vat09);
        tv_total_price09 = (TextView) findViewById(R.id.tv_total_price09);
        array_repairParts.add(new RepairPartList(tv_index09, tv_good09, tv_outqty09, tv_price09, tv_vat09, tv_total_price09));

        tv_index10 = (TextView) findViewById(R.id.tv_index10);
        tv_good10 = (TextView) findViewById(R.id.tv_good10);
        tv_outqty10 = (TextView) findViewById(R.id.tv_outqty10);
        tv_price10 = (TextView) findViewById(R.id.tv_price10);
        tv_vat10 = (TextView) findViewById(R.id.tv_vat10);
        tv_total_price10 = (TextView) findViewById(R.id.tv_total_price10);
        array_repairParts.add(new RepairPartList(tv_index10, tv_good10, tv_outqty10, tv_price10, tv_vat10, tv_total_price10));

        tv_index11 = (TextView) findViewById(R.id.tv_index11);
        tv_good11 = (TextView) findViewById(R.id.tv_good11);
        tv_outqty11 = (TextView) findViewById(R.id.tv_outqty11);
        tv_price11 = (TextView) findViewById(R.id.tv_price11);
        tv_vat11 = (TextView) findViewById(R.id.tv_vat11);
        tv_total_price11 = (TextView) findViewById(R.id.tv_total_price11);
        array_repairParts.add(new RepairPartList(tv_index11, tv_good11, tv_outqty11, tv_price11, tv_vat11, tv_total_price11));

        tv_index12 = (TextView) findViewById(R.id.tv_index12);
        tv_good12 = (TextView) findViewById(R.id.tv_good12);
        tv_outqty12 = (TextView) findViewById(R.id.tv_outqty12);
        tv_price12 = (TextView) findViewById(R.id.tv_price12);
        tv_vat12 = (TextView) findViewById(R.id.tv_vat12);
        tv_total_price12 = (TextView) findViewById(R.id.tv_total_price12);
        array_repairParts.add(new RepairPartList(tv_index12, tv_good12, tv_outqty12, tv_price12, tv_vat12, tv_total_price12));

        tv_total1 = (TextView) findViewById(R.id.tv_total1);
        tv_vat = (TextView) findViewById(R.id.tv_vat);
        tv_total2 = (TextView) findViewById(R.id.tv_total2);


        tv_ename = (TextView) findViewById(R.id.tv_ename);
        iv_sign = (ImageView) findViewById(R.id.iv_sign);

        tv_year = (TextView) findViewById(R.id.tv_year);
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_day = (TextView) findViewById(R.id.tv_day);

        iv_confirm_check = (ImageView) findViewById(R.id.iv_confirm_check);

        connectController.getZMO_1170_RD11(reqNo);

    }

    public void setTitle(String str) {
        title_id.setText(str);
    }

    public void setDone(String str) {
//		bt_done.setText(str);
    }

    private String currencyFormat(int input) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");

        return decimalFormat.format(input);
    }

    @Override
    public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode, TableModel tableModel) {

        hideProgress();


        if (FuntionName.equals("ZMO_1170_RD11")) {

            HashMap<String, String> ES_HEADER = new HashMap<String, String>();
            ES_HEADER = tableModel.getStruct("ES_HEADER");
            HashMap<String, String> ES_FOOTER = new HashMap<String, String>();
            ES_FOOTER = tableModel.getStruct("ES_FOOTER");
            array_hash = tableModel.getTableArray();

            tv_invnr.setText(ES_HEADER.get("INVNR").toString());
            tv_maktx.setText(ES_HEADER.get("MAKTX").toString());
            tv_incml.setText(currencyFormat(Integer.parseInt(ES_HEADER.get("INCML"))) + "km");
            tv_dlsm1.setText(ES_HEADER.get("KUNNRNM").toString());

            tv_year.setText(ES_HEADER.get("GLTRI").split("-", 0)[0]);
            tv_month.setText(ES_HEADER.get("GLTRI").split("-", 0)[1]);
            tv_day.setText(ES_HEADER.get("GLTRI").split("-", 0)[2]);

            tv_reg_date.setText(ES_HEADER.get("INBDT"));
            tv_repair_reserve_date.setText(ES_HEADER.get("REQDT"));
            String completeDate = ES_HEADER.get("GLTRI") + "  " + ES_HEADER.get("GEUZI");
            tv_repair_complete_date.setText(completeDate);
            tv_round_car.setText(ES_HEADER.get("MINVNR"));
            tv_repair_engineer.setText(ES_HEADER.get("USR_NM").toString()); // 화면 맨 밑 작성자도 같은 필드 사용하면 됨.
            tv_ename.setText(ES_HEADER.get("USR_NM").toString());

            tv_total1.setText(ES_FOOTER.get("DMBTR_T"));
            tv_vat.setText(ES_FOOTER.get("DMBTR_T_VAT"));
            tv_total2.setText(ES_FOOTER.get("DMBTR_T2"));

            // 작업내용 MAKTX
            // 수량 ERFMG
            // 금액 DMBTR
            // 총 금액 DMBTR_T
            for (int i = 0; i < array_hash.size(); i++) {
                array_repairParts.get(i).tv_index.setVisibility(View.VISIBLE);
                array_repairParts.get(i).tv_good.setVisibility(View.VISIBLE);
                array_repairParts.get(i).tv_outqty.setVisibility(View.VISIBLE);
                array_repairParts.get(i).tv_price.setVisibility(View.VISIBLE);
                array_repairParts.get(i).tv_vat.setVisibility(View.VISIBLE);
                array_repairParts.get(i).tv_total_price.setVisibility(View.VISIBLE);

                array_repairParts.get(i).tv_index.setText("" + (i + 1));
                array_repairParts.get(i).tv_good.setText("" + array_hash.get(i).get("MAKTX"));
                array_repairParts.get(i).tv_outqty.setText("" + array_hash.get(i).get("ERFMG"));
                array_repairParts.get(i).tv_price.setText("" + array_hash.get(i).get("DMBTR"));
                array_repairParts.get(i).tv_vat.setText("" + array_hash.get(i).get("DMBTR_VAT"));
                array_repairParts.get(i).tv_total_price.setText("" + array_hash.get(i).get("DMBTR_T"));
            }


//            String signUrl = ES_FOOTER.get("SIGN_T").toString().substring(39);
//            new DownLoadImageTask(iv_sign).execute("http://erp.lotterental.net:8002/ktrerp/upload" + signUrl);
            new DownLoadImageTask(iv_sign).execute(ES_FOOTER.get("SIGN_T"));
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


    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
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
        File newDir = new File(Environment.getExternalStorageDirectory()
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
            context.startActivity(Intent.createChooser(intent, "Send"));


        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }

    }

    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView = null;

        public DownLoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();
	                /*
	                    decodeStream(InputStream is)
	                        Decode an input stream into a bitmap.
	                 */
                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e) { // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);

            connectController.duplicateLogin(mContext);

        }
    }

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

            case R.id.btn_sign:
                break;

        }
    }

    private void clickSign() {

//        if (mResultModel != null) {
//            kog.e("KDH", "mResultModel.getmCarInfoModel().getAUFNR() = " + mResultModel.getmCarInfoModel().getAUFNR());
//            SignFragment signFragment = new SignFragment(mEtName.getText().toString(), mTvContact.getText().toString(),
//                    this, mResultModel.getmCarInfoModel().getAUFNR(),
//                    // mResultModel.getmCarInfoModel().getImageName(),
//                    mResultModel.getmCarInfoModel().getCUSTOMER_NAME(), mResultModel.getmCarInfoModel().getCarNum());
//
//            // myung 20131121 2560대응
//            // int tempX = 1060;
//            // int tempY = 614;
//            // if (DEFINE.DISPLAY.equals("2560"))
//            // {
//            // tempX *= 2;
//            // tempY *= 2;
//            // }
//            signFragment.show(getChildFragmentManager(), "SignFragment");// ,
//            // tempX,
//            // tempY);
//        }
    }


    public PartsTransfer_Cars_Dialog_Adapter getPcda() {
        return pcda;
    }

    public ArrayList<HashMap<String, String>> getArray_hash() {
        return array_hash;
    }
}
