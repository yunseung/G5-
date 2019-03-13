package com.ktrental.adapter;

import java.text.DecimalFormat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.model.MovementModel;
import com.ktrental.util.CommonUtil;
/**
 * </br> 차량운행일지를 정보 리스트를 보여주는 Adapter 이다. </br>
 * 
 * @author hongsungil
 */
public class MovementAdapter extends BaseCommonAdapter<MovementModel> //implements OnClickListener 
{

	public MovementAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {
		// TODO Auto-generated method stub
		MovementtViewHolder holder = (MovementtViewHolder) rootView.getTag();
		MovementModel model = getItem(position);

		if (model != null) {
			
			holder.tvOTYPE_TX.setText(model.getOTYPE_TX());
			holder.tvAUFNR.setText(model.getAUFNR());
			holder.tvDRVNAM.setText(model.getDRVNAM());
			holder.tvOSTEP_TX.setText(model.getOSTEP_TX());
			holder.tvDATUM.setText(CommonUtil.setDotDate(model.getDATUM()));
			holder.tvUZEIT.setText(CommonUtil.setDotTime(model.getUZEIT()));
			holder.tvVKBUR.setText(model.getVKBUR());
			
			//myung 20131210 UPDATE 리스트의 주행거리 포맷팅
			int tempMILAG = Integer.valueOf(model.getMILAG());
		    DecimalFormat df = new DecimalFormat("#,##0");

			holder.tvMILAG.setText(df.format(tempMILAG) + " km");
			holder.tvDIVSN_TX.setText(model.getDIVSN_TX());
			holder.tvGDOIL.setText(model.getGDOIL() + " L");
			holder.tvGDOIL2.setText(model.getGDOIL2() + " L");

			holder.tv_OILPRI.setText(model.getOILPRI() + " 원");
			holder.tv_WASH.setText(model.getWASH() + " 원");
			holder.tv_PARK.setText(model.getPARK() + " 원");
			holder.tv_TOLL.setText(model.getTOLL() + " 원");
			holder.tv_OTRAMT.setText(model.getOTRAMT() + " 원");
			holder.tv_REASON_TX.setText(model.getREASON_TX() + " ");
			holder.tv_PTRAN2.setText(model.getPTRAN2() + " ");



			
			if (model.isCheckFlag()) {
				holder.ivCheck.setImageResource(R.drawable.check_on);
			} else {
				holder.ivCheck.setImageResource(R.drawable.check_off);
			}
		}
	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {
		View rootView = mInflater.inflate(R.layout.movement_row, viewgroup,
				false);

		MovementtViewHolder holder = new MovementtViewHolder();
		
		// 2014.01.09	ypkim
		// add check box 
		holder.ivCheck = (ImageView) rootView.findViewById(R.id.iv_check);
//		holder.llRoot = (LinearLayout) rootView.findViewById(R.id.ll_root);
//		
		holder.tvOTYPE_TX = (TextView) rootView.findViewById(R.id.tv_OTYPE_TX);
		holder.tvAUFNR = (TextView) rootView.findViewById(R.id.tv_AUFNR);
		holder.tvDRVNAM = (TextView) rootView.findViewById(R.id.tv_DRVNAM);
		holder.tvOSTEP_TX = (TextView) rootView.findViewById(R.id.tv_OSTEP_TX);
		holder.tvDATUM = (TextView) rootView.findViewById(R.id.tv_DATUM);
		holder.tvUZEIT = (TextView) rootView.findViewById(R.id.tv_UZEIT);
		holder.tvVKBUR = (TextView) rootView.findViewById(R.id.tv_VKBUR);
		holder.tvMILAG = (TextView) rootView.findViewById(R.id.tv_MILAG);
		holder.tvDIVSN_TX = (TextView) rootView.findViewById(R.id.tv_DIVSN_TX);
		holder.tvGDOIL = (TextView) rootView.findViewById(R.id.tv_GDOIL);
		holder.tvGDOIL2 = (TextView) rootView.findViewById(R.id.tv_GDOIL2);

		holder.tv_OILPRI = (TextView) rootView.findViewById(R.id.tv_OILPRI);
		holder.tv_WASH = (TextView) rootView.findViewById(R.id.tv_WASH);
		holder.tv_PARK = (TextView) rootView.findViewById(R.id.tv_PARK);
		holder.tv_TOLL = (TextView) rootView.findViewById(R.id.tv_TOLL);
		holder.tv_OTRAMT = (TextView) rootView.findViewById(R.id.tv_OTRAMT);
		holder.tv_REASON_TX = (TextView) rootView.findViewById(R.id.tv_REASON_TX);
		holder.tv_PTRAN2 = (TextView) rootView.findViewById(R.id.tv_PTRAN2);

		
//		holder.ivCheck.setOnClickListener(this);
//		holder.llRoot.setOnClickListener(this);
		rootView.setTag(holder);
		
		return rootView;
	}

	private class MovementtViewHolder {
		ImageView ivCheck;
//		LinearLayout llRoot;
		TextView tvOTYPE_TX;
		TextView tvAUFNR;
		TextView tvDRVNAM;
		TextView tvOSTEP_TX;
		TextView tvDATUM;
		TextView tvUZEIT;
		TextView tvVKBUR;
		TextView tvMILAG;
		TextView tvDIVSN_TX;
		TextView tvGDOIL;
		TextView tvGDOIL2;

		TextView tv_OILPRI;
		TextView tv_WASH;
		TextView tv_PARK;
		TextView tv_TOLL;
		TextView tv_OTRAMT;
		TextView tv_REASON_TX;
		TextView tv_PTRAN2;


	}
	
	/*
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_check:
			clickCheck2(v);
			break;
		case R.id.ll_root:
//			clickCheck2(v);
			break;

		default:
			break;
	*/
	
	private void clickCheck2(View v) {
//		final int position = (Integer) v.getTag();
		// checkItem(position);
		
		MovementModel model = (MovementModel)v.getTag();

		if (model != null) {
			if (model.isCheckFlag()) {
				model.setCheckFlag(false);
			} else {
				model.setCheckFlag(true);
			}
			notifyDataSetChanged();
		}
	}

}
