package com.ktrental.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.model.CorCardModel;

import java.util.ArrayList;

/**
 * </br> 법인카드 Adapter 이다. </br>
 * 
 * @author hongsungil
 */
public class CorCardAdapter extends BaseCommonAdapter<CorCardModel> implements View.OnClickListener//implements OnClickListener
{

	private int mPosition = 0;
	public CorCardAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {
		// TODO Auto-generated method stub
		CorCardViewHolder holder = (CorCardViewHolder) rootView.getTag();
		CorCardModel model = getItem(position);

		if (model != null) {

			holder.tvUSETYPENM.setText(model.getUSETYPENM());
			holder.tvSTATNM.setText(model.getSTATNM());
			holder.tvORGL_PERM_DT.setText(model.getORGL_PERM_DT());
			holder.tvBUY_PTM.setText(model.getBUY_PTM());
			holder.tvPERM_SAM.setText(model.getPERM_SAM());
			holder.tvDOCNAM.setText(model.getDOCNAM());
			holder.tvSGTXT.setText(model.getSGTXT());

			holder.tvBT_NM.setText(model.getBT_NM());
			holder.tvTRE_FRAN.setText(model.getTRE_FRAN());
			holder.tvORGL_PERM_NO.setText(model.getORGL_PERM_NO());
			holder.tvTAXTN_TY.setText(model.getTAXTN_TY());

			holder.tvFRAN_ADDR.setText(model.getFRAN_ADDR());
			holder.tvSKTEXT.setText(model.getSKTEXT());
			holder.tvSENAME.setText(model.getSENAME());
			holder.tvCHG_SALE_AMT.setText(model.getCHG_SALE_AMT());
			holder.tvCHG_STAX.setText(model.getCHG_STAX());
			holder.tvELC_BUY_DT.setText(model.getELC_BUY_DT());
			holder.tvBELNR.setText(model.getBELNR());

			holder.tvCARDKINDNM.setText(model.getCARDKINDNM());
			holder.tvDOCSTATNM.setText(model.getDOCSTATNM());

			if (model.isCheckFlag()) {
				holder.ivCheck.setImageResource(R.drawable.check_on);
//				holder.ivCheck.setTag(R.drawable.check_on, "TRUE");
			} else {
				holder.ivCheck.setImageResource(R.drawable.check_off);
//				holder.ivCheck.setTag(R.drawable.check_on, "FALSE");
			}
			holder.ivCheck.setTag(position);
			holder.llRoot.setTag(position);

			mPosition++;
		}
	}

	private CorCardAdapter.OnClickRowListener mOnClickRowListener;

	public void setOnClickRowListener(OnClickRowListener clickRowListener) {
		mOnClickRowListener = clickRowListener;
	}

	public interface OnClickRowListener {
		void onClickRowListener(int position);
		void onCheckRowListener(int position, boolean check);
	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {
		View rootView = mInflater.inflate(R.layout.corcard_row, viewgroup,
				false);

		CorCardViewHolder holder = new CorCardViewHolder();
		
		// 2014.01.09	ypkim
		// add check box 
		holder.ivCheck = (ImageView) rootView.findViewById(R.id.iv_check);
//		holder.ivCheck.setOnClickListener(new View.OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				ImageView check = (ImageView)v;
//				if(check.getTag(R.drawable.check_on).equals("TRUE")){
//					check.setImageResource(R.drawable.check_off);
//				} else {
//					check.setImageResource(R.drawable.check_on);
//				}
//			}
//		});

		holder.llRoot = (LinearLayout) rootView.findViewById(R.id.ll_root);
//		holder.root = (LinearLayout) rootView.findViewById(R.id.root);
//		
		holder.tvUSETYPENM = (TextView) rootView.findViewById(R.id.tvUSETYPENM);
		holder.tvSTATNM = (TextView) rootView.findViewById(R.id.tvSTATNM);
		holder.tvORGL_PERM_DT = (TextView) rootView.findViewById(R.id.tvORGL_PERM_DT);
		holder.tvBUY_PTM = (TextView) rootView.findViewById(R.id.tvBUY_PTM);
		holder.tvPERM_SAM = (TextView) rootView.findViewById(R.id.tvPERM_SAM);
		holder.tvDOCNAM = (TextView) rootView.findViewById(R.id.tvDOCNAM);
		holder.tvSGTXT = (TextView) rootView.findViewById(R.id.tvSGTXT);
		holder.tvBT_NM = (TextView) rootView.findViewById(R.id.tvBT_NM);
		holder.tvTRE_FRAN = (TextView) rootView.findViewById(R.id.tvTRE_FRAN);
		holder.tvORGL_PERM_NO = (TextView) rootView.findViewById(R.id.tvORGL_PERM_NO);
		holder.tvTAXTN_TY = (TextView) rootView.findViewById(R.id.tvTAXTN_TY);
		holder.tvFRAN_ADDR = (TextView) rootView.findViewById(R.id.tvFRAN_ADDR);
		holder.tvSKTEXT = (TextView) rootView.findViewById(R.id.tvSKTEXT);
		holder.tvSENAME = (TextView) rootView.findViewById(R.id.tvSENAME);
		holder.tvCHG_SALE_AMT = (TextView) rootView.findViewById(R.id.tvCHG_SALE_AMT);
		holder.tvCHG_STAX = (TextView) rootView.findViewById(R.id.tvCHG_STAX);
		holder.tvELC_BUY_DT = (TextView) rootView.findViewById(R.id.tvELC_BUY_DT);
		holder.tvBELNR = (TextView) rootView.findViewById(R.id.tvBELNR);
		holder.tvCARDKINDNM = (TextView) rootView.findViewById(R.id.tvCARDKINDNM);
		holder.tvDOCSTATNM = (TextView) rootView.findViewById(R.id.tvDOCSTATNM);

		holder.ivCheck.setOnClickListener(this);
		holder.llRoot.setOnClickListener(this);
//		holder.root.setOnClickListener(this);

//		holder.tvUSETYPENM.setTag(mPosition);
//		holder.tvSTATNM.setTag(mPosition);
//		holder.tvORGL_PERM_DT.setTag(mPosition);
//		holder.tvBUY_PTM.setTag(mPosition);
//		holder.tvPERM_SAM.setTag(mPosition);
//		holder.tvDOCNAM.setTag(mPosition);
//		holder.tvSGTXT.setTag(mPosition);
//		holder.tvBT_NM.setTag(mPosition);
//		holder.tvTRE_FRAN.setTag(mPosition);
//		holder.tvORGL_PERM_NO.setTag(mPosition);
//		holder.tvTAXTN_TY.setTag(mPosition);
//		holder.tvFRAN_ADDR.setTag(mPosition);
//		holder.tvSKTEXT.setTag(mPosition);
//		holder.tvSENAME.setTag(mPosition);
//		holder.tvCHG_SALE_AMT.setTag(mPosition);
//		holder.tvCHG_STAX.setTag(mPosition);
//		holder.tvELC_BUY_DT.setTag(mPosition);
//		holder.tvBELNR.setTag(mPosition);
//		holder.tvCARDKINDNM.setTag(mPosition);
//		holder.tvDOCSTATNM.setTag(position);

		rootView.setTag(holder);
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.iv_check:
				clickCheck(v);
				break;
			case R.id.ll_root:
				clickRoot(v);
				break;
//			case R.id.root:
//				clickRoot(v);
//				break;
			default:
				break;
		}
	}

	private void clickCheck(View v)
	{
		int position = (Integer) v.getTag();
		boolean isChecked = checkItem(position);
		mOnClickRowListener.onCheckRowListener(position, isChecked);
	}

	public boolean checkItem(int position)
	{
		CorCardModel model = null;

		model = getItem(position);

		if (model != null)
		{
			if (model.isCheckFlag())
			{
				model.setCheckFlag(false);
			}
			else
			{
				// 하나만 체크할수 있도록 처리!!!
				try {
					ArrayList<CorCardModel> arr_model = (ArrayList<CorCardModel>)super.getDatas();
					for(int i=0; i<arr_model.size(); i++) {
						arr_model.get(i).setCheckFlag(false);
					}
				} catch (Exception e){
					e.printStackTrace();
				}
				model.setCheckFlag(true);
			}
			notifyDataSetChanged();
		}
		return model.isCheckFlag();
	}

	@Override
	public CorCardModel getItem(int position)
	{
		CorCardModel model = null;
		// 14.06.15 Jonathan 이쪽 받아오는 부분 들어가서 BaseCommonAdapter로 가게된다.

		model = super.getItem(position);

		return model;
	}

	private void clickRoot(View v)
	{
		if (v != null)
		{
			int position = (Integer) v.getTag();
//			CorCardModel model = getItem(position);
			mOnClickRowListener.onClickRowListener(position);
		}

	}
	private class CorCardViewHolder {
		LinearLayout llRoot;
//		LinearLayout root;

		ImageView ivCheck;
//		LinearLayout llRoot;

		TextView tvUTYPE;
		TextView tvGRAM_SEQ;  // 00000073
		TextView tvDEAL_CDCO; // LT
//		REASON10;
//		REASON20;
		TextView tvBT_NM; // 특급호텔
		TextView tvORGL_PERM_DT; // 사용일자
//		REASON30; //
//		REASON;
//		DEAL_DIV; // T
//		FR_USE_YN; // A
//		FRAN_NO; // 9870113735
		TextView tvBUY_SAM; // 11000.00 사용금액
//		DOCSTATUS; // 02
//		TAXTN_TY_INQ_DT; // 2017/03/01 ?
//		FRAN_DTS_ADDR; //1번지 (주)호텔롯데 경리부수입관리과
//		TRS_DT; // 2017/03/04
//		ELC_WK_DT; // 2017/03/04
		TextView tvAPP_SCD_DT; // 2017/04/23 // 지급희망일자 / 법인카드 상세내역
//		SLIP_SUBM_YN;
		TextView tvCHG_STAX; // 1000.00 // 부가세
//		BUY_PUTP; // 0.00
//		ELC_CUR_CD; // KRW
		TextView tvVKGRP; // Y03 // 사용부서
		TextView tvBELNR; // 35000009242 // 번호
//		REASON40; //
		TextView tvSENAME; // 공원배 // 사용자
//		OWRSKTEXT; //
//		ELC_SEQ; // 0000000000000001752459
//		FRAN_PSNO; // 100070
//		ORGEH; // 00002595
//		BUY_CANC; //
//		RFN_OJ_YN; // Y
//		NTH_PERM_BUY_YN; // A
//		CARDKIND; // S01
//		BUY_APY_XRT; // 0.00000
//		NOM_CNC_DIV; // A
		TextView tvCARD_NO; // 1111111111
		TextView tvSGTXT; // 33소2048 레이 공원배 순회차량 주차비 // 사용목적
		TextView tvSTATNM; // 승인완료
//		STATUS; // 05
		TextView tvUSETYPENM; // 회사사용
//		EAI_DEAL_STAT; //
//		WEBDOCNUM; // AA0B115D-4644-46A7-8030-B728E63D57D7
//		WEBTYPE; // A1
		TextView tvCHG_SALE_AMT; // 10000.00 // 공급가
		TextView tvDOCNAM; // 차량주차통행료(순회차량) //계정유형
		TextView tvTAXTN_TY; // 일반 // 과세유형
//		BUY_DOL_CA; // 0.00
//		ELC_SLIP_NO; //
//		ERROR_YN; // 2
		TextView tvBUY_SAM2; // 11000.00 // 사용금액
		TextView tvFRAN_BRNO; // 1048125980 // 사업자번호
		TextView tvBUDAT; // 2017/03/02 // 사용일자
		TextView tvPERM_SAM; // 11000.00 //사용금액
//		UTYPENM; //
		TextView tvTRE_FRAN; // 호텔롯데 본점 // 거래처
//		FRAN_REPTR; // 송용덕
		TextView tvSKTEXT; // 서울북부MOT // 소유자 부서
//		BT_CD; // 1101
//		ATTATCHNO;
		TextView tvCORP_ID; // 1178183462
//		ZCLOSE; // X
//		DATA_DC; // D
		TextView tvORGL_PERM_NO; // 51959188 // 승인번호
//		GJAHR; // 2017
//		GUBUN;
//		KTEXT;
//		IFYN;
//		FRAN_PNO; // 0000000007SCA
		TextView tvBUY_AMT; // 11000.00
//		ORGL_BUY_COLL_NO;
//		ELC_USE_DIV;
//		ENAME;
//		DSC_SRVC_DIV;
//		CARDNM;
		TextView tvFRAN_ADDR; // 주소 / 서울시 중구 소공동
//		PD_DIV; // A
//		SORGEH; // 00002595
//		USETYPE; // 02
		TextView tvPERNR; // 00070092 /사용자
		TextView tvDOCTYPE; // KD49 // 계정유형 // 상세내역
//		BUY_CLCTN_NO; // 2017030314238001     00100010126   //이렇게 전달옴
//		PT_BUY_CNC_YN; // A
//		CARDOWRTXT;
		TextView tvCARDKINDNM; // 개인형 법인 // 카드구분
//		ECFEE_AMT; // 0.00
		TextView tvBUY_PTM; // 13:02:33
		TextView tvDOCSTATNM; // 실 전표 // 전표상태
		TextView tvELC_BUY_DT; // 2017/03/03 // 전표일자
//		SLIP_DEAL_DC;
//		TextView tvSPERN; // 00070092
//		BUKRS; // 3200
	}

}
