package com.ktrental.product;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.ktrental.R;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.fragment.TireFragment;
import com.ktrental.model.TableModel;
import com.ktrental.util.LogUtil;

public class Menu4_1_Activity extends BaseActivity implements ConnectInterface, OnClickListener
{
	TireFragment mainFragment;
//	EditText mEditDummy;
	
	@Override
	 protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu4_1_activity);
//		init(500);
		
		mainFragment = new TireFragment(TireFragment.class.getName(), null, "", "");
		getSupportFragmentManager().beginTransaction()
		.add(R.id.layout_call_fragment, mainFragment)
		.commit();
		
//		mEditDummy = (EditText)findViewById(R.id.editDummy);
//		pp.hide();
		new Handler().postDelayed(mShowRunnable, 300);
		
	}
	
	//myung 20131203 ADD 상태 변경 시 로딩바 추가 필요.
    private Runnable mShowRunnable = new Runnable() {
        @Override
        public void run() {
    		
    		
        	init(500);
        }
    };

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectResponse(String FuntionName, String resultText,
			String MTYPE, int resulCode, TableModel tableModel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reDownloadDB(String newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		finish();
		if(pp != null && pp.isShowing()) {
			pp.dismiss();
		}
		super.onBackPressed();
	}
	
	@Override
	protected void onPause() {

		if(pp != null && pp.isShowing()) {
			pp.dismiss();
		}
		super.onPause();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(childFragment != null){
			try {
				childFragment.onActivityResult(requestCode, resultCode, data);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
//		mDrawerFragment.onActivityResult(requestCode, requestCode, data);
		LogUtil.d("TAG", "requestCode = " + requestCode);
		LogUtil.d("TAG", "resultCode = " + resultCode);
	}

	private Fragment childFragment = null;
	public void setFragment(Fragment fragment){
		childFragment = fragment;
	}
}
