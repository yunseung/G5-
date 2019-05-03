package com.ktrental.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ktrental.R;
import com.ktrental.activity.Main_Activity;
import com.ktrental.adapter.MenuAdapter;
import com.ktrental.common.DEFINE;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.dialog.PartsTransfer_Cars_Dialog;
import com.ktrental.model.LoginModel;
import com.ktrental.ui.ContentLayout;
import com.ktrental.ui.ContentLayout.OnContentSlide;
import com.ktrental.ui.DrawerLayout;
import com.ktrental.util.CommonUtil;
import com.ktrental.util.FragmentController;
import com.ktrental.util.LogUtil;
import com.ktrental.util.OnChangeFragmentListener;
import com.ktrental.util.kog;

import java.util.ArrayList;

/**
 * 메인 fragment 이며 메뉴등을 관리한다. </br>
 * DrawerLayout와 화면을 관리하고 </br>
 * FragmentController를 이용하여 fragment들을 관리한다.
 * 
 * 
 * @author hongsungil
 */
public class DrawerFragment extends Fragment implements OnItemClickListener, OnChangeFragmentListener, OnContentSlide {


	/**
	 * 메뉴에 들어가는 Fragment 들은 신규 Fragment가 생긴다면 컨트롤러에 등록을 해주어야 한다.
	 */
	private FragmentController mFragmentController;
	private HomeFragment mHomeFragment;
	private MaintenanceStatusFragment mMaintenanceStatusFragment;
	private InventoryControlFragment mInventoryControlFragment;
	private MaintenancePartsFragment mMaintenancePartsFragment;
	private PartsTransferFragment mPartsTransferFragment;
	// private TireFragment mTireFragment;
	private CustomerSearchFragment mCustomerSearchFragment;
	private SetupFragment mSetupFragment;
	private ResultSendFragment mResultSendFragment;
	private KDH_Donan mKdh_Donan;

	private MonthProgressFragment mMonthProgressFragment;
	private NoticeFragment mNoticeFragment;
	private MaintenanceCheckListFragment mMaintenanceCheckListFragment;
	private MovementFragment mMovementFragment;
	// 2018.04.04. hjt 법인카드 화면 추가
	private CorCardRegFragment mCorCardRegFragment;
	private MaintenancePerpomanceFragment mMaintenancePerpomanceFragment;
	private PartsTransferCarsDialogFragment mPartsTransferCarsDialogFragment;

	private DrawerLayout mDrawerLayout;
	private LinearLayout mMenuView;
	private ListView mMenuListView;

	private MenuAdapter mMenuAdapter;
	private ArrayList<String> mMenuArr = new ArrayList<String>();

	private Context mContext;

	public DrawerFragment(){
	}

	// TextView testView;
	// View mViewPartsTransferCarsDialog;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		mContext = activity;
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mMaintenanceStatusFragment = new MaintenanceStatusFragment(MaintenanceStatusFragment.class.getName(), this);

		mHomeFragment = new HomeFragment(HomeFragment.class.getName(), this);
		mHomeFragment.setOnChangeFragmentListener(this);

		mInventoryControlFragment = new InventoryControlFragment(InventoryControlFragment.class.getName(), this);

		mMaintenancePartsFragment = new MaintenancePartsFragment(MaintenancePartsFragment.class.getName(), this);

		mPartsTransferFragment = new PartsTransferFragment(PartsTransferFragment.class.getName(), this);

		// mTireFragment = new TireFragment(TireFragment.class.getName(), this,
		// "", "");

		mCustomerSearchFragment = new CustomerSearchFragment(CustomerSearchFragment.class.getName(), this);
		mSetupFragment = new SetupFragment(SetupFragment.class.getName(), this);

		mResultSendFragment = new ResultSendFragment();
		mKdh_Donan = new KDH_Donan();

		mNoticeFragment = new NoticeFragment(NoticeFragment.class.getName(), this);
		mMonthProgressFragment = new MonthProgressFragment(MonthProgressFragment.class.getName(), this, false);

		
		mMaintenanceCheckListFragment = new MaintenanceCheckListFragment(MaintenanceCheckListFragment.class.getName(), this);
		

		
		LoginModel model = KtRentalApplication.getLoginModel();
		mMovementFragment = new MovementFragment(model.getEqunr(), model.getPernr(), " ", model.getInvnr(),
				model.getFUELNM(), model.getEname(), false);

		mCorCardRegFragment = new CorCardRegFragment(model.getEqunr(), model.getPernr(), " ", model.getInvnr(), model.getFUELNM(), model.getEname(), false);

		mMaintenancePerpomanceFragment = new MaintenancePerpomanceFragment(
				MaintenancePerpomanceFragment.class.getName(), this, CommonUtil.getCurrentMonth());

		FragmentManager fragmentManager = getChildFragmentManager();

		mFragmentController = new FragmentController(mHomeFragment, fragmentManager, FragmentController.TYPE_HOME, R.id.ll_content);

		mPartsTransferCarsDialogFragment = new PartsTransferCarsDialogFragment();

		FragmentTransaction ft = fragmentManager.beginTransaction();

		addFragment(ft, mHomeFragment, FragmentController.TYPE_HOME, "홈화면");
		addFragment(ft, mMaintenanceStatusFragment, FragmentController.TYPE_MAINTENANCE, "일간정비현황");
		addFragment(ft, mInventoryControlFragment, FragmentController.TYPE_STOCK, "정비부품 신청관리");
		addFragment(ft, mMaintenancePartsFragment, FragmentController.TYPE_STOCK, "정비부품 입고관리");
		addFragment(ft, mPartsTransferFragment, FragmentController.TYPE_STOCK, "정비부품 이관관리");
		addFragment(ft, mCustomerSearchFragment, FragmentController.TYPE_SEARCH, "고객조회");
		addFragment(ft, mMaintenancePerpomanceFragment, FragmentController.TYPE_SEARCH, "정비현황조회");
		addFragment(ft, mSetupFragment, FragmentController.TYPE_SETTING, "환경설정");

		addFragment(ft, mMonthProgressFragment, FragmentController.TYPE_MAINTENANCE, "월간정비현황");
		addFragment(ft, mNoticeFragment, FragmentController.TYPE_ETC, "공지사항");
		addFragment(ft, mMovementFragment, FragmentController.TYPE_ETC, "차량운행일지");

		addFragment(ft, mMaintenanceCheckListFragment, FragmentController.TYPE_MAINTENANCE, "순회정비차량점검표");
		

		// Jonathan 15.02.11 미전송 내역 안한다고 해서 주석처리 한다.
		// addFragment(ft, mResultSendFragment, FragmentController.TYPE_ETC,
		// "미전송 내역 관리");
		addFragment(ft, mKdh_Donan, FragmentController.TYPE_ETC, getString(R.string.title_donan));

		//2018.04.04. hjt 법인카드 메뉴 추가
		addFragment(ft, mCorCardRegFragment, FragmentController.TYPE_ETC, "법인카드 등록");

		// addFragment(ft, mPartsTransferCarsDialogFragment,
		// FragmentController.TYPE_ETC,
		// "순회차량정비 조회")
		addFragmentDummy(mPartsTransferCarsDialogFragment, FragmentController.TYPE_SEARCH, "책임정비기사 조회");
		
		

		ft.commit();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = (View) inflater.inflate(R.layout.drawer_layout, null);

		mHomeFragment.setClassName(HomeFragment.class.getName());
		//

		mDrawerLayout = (DrawerLayout) view.findViewById(R.id.dl_handle);

		mMenuListView = (ListView) view.findViewById(R.id.lv_menu);

		mMenuAdapter = new MenuAdapter(getActivity());
		mMenuAdapter.setData(mMenuArr);

		mMenuListView.setAdapter(mMenuAdapter);
		mMenuListView.setOnItemClickListener(new DrawerItemClickListener());

		mMenuListView.setDivider(null);

		initFragment();

		ContentLayout content = (ContentLayout) view.findViewById(R.id.ll_content);

		content.setOnContentSlide(this);

		mMenuView = (LinearLayout) view.findViewById(R.id.ll_menu);

		// mViewPartsTransferCarsDialog = inflater.inflate(R.layout.menu_item,
		// null);
		// mViewPartsTransferCarsDialog.setBackgroundResource(R.drawable.menu_selector);
		//
		// TextView txtPartsTransferCarsDialog = (TextView)
		// mViewPartsTransferCarsDialog.findViewById(R.id.tv_menu);
		// txtPartsTransferCarsDialog.setText("순회차량정비 조회");
		//
		// mViewPartsTransferCarsDialog.setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// showCarSearchDialog();
		// }
		// });
		//
		//
		// mMenuView.addView(mViewPartsTransferCarsDialog);

		// 좌측 메인메뉴 1depth 투영클릭 불가
		mMenuView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		return view;
	}

	private void showCarSearchDialog() {
		final PartsTransfer_Cars_Dialog ptcd = new PartsTransfer_Cars_Dialog(mContext);
		Button bt_done_car = (Button) ptcd.findViewById(R.id.partstransfer_car_search_done_id);
		ptcd.setTitle("책임정비기사 조회");
		ptcd.setDone("확인");

		bt_done_car.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ptcd.dismiss();
			}
		});

		ptcd.show();
	}

	/**
	 * This list item click listener implements very simple view switching by
	 * changing the primary content text. The drawer is closed when a selection
	 * is made.
	 */
	private class DrawerItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (mFragmentController.getType() == FragmentController.TYPE_ETC) {
				kog.e("KDH", "position = " + position);
				/** 2018.04.04. hjt 미전송내역은 사용하지 않기 때문에 뺌
				 * if (position == 3) { // 미전송관리내역
					ResultSendFragment fragment = new ResultSendFragment();
					// myung 20131120 UPDATE 2560대응
					int tempX = 1060;
					int tempY = 614;
					if (DEFINE.DISPLAY.equals("2560")) {
						tempX *= 2;
						tempY *= 2;
					}
					fragment.show(getChildFragmentManager(), "", tempX, tempY);
					mDrawerLayout.closeDrawer(mMenuView);
					// mFragmentController.allChildRemoveFragment();
					return;

				} else*/
				if (position == 2) { // Jonathan 150324 미귀/도난차량이 다른곳을
											// 바라보고 있어서 position을 2로 하고 미전송관리내역을
											// position 3으로 한다.
					KDH_Donan fragment = new KDH_Donan();
					// myung 20131120 UPDATE 2560대응
					// 670 / 600
					int tempX = 680;
					int tempY = 640;
					if (DEFINE.DISPLAY.equals("2560")) {
						tempX *= 2;
						tempY *= 2;
					}
					fragment.show(getChildFragmentManager(), "");// , tempX,
																	// tempY);
					mDrawerLayout.closeDrawer(mMenuView);
					return;
				}

			} else if (mFragmentController.getType() == FragmentController.TYPE_SEARCH) {
				if (position == 2) {
					showCarSearchDialog();
					// mDrawerLayout.closeDrawer(mMenuView);
				}
			}
			mFragmentController.changeFragment(position);
			mDrawerLayout.closeDrawer(mMenuView);
			mFragmentController.allChildRemoveFragment();
		}
	}

	public void onSlide() {
	}

	public void onHome(int type) {
		mDrawerLayout.closeDrawer(mMenuView);
		mFragmentController.allChildRemoveFragment();
		mFragmentController.setType(FragmentController.TYPE_HOME);
		setMenuArray();
		mFragmentController.changeFragment(0);
	}

	public void onMaintence(int type) {
		initMenuList(type);
	}

	public void onStock(int type) {
		initMenuList(type);
	}

	public void onEtc(int type) {
		initMenuList(type);
	}

	public void onSearch(int type) {
		initMenuList(type);
		// MonthProgressFragment dialogFragment = new MonthProgressFragment(
		// MonthProgressFragment.class.getName(), null);
		// dialogFragment.show(getChildFragmentManager(), null, 1230, 752);

	}

	public void onSetting(int type) {
		initMenuList(type);
	}

	private void addFragment(FragmentTransaction ft, Fragment fragment, int type, String showMenuName) {

		mFragmentController.addFragment(fragment, type, showMenuName);

		ft.add(R.id.ll_content, fragment);
		ft.hide(fragment);

	}

	private void addFragmentDummy(Fragment fragment, int type, String showMenuName) {

		mFragmentController.addFragment(fragment, type, showMenuName);

	}

	private void initFragment() {

		setMenuArray();
		mFragmentController.initFragmentController();// 꼭 oncreateView 에서
														// 호출해주어야한다.
	}

	private void setMenuArray() {
		mMenuArr = mFragmentController.getMenuNames();
		if (mMenuAdapter != null) {
			if (mMenuArr != null) {
				mMenuAdapter.chageAllData(mMenuArr);

			}
		}

	}

	private void initMenuList(int type) {

		mFragmentController.setType(type);
		setMenuArray();

		// if(type!=FragmentController.TYPE_ETC)
		// mViewPartsTransferCarsDialog.setVisibility(View.GONE);
		// else
		// mViewPartsTransferCarsDialog.setVisibility(View.VISIBLE);

		mDrawerLayout.openDrawer(mMenuView);

	}

	@Override
	public void onContentSlide() {
		mDrawerLayout.closeDrawer(mMenuView);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (mDrawerLayout.isShown()) {
			if (mFragmentController.getType() == FragmentController.TYPE_ETC) {
				kog.e("KDH", "arg2 = " + arg2);
				if (arg2 == 2) {
					onChageFragment("", new ResultSendFragment());
					return;
				} else if (arg2 == 3) {
					onChageFragment("", new KDH_Donan());
					return;
				}
			}
			mFragmentController.changeFragment(arg2);
			mDrawerLayout.closeDrawer(mMenuView);
			mFragmentController.allChildRemoveFragment();
		}

	}

	@Override
	public void onChageFragment(String oldClass, Fragment fragment) {
		mFragmentController.showFragment(fragment);
	}

	public void onBackPressed() {
		if (mFragmentController.isChildFlag()) {
			mFragmentController.removeBackChildFragment();
		} else {
			if (mFragmentController.getType() != FragmentController.TYPE_HOME) {
				mFragmentController.setType(FragmentController.TYPE_HOME);
				setMenuArray();
				mFragmentController.changeFragment(0);
				mFragmentController.allChildRemoveFragment();
			} else {
				Main_Activity activity = (Main_Activity) mContext;
				activity.onFinish(null);
			}
		}
	}

	@Override
	public void onDestroy() {

		CommonUtil.unbindDrawables(getView());
		System.gc();
		super.onDestroy();
	}

	public void onMoveMaintenave(String moveDay, String progressType, String showText) {
		mMaintenanceStatusFragment.setCurrentDay(moveDay, progressType, showText);
		mFragmentController.setType(FragmentController.TYPE_MAINTENANCE); // 정비
		setMenuArray();
		mFragmentController.changeFragment(0);
		mFragmentController.allChildRemoveFragment();
	}

	public void onMoveNotice() {
		// mMaintenanceStatusFragment.setCurrentDay(moveDay, progressType,
		// showText);
		mFragmentController.setType(FragmentController.TYPE_ETC); // 기타 -> 공지사항
		setMenuArray();
		mFragmentController.changeFragment(0);
		mFragmentController.allChildRemoveFragment();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mFragmentController.getCurrentFragment().onActivityResult(requestCode, resultCode, data);
		LogUtil.d("TAG", "request code = " + requestCode);
		LogUtil.d("TAG", "request code = " + requestCode);
	}

}
