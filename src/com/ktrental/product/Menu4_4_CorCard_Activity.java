package com.ktrental.product;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.ktrental.R;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.fragment.CorCardRegFragment;
import com.ktrental.model.TableModel;

public class Menu4_4_CorCard_Activity extends BaseActivity implements ConnectInterface, OnClickListener
{

	private CorCardRegFragment mCorCardRegFragment;

	@Override
	 protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu5_activity);
		
		init();

		mCorCardRegFragment = new CorCardRegFragment(model.getEqunr(), model.getPernr(), " ", model.getInvnr(), model.getFUELNM(), model.getEname(), false);

		getSupportFragmentManager().beginTransaction()
		.add(R.id.layout_call_fragment, mCorCardRegFragment)
		.commit();
		
//		mEditDummy = (EditText)findViewById(R.id.editDummy);
//		pp.hide();
		
	}

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
		pp.dismiss();
		super.onBackPressed();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		pp.dismiss();
		super.onPause();
	}

}
