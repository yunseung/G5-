package com.ktrental.product;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.ktrental.R;
import com.ktrental.cm.connect.Connector.ConnectInterface;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.fragment.MovementFragment;
import com.ktrental.model.LoginModel;
import com.ktrental.model.TableModel;
import com.ktrental.util.kog;

public class Menu7_2_Movement_Activity extends BaseActivity implements ConnectInterface, OnClickListener
{
	
	MovementFragment mMovementFragment;
//	EditText mEditDummy;
	
	@Override 
	 protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu7_2_movement_activity);
		
		init();

		LoginModel model = KtRentalApplication.getLoginModel();
		mMovementFragment = new MovementFragment(model.getEqunr(),
				model.getPernr(), " ", model.getInvnr(), model.getFUELNM(),
				model.getEname(), true);
		
		
		
	
		
		
		getSupportFragmentManager().beginTransaction()
		.add(R.id.layout_movement_fragment, mMovementFragment)
		.commit();
		
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
