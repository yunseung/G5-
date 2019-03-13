package com.ktrental.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ktrental.R;
import com.ktrental.adapter.MenuAdapter;
import com.ktrental.ui.ContentLayout;
import com.ktrental.ui.ContentLayout.OnContentSlide;
import com.ktrental.ui.CustomSlidingLayout;
import com.ktrental.util.FragmentController;
import com.ktrental.util.OnChangeFragmentListener;

import java.util.ArrayList;

public class SlidingFragment extends BaseFragment implements
		OnItemClickListener, OnChangeFragmentListener, OnContentSlide {

	private ListView mMenuListView;

	private ArrayList<String> mMenuArr = new ArrayList<String>();

	private CustomSlidingLayout mSlidingPaneLayout;

	private HomeFragment mHomeFragment;
	private MaintenanceStatusFragment mMaintenanceStatusFragment;

	private InventoryControlFragment mInventoryControlFragment;

	// private Fragment mCurrentFragment;

	private FragmentController mFragmentController;

	private MenuAdapter mMenuAdapter;

	public SlidingFragment(){}

	public SlidingFragment(String className,
			OnChangeFragmentListener changeFragmentListener) {
		super(className, changeFragmentListener);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mMaintenanceStatusFragment = new MaintenanceStatusFragment(
				MaintenanceStatusFragment.class.getName(), this);

		mHomeFragment = new HomeFragment(HomeFragment.class.getName(), this);

		mInventoryControlFragment = new InventoryControlFragment(
				HomeFragment.class.getName(), this);

		FragmentManager fragmentManager = getChildFragmentManager();

		mFragmentController = new FragmentController(mHomeFragment,
				fragmentManager, FragmentController.TYPE_HOME, R.id.ll_content);

		FragmentTransaction ft = fragmentManager.beginTransaction();

		addFragment(ft, mHomeFragment, FragmentController.TYPE_HOME, "홈화면");
		addFragment(ft, mMaintenanceStatusFragment,
				FragmentController.TYPE_HOME, "정비현황");

		addFragment(ft, mInventoryControlFragment,
				FragmentController.TYPE_STOCK, "재고");

		ft.commit();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = (View) inflater.inflate(R.layout.sliding_layout, null);

		mHomeFragment.setClassName(HomeFragment.class.getName());
		//

		mSlidingPaneLayout = (CustomSlidingLayout) view
				.findViewById(R.id.fl_content);

		mMenuListView = (ListView) view.findViewById(R.id.lv_menu);

		mMenuAdapter = new MenuAdapter(getActivity());
		mMenuAdapter.setData(mMenuArr);

		mMenuListView.setAdapter(mMenuAdapter);
		mMenuListView.setOnItemClickListener(this);

		mMenuListView.setDivider(null);

		initFragment();

		ContentLayout content = (ContentLayout) view
				.findViewById(R.id.ll_content);

		mSlidingPaneLayout.setContentLayout(content);
		content.setOnContentSlide(this);

		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		if (!mSlidingPaneLayout.isSlideOpen()) {
			mFragmentController.changeFragment(arg2);
			mSlidingPaneLayout.onSlide();
		}

	}

	@Override
	public void onChageFragment(String oldClass, Fragment fragment) {

		mFragmentController.showFragment(fragment);

		// mSlidingPaneLayout.onSlide();
		mSlidingPaneLayout.smoothSlideClosed();
	}

	public void onSlide() {
		mSlidingPaneLayout.onSlide();
	}

	public void onHome(int type) {

		initMenuList(type);

	}

	public void onStock(int type) {
		initMenuList(type);
		mFragmentController.allChildRemoveFragment();

	}

	private void addFragment(FragmentTransaction ft, Fragment fragment,
			int type, String showMenuName) {

		mFragmentController.addFragment(fragment, type, showMenuName);

		ft.add(R.id.ll_content, fragment);
		ft.hide(fragment);

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

		// mFragmentController.changeFragment(type, 0);
		mSlidingPaneLayout.smoothSlideOpen();
	}

	@Override
	public void onContentSlide() {
		// TODO Auto-generated method stub
		mSlidingPaneLayout.smoothSlideClosed();
	}
}
