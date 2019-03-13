package com.ktrental.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * fragment 컨트롤러이다.
 * 
 * 
 * @author hongsungil
 * 
 */
public class FragmentController {
	/**
	 * 홈 화면 상수 <br/>
	 * 값 : 0
	 */
	public static final int TYPE_HOME = 0;

	/**
	 * 정비현황 화면 상수 <br/>
	 * 값 : 1
	 */
	public static final int TYPE_MAINTENANCE = 1;
	/**
	 * 재고현황 화면 상수 <br/>
	 * 값 : 2
	 */
	public static final int TYPE_STOCK = 2;
	/**
	 * 진행상태 화면 상수 <br/>
	 * 값 : 3
	 */
	public static final int TYPE_SEARCH = 4;
	public static final int TYPE_ETC = 5;

	public static final int TYPE_MONTHPROGRESS = 2;

	public static final int TYPE_SETTING = 24121;

	private Fragment mCurrentFragment;

	private FragmentManager mFragmentManager;

	private HashMap<Integer, FragmentModel> mFragmentMap = new HashMap<Integer, FragmentModel>();

	private int mType = 0;
	private int mPosition = 0;

	private List<WeakReference<Fragment>> mChildFragmentArr = new ArrayList<WeakReference<Fragment>>();

	private int mLayoutId;

	private boolean childFlag = false;

	/**
	 * 생성자 <br/>
	 * 만약 신규 fragment가 생길시에 type을 FragmentController 클래스안에 상수를 추가 해주어야한다.
	 * 
	 * @param mainFragment
	 *            최초에 보여지는 fragment
	 * @param fragmentManager
	 *            관리가 필요한 FragmentManager 객체
	 */
	public FragmentController(Fragment mainFragment,
			FragmentManager fragmentManager, int type, int layoutId) {

		mCurrentFragment = mainFragment;
		mFragmentManager = fragmentManager;

		mType = type;
		mLayoutId = layoutId;
	}

	/**
	 * 초기화 <br/>
	 * 꼭 초기화 함수를 onCreateView 에서 호출을 해주어야 한다.
	 * 
	 */
	public void initFragmentController() {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.show(mCurrentFragment);
		ft.commit();
	}
	

	/**
	 * 관리할 fragment를 추가 해준다. <br/>
	 * 만약 신규 fragment가 생길시에 type을 FragmentController 클래스안에 상수를 추가 해주어야한다.
	 * 
	 * @param fragment
	 *            추가 될 fragment
	 * @param type
	 *            추가 될 fragment type 상수 값이다.
	 */
	public void addFragment(Fragment fragment, int type, String menuName) {
		boolean valid = mFragmentMap.containsKey(type);

		if (!valid) {
			ArrayList<Fragment> fragments = new ArrayList<Fragment>();
			fragments.add(fragment);

			ArrayList<String> nameArrayList = new ArrayList<String>();
			nameArrayList.add(menuName);

			FragmentModel model = new FragmentModel(nameArrayList, fragments);

			mFragmentMap.put(type, model);
		} else {
			FragmentModel model = mFragmentMap.get(type);
			if (model != null) {
				model.fragmentArrayList.add(fragment);
				model.nameArrayList.add(menuName);
			}
			// // ArrayList<Fragment> fragments = mFragmentMap.get(type);
			// fragments.add(fragment);
		}
	}

	/**
	 * 
	 * @param type
	 *            추가될 fragment에 type을 넣어준다.
	 */
	public void changeFragment(int type, int position) {
		changeFragment(getFragment(type, position));
		mType = type;

	}

	public void changeFragment(int position) {
		mPosition = position;
		changeFragment(getFragment(mType, position));
	}

	private void changeFragment(Fragment fragment) {
		if (fragment != null) {
			FragmentTransaction ft = mFragmentManager.beginTransaction();

			if (fragment.isHidden()) {

				ft.setCustomAnimations(android.R.anim.fade_in,
						android.R.anim.fade_out);
				ft.hide(mCurrentFragment);
				if(mType == 5 && mPosition == 3){
					ft.detach(fragment);
					ft.attach(fragment);
				} else {
					ft.show(fragment);
				}
				mCurrentFragment = fragment;
				ft.commitAllowingStateLoss();
			}
		}
	}

	private Fragment getFragment(int type, int position) {
		Fragment reFragment = null;
		boolean valid = mFragmentMap.containsKey(type);

		if (valid) {
			FragmentModel model = mFragmentMap.get(type);

			if (model != null) {
				reFragment = model.fragmentArrayList.get(position);
			}
		}

		return reFragment;
	}

	public void allChildRemoveFragment() {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		for (WeakReference<Fragment> ref : mChildFragmentArr) {
			Fragment refragment = ref.get();
			if (refragment != null) {
				ft.remove(refragment);
				refragment = null;
			}
		}
		ft.show(mCurrentFragment);
		ft.commitAllowingStateLoss();

		mChildFragmentArr.clear();

		childFlag = false;

	}

	public void removeBackChildFragment() {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		for (int i = mChildFragmentArr.size() - 1; i > -1; i--) {
			WeakReference<Fragment> ref = mChildFragmentArr.get(i);
			Fragment refragment = ref.get();
			if (refragment != null) {

				if (refragment.isVisible()) {
					ft.remove(refragment);
					refragment = null;
					mChildFragmentArr.remove(i);

				} else {
					ft.show(refragment);
					break;
				}

			}
		}
		if (mChildFragmentArr.isEmpty()) {
			ft.show(mCurrentFragment);
			childFlag = false;
		}
		ft.commit();

		//

	}

	/**
	 * 현재 fragment를 보여주는 작업을 한다.
	 */
	public void showFragment(Fragment fragment) {

		FragmentTransaction ft = mFragmentManager.beginTransaction();

		if (fragment.isAdded()) // 현재 fragment가 등록이 되어있는지 판단.
		{
			ft.show(fragment);
			for (WeakReference<Fragment> ref : mChildFragmentArr) {
				Fragment refragment = ref.get();
				if (refragment != null) {
					// ft.remove(refragment);
					// refragment = null;
					if (refragment.isAdded()) {
						ft.hide(refragment);
					}
				}
			}

		} else {

			ft.add(mLayoutId, fragment);

			for (WeakReference<Fragment> ref : mChildFragmentArr) {
				Fragment refragment = ref.get();
				if (refragment != null) {
					// ft.remove(refragment);
					//
					// mChildFragmentArr.remove(ref);
					// ref = null;
					//
					// refragment = null;
					if (refragment.isAdded()) {
						ft.hide(refragment);
					}
				}
			}
			ft.commit();
			mChildFragmentArr.add(new WeakReference<Fragment>(fragment));
		}
		ft = mFragmentManager.beginTransaction();
		ft.hide(mCurrentFragment);
		ft.commit();

		// mCurrentFragment = fragment;

		childFlag = true;
	}

	/**
	 * 현재 fragment를 보여주는 작업을 한다.
	 */
	public void showFragment(Fragment fragment, int layoutId) {

		FragmentTransaction ft = mFragmentManager.beginTransaction();

		// ft.setCustomAnimations(android.R.anim.fade_in,
		// android.R.anim.fade_out);

		if (fragment.isAdded()) // 현재 fragment가 등록이 되어있는지 판단.
		{
			ft.show(fragment);
			for (WeakReference<Fragment> ref : mChildFragmentArr) {
				Fragment refragment = ref.get();
				if (refragment != null) {
					ft.remove(refragment);
					refragment = null;
				}
			}

		} else {

			ft.add(layoutId, fragment);

			for (WeakReference<Fragment> ref : mChildFragmentArr) {
				Fragment refragment = ref.get();
				if (refragment != null) {
					ft.remove(refragment);

					mChildFragmentArr.remove(ref);
					ref = null;

					refragment = null;

				}
			}
			ft.commit();
			mChildFragmentArr.add(new WeakReference<Fragment>(fragment));
		}
		ft = mFragmentManager.beginTransaction();
		// ft.hide(mCurrentFragment);
		ft.commit();

		// mCurrentFragment = fragment;

		childFlag = true;
	}

	/**
	 * 현재 fragment를 숨기는 작업을 한다.
	 */
	public void hideCurrentFragment(Fragment fragment) {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.hide(fragment);
		// ft.remove(fragment);
		ft.commit();
	}

	public void showFragment() {

	}

	// private Fragment recreateFragment(Fragment f) {
	// try {
	// Fragment.SavedState savedState = mFragmentManager
	// .saveFragmentInstanceState(f);
	//
	// Fragment newInstance = f.getClass().newInstance();
	// newInstance.setInitialSavedState(savedState);
	//
	// return newInstance;
	// } catch (Exception e) // InstantiationException, IllegalAccessException
	// {
	// throw new RuntimeException("Cannot reinstantiate fragment "
	// + f.getClass().getName(), e);
	// }
	// }

	/**
	 * 현재 fragment에 최상위 뷰를 리턴해 준다.
	 * 
	 * @return View rootView
	 */
	public View getCurrentFragmentRootView() {

		View reRootView = null;

		if (mCurrentFragment != null)
			reRootView = mCurrentFragment.getView();

		return reRootView;
	}
	
	public Fragment getCurrentFragment(){
		
		Fragment fFragment = new Fragment();
		
		if (mCurrentFragment != null)
			fFragment = mCurrentFragment.getTargetFragment();
		
		return fFragment;
	}
	

	public ArrayList<String> getMenuNames() {

		ArrayList<String> reArrayList = null;

		FragmentModel model = mFragmentMap.get(mType);

		if (model != null)
			reArrayList = model.nameArrayList;

		return reArrayList;
	}

	private class FragmentModel {
		ArrayList<String> nameArrayList = new ArrayList<String>();
		ArrayList<Fragment> fragmentArrayList = new ArrayList<Fragment>();

		public FragmentModel(ArrayList<String> _nameArrayList,
				ArrayList<Fragment> _fragmentArrayList) {
			nameArrayList = _nameArrayList;
			fragmentArrayList = _fragmentArrayList;
		}

	}

	public void setType(int type) {
		mType = type;
	}

	public int getType() {
		return mType;
	}

	public boolean isChildFlag() {
		return childFlag;
	}

	public void setChildFlag(boolean childFlag) {
		this.childFlag = childFlag;
	}
}
