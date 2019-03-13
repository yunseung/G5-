package com.ktrental.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.model.BaseMaintenanceModel;
import com.ktrental.model.LoginModel;
import com.ktrental.model.TableModel;
import com.ktrental.popup.EventPopupC;
import com.ktrental.util.OnChangeFragmentListener;

import java.util.ArrayList;

public class SetupFragment extends BaseResultFragment implements OnClickListener, ConnectInterface
{

    private Context                            context;
    
    private TextView tv_user;
    private TextView tv_part;
    private TextView tv_web;
    private TextView tv_car;
    private TextView tv_serial;
    private TextView tv_ver;
    public SetupFragment(){}

    public SetupFragment(String className, OnChangeFragmentListener changeFragmentListener)
        {
        super(className, changeFragmentListener);
        }

    @Override
    public void onAttach(Activity activity)
        {
        context = activity;

        super.onAttach(activity);
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState)
        {
        View v = inflater.inflate(R.layout.setupfragment, null);
        tv_user = (TextView)v.findViewById(R.id.setup_user_id);
        tv_part = (TextView)v.findViewById(R.id.setup_part_id);
        tv_web = (TextView)v.findViewById(R.id.setup_web_id);
        tv_car = (TextView)v.findViewById(R.id.setup_car_id);
        tv_serial = (TextView)v.findViewById(R.id.setup_serial_id);
        tv_ver = (TextView)v.findViewById(R.id.setup_ver_id);
        
        LoginModel lm = KtRentalApplication.getLoginModel();
        String user = lm.getEname();
        String part = lm.getLogid();
        String web = lm.getDeptcd();
        String car = lm.getInvnr();
        String serial = android.os.Build.SERIAL;
        String ver = getAppVer();
        
        tv_user.setText(user);
        tv_part.setText(part);
        tv_web.setText(web);
        tv_car.setText(car);
        tv_serial.setText(serial);
        tv_ver.setText(ver);

        return v;
        }
    
    private String getAppVer()
        {
        String version = "0";
        try {
            PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = i.versionName;
            }
        catch(NameNotFoundException e) { e.printStackTrace(); }
        return version;
        }

    @Override
    public void connectResponse(String FuntionName, String resultText, String MTYPE, int resulCode, TableModel tableModel)
        {
//        Log.i("#", "####" + FuntionName + "/" + resultText + "/" + MTYPE + "/" + resulCode);
        hideProgress();
        if (MTYPE == null || !MTYPE.equals("S"))
            {
            EventPopupC epc = new EventPopupC(context);
            epc.show(resultText);
            return;
            }
        if (FuntionName.equals("ZMO_1060_RD04"))
            {}
        else if (FuntionName.equals("ZMO_1060_RD03"))
            {}
        else if (FuntionName.equals("ZMO_1040_WR02"))
            {
            EventPopupC epc = new EventPopupC(context);
            epc.show("업데이트 되었습니다.");
            }
        }

    public String getDateFormat(String date)
        {
        StringBuffer sb = new StringBuffer(date);
        if (date.length() == 8)
            {
            sb.insert(4, ".");
            int last = sb.length() - 2;
            sb.insert(last, ".");
            }
        return sb.toString();
        }

    @Override
    public void reDownloadDB(String newVersion)
        {}

    @Override
    public void onClick(View arg0)
        {
        switch (arg0.getId())
            {
            case R.id.aabbcc:
            break;
            }
        }

    @Override
    protected void movePlan(ArrayList<BaseMaintenanceModel> models)
        {
        // TODO Auto-generated method stub
        
        }

}
