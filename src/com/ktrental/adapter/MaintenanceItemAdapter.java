package com.ktrental.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.fragment.MaintenanceResultFragment;
import com.ktrental.model.MaintenanceItemModel;
import com.ktrental.popup.BaseTextPopup;
import com.ktrental.popup.EventPopup1;
import com.ktrental.popup.InventoryPopup;
import com.ktrental.popup.InventoryPopup.OnDismissListener;
import com.ktrental.ui.PopupWindowTextView;
import com.ktrental.ui.PopupWindowTextView.OnLayoutListener;
import com.ktrental.util.OnEventCancelListener;
import com.ktrental.util.OnEventOkListener;
import com.ktrental.util.kog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * </br>
 * 정비항목입력에 보여질 현재 선택된 그룹에 리스트를 보여주는 Adapter 이다. </br>
 *
 * @author hongsungil
 */
public class MaintenanceItemAdapter extends BaseCommonAdapter<MaintenanceItemModel> implements View.OnClickListener, BaseTextPopup.OnSelectedPopupItem {

	private ArrayList<MaintenanceItemModel> selectedMaintenanceModels = new ArrayList<MaintenanceItemModel>();
	private ArrayList<MaintenanceItemModel> addMaintenanceModels = new ArrayList<MaintenanceItemModel>();

	private HashMap<PopupWindowTextView, Integer> anchorArray = new HashMap<PopupWindowTextView, Integer>();

	private View mPopView;

	private ListView mLv;
	private InventoryPopup inventoryPopup;

	private OnConsumptionItem mOnConsumptionItem;

	// 2017-11-28. hjt 순회점검인지 여부
	private boolean mIsInspect = false;

	private int[] mInventoryLocation = new int[2];

	// hjt. 2018-06-07.
	private BaseTextPopup mOilQtyFilterPopup;

	private final static String OIL_FILTER = "Oil_Filter";

	private LinkedHashMap<String, String> mOilFilterMap = new LinkedHashMap<String, String>();

	private int mOilPosition = -1;

	public interface OnConsumptionItem {
		void onCancelConsumption(String matkl, String matnr);

		void onAddConsumption(String matkl, String matnr, int nqty);

		void onEndNewView();
	}

	public MaintenanceItemAdapter(Context context, View popView, ListView lv, InventoryPopup _inventoryPopup,
								  OnConsumptionItem onConsumptionItem) {
		super(context);
		mPopView = popView;
		mLv = lv;
		inventoryPopup = _inventoryPopup;
		mOnConsumptionItem = onConsumptionItem;
		// TODO Auto-generated constructor stub
	}

	public void setIsIspect(boolean isIspect){
		mIsInspect = isIspect;
	}

	@Override
	protected void bindView(View rootView, Context context, int position) {

		ItemViewHolder itemViewHolder = (ItemViewHolder) rootView.getTag();
		MaintenanceItemModel model = getItem(position);

		if (model != null) {

			if (model.isCheck()) {
				rootView.setBackgroundResource(R.drawable.list02_bg_s);
				itemViewHolder.ivCheck.setImageResource(R.drawable.check_on);

			} else {

				rootView.setBackgroundResource(R.drawable.list02_bg_n);
				itemViewHolder.ivCheck.setImageResource(R.drawable.check_off);

			}

			int consumption = model.getConsumption();
			int stock = model.getStock();
			int selectedConsumption = model.getSelectcConsumption();

			// myung 20131224 UPDATE 리스트의 재고는 해당 아이테믜 선택된 소모 전체를 확인하고 적용.
			// int val = stock - consumption - selectedConsumption;
			int val = stock - MaintenanceResultFragment.getToalConsumption(model.getMATNR());
			//TODO 윤승
//			int price = MaintenanceResultFragment.getToalConsumption()
			itemViewHolder.getStock = val;

			// Jonathan 14.12.16 수정
			// itemViewHolder.tvConsumption.setText("" +
			// model.getConsumption());

			kog.e("Jonathan", " tvConsumption :: " + model.getMTQTY().trim());
			itemViewHolder.tvConsumption.setText("" + model.getMTQTY().trim());

			if (val == 0 && !mIsInspect) {
				itemViewHolder.tvConsumption.setTextColor(Color.parseColor("#fd2727"));
				itemViewHolder.tvName.setTextColor(Color.parseColor("#fd2727"));
				itemViewHolder.tvStock.setTextColor(Color.parseColor("#fd2727"));
				itemViewHolder.tvPrice.setTextColor(Color.parseColor("#fd2727"));
			} else {
				String color = "#333333";
				itemViewHolder.tvConsumption.setTextColor(Color.parseColor(color));
				itemViewHolder.tvName.setTextColor(Color.parseColor(color));
				itemViewHolder.tvStock.setTextColor(Color.parseColor(color));
				itemViewHolder.tvPrice.setTextColor(Color.parseColor(color));
			}

			itemViewHolder.tvStock.setText("" + val);
			//TODO 윤승
//			itemViewHolder.tvPrice.setText(""/);
			itemViewHolder.tvName.setText(model.getName());
		}
		itemViewHolder.ivCheck.setTag(position);
		itemViewHolder.llRoot.setTag(position);
		itemViewHolder.tvStock.setTag(position);
		itemViewHolder.tvPrice.setTag(position);

		if (anchorArray.containsKey(itemViewHolder.tvConsumption)) {
			anchorArray.remove(itemViewHolder.tvConsumption);
			anchorArray.put(itemViewHolder.tvConsumption, position);
		}


	}

	@Override
	protected View newView(Context context, int position, ViewGroup viewgroup) {
		View rootView = mInflater.inflate(R.layout.maintenance_item_row, null);

		ItemViewHolder itemViewHolder = new ItemViewHolder();

		itemViewHolder.llRoot = (LinearLayout) rootView.findViewById(R.id.ll_root);

		// myung 20131120 2560 대응
		// int tempY = 50;
		// if(DEFINE.DISPLAY.equals("2560")){
		// tempY *= 2;
		// }
		//
		//
		// rootView.setLayoutParams(new AbsListView.LayoutParams(
		// LayoutParams.MATCH_PARENT, tempY));

		itemViewHolder.ivCheck = (ImageView) rootView.findViewById(R.id.iv_check);

		itemViewHolder.tvName = (TextView) rootView.findViewById(R.id.tv_item_name);

		itemViewHolder.tvConsumption = (PopupWindowTextView) rootView
				.findViewById(R.id.tv_item_consumption);

		itemViewHolder.tvConsumption.setOnLayoutListener(new OnLayoutListener() {

			@Override
			public void onLayout() {
				// TODO Auto-generated method stub
				mOnConsumptionItem.onEndNewView();
			}
		});

		// kog.e("KDH", "itemViewHolder.getStock = "+itemViewHolder.getStock );
		// if(itemViewHolder.getStock > 0)
		// {
		// itemViewHolder.llRoot.setVisibility(View.VISIBLE);
		// }
		// else
		// {
		// itemViewHolder.llRoot.setVisibility(View.GONE);
		// }

		itemViewHolder.tvStock = (TextView) rootView.findViewById(R.id.tv_item_stock);
		itemViewHolder.tvPrice = (TextView) rootView.findViewById(R.id.tv_item_price);

		itemViewHolder.llRoot.setOnClickListener(this);
		itemViewHolder.ivCheck.setOnClickListener(this);
		itemViewHolder.tvStock.setOnClickListener(this);

		anchorArray.put(itemViewHolder.tvConsumption, position);

		if (anchorArray.containsKey(itemViewHolder.tvConsumption)) {
			anchorArray.remove(itemViewHolder.tvConsumption);
			anchorArray.put(itemViewHolder.tvConsumption, position);
		}
		rootView.setTag(itemViewHolder);

		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.iv_check:
			case R.id.ll_root:
				clickCheck2(v);
				break;

			// clickCheck2(v);
			// break;
			default:
				break;
		}
	}

	private PopupWindowTextView mSelectView = null;

	private void clickCheck2(View v) {
		final int position = (Integer) v.getTag();
		// checkItem(position);

		String light_bulb = mContext.getString(R.string.light_bulb);
		String washer_liquid = mContext.getString(R.string.washer_liquid);
		String brush = mContext.getString(R.string.brush);

		// hjt. 2018.06.07. 가솔린과 디젤 오일일 경우 실 오일 주입량 선택 가능하도록 변경
		String gasolin = mContext.getString(R.string.gasolin);
		String diesel = mContext.getString(R.string.diesel);

		MaintenanceItemModel model = null;
		model = getItem(position);

		if (model != null) {
			int stock = model.getStock();
			if (stock < 1 && !mIsInspect) {
				showEventPopup2(null, "재고수량이 없습니다.");
				return;
			}
			// Jonathan 14.12.16 전구나 와셔액일 경우만, 눌렀을때 숫자 다이얼로그 나옴.
			else if (model.getName().contains(light_bulb) || model.getName().contains(washer_liquid)
					|| model.getName().contains(brush)) {
//				if (inventoryPopup == null)
//					inventoryPopup = new InventoryPopup(mContext, QuickAction.HORIZONTAL, R.layout.inventory_popup,
//							InventoryPopup.TYPE_NORMAL_NUMBER);

				if (anchorArray.containsValue(position)) {
					PopupWindowTextView key = null;
					Iterator<PopupWindowTextView> iterator = anchorArray.keySet().iterator();
					while (iterator.hasNext()) {
						key = (PopupWindowTextView) iterator.next();
						mSelectView = key;
						int pos = anchorArray.get(key);

						if (pos == position)
							break;
					}

					if (key != null) {

						mPopView.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								mOilPosition = -1;
								inventoryPopup.show(mSelectView, position, mPopView.getMeasuredWidth(),
										mPopView.getMeasuredHeight());
								inventoryPopup.setOnDismissListener(new OnDismissListener() {

									@Override
									public void onDismiss(String result, int position) {

										MaintenanceItemModel model = null;

										model = getItem(position);
										if (result.length() > 0) {

											if (model != null) {
												int iResult = Integer.parseInt(result);
												if (iResult == 0) {
													selectedMaintenanceModels.remove(model);
													model.setCheck(false);
													model.setSelectcConsumption(0);
													model.setConsumption(0);

													//2018-11-09. model.getMaintenanceGroupModel() 이 null 인 경우가 저 안에서 생긴다 ㅡㅡ;;;
													// 어차피 Name_key는 사용하지 않으므로 null 넘겨주도록 해본다.
//													if (mOnConsumptionItem != null && model.getMaintenanceGroupModel() != null)
//														mOnConsumptionItem.onCancelConsumption(
//																model.getMaintenanceGroupModel().getName_key(),
//																model.getMATNR());
													try {
														if (mOnConsumptionItem != null && model.getMaintenanceGroupModel() != null)
															mOnConsumptionItem.onCancelConsumption(
																	null,
																	model.getMATNR());
														notifyDataSetChanged();
													} catch (Exception e){
														e.printStackTrace();
													}

													return;
												}

												// Log.d("",
												// ""
												// + model.getConsumption());
												if (isStock(model, result))
													checkItem(result, model);
												else
													showEventPopup2(null, "재고수량보다 적게 입력을 해주세요.");
											}
										}
									}
								});
							}
						});

					}
				}
			}
			else if (model.getName().contains(gasolin) || model.getName().contains(diesel)) {
				int minqty = 1;
				int maxqty = 1;

				if(model.getMINQTY() != null && !model.getMINQTY().trim().equals("") && !model.getMINQTY().trim().equals("1")){
					minqty = Integer.parseInt(model.getMINQTY().trim());
				}
				if(model.getMAXQTY() != null && !model.getMAXQTY().trim().equals("") && !model.getMAXQTY().trim().equals("1")){
					maxqty = Integer.parseInt(model.getMAXQTY().trim());
				}

				if(minqty != maxqty){
					for(int i=minqty; i<=maxqty; i++) {
						mOilFilterMap.put(String.valueOf(i), String.valueOf(i));
					}
					mOilQtyFilterPopup = new BaseTextPopup(mContext, mOilFilterMap, OIL_FILTER);
					if (anchorArray.containsValue(position)) {
						PopupWindowTextView key = null;
						Iterator<PopupWindowTextView> iterator = anchorArray.keySet().iterator();
						while (iterator.hasNext()) {
							key = (PopupWindowTextView) iterator.next();
							mSelectView = key;
							int pos = anchorArray.get(key);

							if (pos == position)
								break;
						}

						if (key != null) {

							mPopView.post(new Runnable() {

								@Override
								public void run() {
									mOilPosition = position;
									showFilterPopup(mOilQtyFilterPopup, mSelectView, false);
								}
							});

						}
					}
				} else {
					mOilPosition = -1;
					checkItem(model.getMTQTY().trim(), model);
				}
			}
			// Jonathan 14.12.16 전구나 와셔액 빼고는 나머지 모두 리스트 눌렀을때 바로 내려가게한다.
			else {
				mOilPosition = -1;
				checkItem(model.getMTQTY().trim(), model);
			}
		}
	}

	private boolean isStock(MaintenanceItemModel model, String result) {

		Boolean isStock = false;

		int consumption = Integer.parseInt(result);
		int stock = model.getStock();
		int selectedConsumption = model.getSelectcConsumption();

		int val = stock - consumption - selectedConsumption;

		if (val > -1)
			isStock = true;

		return isStock;

	}

	private void checkItem(String result, final MaintenanceItemModel model) {

		if (model != null) {
			kog.e("KDH", "1111수량 = " + model.getMTQTY());
			kog.e("KDH", "1111체크 = " + model.isCheck());
			kog.e("KDH", "1111모델 = " + model.toString());
			kog.e("KDH", "1111모델 = " + model.getMTQTY());
			kog.e("KDH", "1111모델 = " + model.getName());
			kog.e("KDH", "1111모델 = " + result);

			if (model.isCheck()) {
				if (selectedMaintenanceModels.remove(model))
					model.setCheck(false);
			} else {

				String washer_liquid = mContext.getString(R.string.washer_liquid);

				if(mIsInspect){
					result = "1";
				}
				final int consumption = Integer.parseInt(result);

				if (model.getName().contains(washer_liquid)) {
					if (consumption > 2) {
						showEventPopup2(null, "와셔액 최대 입력 수량은 2개 입니다.");
						return;
					}
				}

				if (consumption > 9) {
					showEventPopup1("", "입력 수량이 많습니다. 입력 하시겠습니까?", "예", new OnEventOkListener() {

						@Override
						public void onOk() {
							// TODO Auto-generated method stub
							model.setConsumption(consumption);
							selectedMaintenanceModels.add(model);
							model.setCheck(true);
							mOnConsumptionItem.onAddConsumption(null,
									model.getMATNR(), consumption);
//							mOnConsumptionItem.onAddConsumption(model.getMaintenanceGroupModel().getName_key(),
//									model.getMATNR(), consumption);
							notifyDataSetChanged();
						}
					}, new OnEventCancelListener() {

						@Override
						public void onCancel() {
							// TODO Auto-generated method stub

						}
					});

				} else {

					model.setConsumption(consumption);
					selectedMaintenanceModels.add(model);
					model.setCheck(true);
					mOnConsumptionItem.onAddConsumption(null,
							model.getMATNR(), consumption);
//					mOnConsumptionItem.onAddConsumption(model.getMaintenanceGroupModel().getName_key(),
//							model.getMATNR(), consumption);
					notifyDataSetChanged();
				}
			}
		}
	}

	public ArrayList<MaintenanceItemModel> getSelectedMaintenanceModels() {
		return selectedMaintenanceModels;
	}

	public void setSelectedMaintenanceModels(ArrayList<MaintenanceItemModel> selectedMaintenanceModels) {
		this.selectedMaintenanceModels = selectedMaintenanceModels;
	}

	public void initSelectedMaintenanceArray() {
		for (int i = 0; i < selectedMaintenanceModels.size(); i++) {
			selectedMaintenanceModels.get(i).setCheck(false);
			selectedMaintenanceModels.get(i).setSelectcConsumption(selectedMaintenanceModels.get(i).getConsumption()
					+ selectedMaintenanceModels.get(i).getSelectcConsumption());
			selectedMaintenanceModels.get(i).setConsumption(0);
			addMaintenanceModels.add(selectedMaintenanceModels.get(i));
		}

		selectedMaintenanceModels = new ArrayList<MaintenanceItemModel>();
		notifyDataSetChanged();
	}

	public void initSelectedMaintenanceItem(ArrayList<MaintenanceItemModel> models) {
		for (int i = 0; i < models.size(); i++) {

			for (MaintenanceItemModel maintenanceItemModel : mItemArray) {

				if (maintenanceItemModel.getName().equals(models.get(i).getName())) {

					maintenanceItemModel.setConsumption(0);
					maintenanceItemModel.setSelectcConsumption(0);
					maintenanceItemModel.setCheck(false);
					for (MaintenanceItemModel selectedModel : selectedMaintenanceModels) {
						if (selectedModel.getName().equals(models.get(i).getName())) {
							selectedModel.setConsumption(0);
							selectedModel.setSelectcConsumption(0);

							selectedMaintenanceModels.remove(selectedModel);
							break;
						}
					}

				}

			}

		}

		notifyDataSetChanged();
	}

	public class ItemViewHolder {
		public LinearLayout llRoot;
		public ImageView ivCheck;
		public PopupWindowTextView tvConsumption;
		public TextView tvName;
		public TextView tvStock;
		public TextView tvPrice;
		int getStock;

	}

	@Override
	protected void releaseResouces() {
		anchorArray.clear();
		super.releaseResouces();
	}

	public void showEventPopup1(String title, String body, String okText, OnEventOkListener onEventPopupListener,
								OnEventCancelListener onEventCancelListener) {
		EventPopup1 popup = new EventPopup1(mContext, body, onEventPopupListener);
		popup.setOnCancelListener(onEventCancelListener);
		popup.show();
		popup.setOkButtonText(okText);
	}

	private void showFilterPopup(BaseTextPopup popup, View anchor, boolean flag) {

		popup.show(anchor);
		popup.setOnSelectedItem(this);
	}

	@Override
	public void onSelectedItem(int position, String popName) {
		MaintenanceItemModel model = null;
		if(mOilPosition == -1){
			return;
		}
		if(mOilFilterMap == null){
			return;
		}
		model = getItem(mOilPosition);
		String result = "";

		result = getSeletedItem(mOilFilterMap, position, mSelectView);
		if (model != null) {
			int iResult = Integer.parseInt(result);
			if (iResult == 0) {
				selectedMaintenanceModels.remove(model);
				model.setCheck(false);
				model.setSelectcConsumption(0);
				model.setConsumption(0);

				//2018-11-09. model.getMaintenanceGroupModel() 이 null 인 경우가 저 안에서 생긴다 ㅡㅡ;;;
				// 어차피 Name_key는 사용하지 않으므로 null 넘겨주도록 해본다.
//													if (mOnConsumptionItem != null && model.getMaintenanceGroupModel() != null)
//														mOnConsumptionItem.onCancelConsumption(
//																model.getMaintenanceGroupModel().getName_key(),
//																model.getMATNR());
				if (mOnConsumptionItem != null && model.getMaintenanceGroupModel() != null)
					mOnConsumptionItem.onCancelConsumption(
							null,
							model.getMATNR());
				notifyDataSetChanged();
				return;
			}

			// Log.d("",
			// ""
			// + model.getConsumption());
			if (isStock(model, result)) {
				checkItem(result, model);
			}
			else {
				showEventPopup2(null, "재고수량보다 적게 입력을 해주세요.");
			}
		}
	}

	private String getSeletedItem(LinkedHashMap<String, String> linkedHashMap, int position, PopupWindowTextView tv) {

		String value = null;
		Iterator<String> it = linkedHashMap.keySet().iterator();

		int i = 0;

		while (it.hasNext()) {
			String strKey = "";
			String strValue = "";

			strKey = it.next();
			strValue = linkedHashMap.get(strKey);

			if (i == position) {
				value = strValue;
//				if (tv != null)
//					tv.setText(strKey);
				break;
			}

			i++;
		}

		return value;
	}
}
