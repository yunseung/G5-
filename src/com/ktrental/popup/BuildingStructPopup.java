package com.ktrental.popup;

import java.util.ArrayList;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.ktrental.R;
import com.ktrental.adapter.AddressWheelAdapter;
import com.ktrental.cm.connect.ConnectController;
import com.ktrental.cm.db.DbAsyncTask;
import com.ktrental.cm.db.DbAsyncTask.DbAsyncResLintener;
import com.ktrental.model.DbQueryModel;

public class BuildingStructPopup extends PopupWindow implements
		DbAsyncResLintener {

	private Context mContext;
	private int mBuildingStructureCode = 0;
	private View mAnchorView;
	private ArrayList<String> mBuildingStructArr = new ArrayList<String>();
	private AddressWheelAdapter mAddressWheelAdapter;
	private WheelView mWheelView;

	private String currentText = "";

	private OnWheelChangedListener mOnWheelChangedListener;

	public BuildingStructPopup(View root, Context context, int orientation,
			int layoutId, int buildingStructureCode,
			OnWheelChangedListener listener) {
		// super(context, orientation);
		super(root, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mOnWheelChangedListener = listener;
		mContext = context;
		mBuildingStructureCode = buildingStructureCode;
		initSettingView(root);
	}

	private void initSettingView(View root) {

		// ViewGroup root = (ViewGroup) LayoutInflater.from(mContext).inflate(
		// layoutId, null, false);
		//
		// setContentView(root);
		mWheelView = (WheelView) root.findViewById(R.id.wv_struct);
		mWheelView.setCenterDrawable(mContext.getResources().getDrawable(
				R.drawable.wheel_val));
		mWheelView.addChangingListener(mOnWheelChangedListener);
	}

	private void getBuildingStructure(int buildingStructureCode,DbAsyncResLintener lintener) {

		String[] _whereArgs = { String.valueOf(buildingStructureCode) };
		String[] _whereCause = { "ZCODEH2" };
		String[] colums = { "ZCODEVT" };

		DbQueryModel dbQueryModel = new DbQueryModel(
				ConnectController.O_ITAB1_TABLE_NAME, _whereCause, _whereArgs,
				colums);

		DbAsyncTask dbAsyncTask = new DbAsyncTask("getBuildingStructure",mContext, this, dbQueryModel);
		dbAsyncTask.execute(DbAsyncTask.DB_SELECT);
	}

	private void setArray(Cursor cursor) {
		if (cursor == null)
			return;

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			mBuildingStructArr.add(cursor.getString(0));
			cursor.moveToNext();

		}
		cursor.close();
	}

	@Override
	public void onCompleteDB(String funName, int type,Cursor cursor, String tableName) {
		// TODO Auto-generated method stub

		setArray(cursor);

		AddressWheelAdapter cityAdapter = new AddressWheelAdapter(mContext,
				mBuildingStructArr);
		cityAdapter.setItemResource(R.layout.wheel_text_item);
		cityAdapter.setItemTextResource(R.id.text);
		mWheelView.setViewAdapter(cityAdapter);

		// super.show(mAnchorView);
		//showAsDropDown(mAnchorView);
	}

	public void show(View anchor) {
		mAnchorView = anchor;
		getBuildingStructure(mBuildingStructureCode,this);
		showAsDropDown(mAnchorView);

	}
	
	public String getArrayText(int index){
		String reString = "";
		reString = mBuildingStructArr.get(index);
		
		return reString;
	}
}
