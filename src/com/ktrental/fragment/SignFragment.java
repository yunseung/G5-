package com.ktrental.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ktrental.R;
import com.ktrental.common.DEFINE;
import com.ktrental.dialog.Mistery_Shopping_Dialog;
import com.ktrental.model.MaintenanceSendSignModel;
import com.ktrental.popup.InventoryPopup;
import com.ktrental.popup.InventoryPopup.OnDismissListener;
import com.ktrental.popup.QuickAction;
import com.ktrental.ui.FingerPaint_View;
import com.ktrental.util.kog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class SignFragment extends BaseFragment implements OnClickListener {


	final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TEST_IMG/";

	private EditText mEtName;
	private TextView mTvContact;
	private FingerPaint_View mFingerPaint_View;
	private String mName;
	private String mContact;
	private OnSignListener mOnSignListener;
	private String mSignImageName;
	
	
	
	//	private int SCALE = 16;
	private int SCALE = 1;

	private String mCarInfoName;

	private String mInvnr = "";
	

	private ArrayList<MaintenanceSendSignModel> mMaintenanceSendSignModels = new ArrayList<MaintenanceSendSignModel>();

	public interface OnSignListener {
		void onSignComplate(
                ArrayList<MaintenanceSendSignModel> aMaintenanceSendSignModel,
                String name, String contact);
	}
	public SignFragment(){}

	@SuppressLint("ValidFragment")
	public SignFragment(String name,
						String contact,
						OnSignListener aOnSignListener,
						String imageName,
						String _carInfoName,
						String invnr) {

		MakeFolderPath(PATH);
		
		kog.e("Jonathan", "mSignImageName :: " + imageName);

		mName = name;
		mContact = contact;
		mOnSignListener = aOnSignListener;
//		mSignImageName = imageName;
		mSignImageName =imageName+".jpg";
		mCarInfoName = _carInfoName;
		mInvnr = invnr;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.sign_popup_layout, null);

		mEtName = (EditText) mRootView.findViewById(R.id.et_name);
		mTvContact = (TextView) mRootView.findViewById(R.id.tv_contact);
		mTvContact.setOnClickListener(this);
		mRootView.findViewById(R.id.btn_reset).setOnClickListener(this);
		mRootView.findViewById(R.id.btn_save).setOnClickListener(this);
		mRootView.findViewById(R.id.btn_cancel).setOnClickListener(this);

		mFingerPaint_View = (FingerPaint_View) mRootView
				.findViewById(R.id.fpv_id);

		TextView title = (TextView) mRootView
				.findViewById(R.id.tv_dialog_title);

		mEtName.setText(mName);
		mTvContact.setText(mContact);

		title.setText("고객확인");

		mRootView.findViewById(R.id.iv_exit).setOnClickListener(this);
		return mRootView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_reset:
			clickReset();
			break;
		case R.id.btn_save:
			Mistery_Shopping_Dialog dlg = new Mistery_Shopping_Dialog(mContext, "40");
			dlg.setOnDismissListener(new Dialog.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					clickSave();
				}
			});
			dlg.show();

			break;
		case R.id.btn_cancel:
			clickCancel();
			break;
		case R.id.iv_exit:
			dismiss();
			break;
		case R.id.tv_contact:
			clickContact(v);
			break;
		default:
			break;
		}
	}

	private void clickContact(View v) {
		InventoryPopup inventoryPopup = new InventoryPopup(mContext,
				QuickAction.HORIZONTAL, R.layout.inventory_popup,
				InventoryPopup.TYPE_PHONE_NUMBER);
		inventoryPopup.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(String result, int position) {
				// TODO Auto-generated method stub
				mTvContact.setText(result);
			}
		});
		inventoryPopup.show(v, 0, mRootView.getMeasuredWidth(),
				mRootView.getMeasuredHeight());
	}

	private void clickReset() {
		mFingerPaint_View.reset();
		mFingerPaint_View.invalidate();
	}

	private void clickSave() {
		//		Bitmap bitmap = mFingerPaint_View.getBitmap();
		
		if(mFingerPaint_View.getBitmap() == null)
		{
			showEventPopup2(null, "사인을 해주세요.");
			return;
		}
		
		
		mFingerPaint_View.setDrawingCacheEnabled(true);
		mFingerPaint_View.buildDrawingCache(false);

		Bitmap bitmap = mFingerPaint_View.getDrawingCache();

		if (DEFINE.DISPLAY.equals("2560")) {
			SCALE *= 2;
		}

		//		Log.e("???Width",  String.valueOf(bitmap.getWidth()));
		//		Log.e("???height",  String.valueOf(bitmap.getHeight()));
		//		Log.e("???Width",  String.valueOf(bitmap.getWidth()/SCALE));
		//		Log.e("???height",  String.valueOf(bitmap.getHeight()/SCALE));

		//		Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / SCALE,
		//				bitmap.getHeight() / SCALE, true);

		Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(),
				bitmap.getHeight(), true);


		kog.e("Jonathan" , "여기 들어오나??? Jonathan123 " + String.valueOf(resizeBitmap.getRowBytes()));
		
				kog.e("resizeBitmap height",  String.valueOf(resizeBitmap.getHeight()));
				kog.e("resizeBitmap Width",  String.valueOf(resizeBitmap.getWidth()));

		mFingerPaint_View.setDrawingCacheEnabled(false);
		mFingerPaint_View.destroyDrawingCache();

		//2014-04-09 KDH 일단 sd카드로 저장한다.

		//		saveImage(resizeBitmap);
		SaveBitmapToFileCache(resizeBitmap);
		
		kog.e("Joanthan", "111111");
		
		//여기서 미리 준비해놔야겠네
		TireUpload_Async asynctask = new TireUpload_Async(getActivity()) {
			@Override
			protected void onProgressUpdate(ArrayList<String>... values) {
				ArrayList<String> list = values[0];
				for (int i = 0; i < list.size(); i++) 
				{
					kog.e("KDH", "#### 사진코드넘버들" + list.get(i));
					final String upLoadName =
//							DEFINE.PIC_UPLOAD_URL+"/ktrerp/upload/"
//							day.substring(0, 8)+"/"+day.substring(8, day.length())
							list.get(i).substring(0,8)+"/"+list.get(i).substring(8, list.get(i).length())
							+"/"
							+mSignImageName;
					Thread thread = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {

								MaintenanceSendSignModel sendSignModel = new MaintenanceSendSignModel(
										upLoadName, upLoadName, mCarInfoName, mInvnr);
								mMaintenanceSendSignModels.add(sendSignModel);
								hideProgress();


								if(mFingerPaint_View != null && !mFingerPaint_View.getBitmap().isRecycled()){
										mFingerPaint_View.getBitmap().recycle();
								}
								dismiss();

							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					});
					thread.start();
				}
			}
		};
		asynctask.execute(strFilePath);
				

	}

	/*
	private void saveImage(final Bitmap bitmap) {

		//		mBitmap = bitmap;

		showProgress("고객서명을 저장 중입니다.");

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					byte[] reBytes = getBytesFromBitmap(bitmap);

					// get the base 64 string
					String imgString = Base64.encodeToString(reBytes,
							Base64.NO_WRAP);
					//					 Log.e("ImageStringSize", String.valueOf(imgString.length()));



					//myung 20140103 ADD TEST 
					//					SaveBitmapToFileCache(bitmap, "TEST_before_make_string.png");
					//					
					//					byte[] signBytes = Base64.decode(imgString, Base64.NO_WRAP);
					//					Log.e("signBytes Size", ""+signBytes.length);
					//					 
					//					Bitmap SignBMP = BitmapFactory.decodeByteArray(signBytes, 0, signBytes.length);
					//							
					//					if(SignBMP==null){
					//						Log.e("SignBMP", "SignBMP is NULL");
					//					}else{
					//						Log.e("SignBMP ByteCount", ""+SignBMP.getByteCount());
					//						SaveBitmapToFileCache(SignBMP, "TEST_after_make_string.png");	
					//					}






					int length = imgString.length() / 6000;
					//					Log.e("ImageSize", String.valueOf(imgString.length()));

					int endSize = 6000;
					int increase = 6000;
					int startSize = 0;

					for (int i = 0; i < length + 1; i++) {
						if (imgString.length() < endSize)
							endSize = imgString.length();

						String img = imgString.substring(startSize, endSize);

						startSize = endSize;
						endSize = endSize + increase;
						MaintenanceSendSignModel sendSignModel = new MaintenanceSendSignModel(
								mSignImageName, img, mCarInfoName, mInvnr);
						mMaintenanceSendSignModels.add(sendSignModel);
						//						Log.i("????", String.valueOf(mMaintenanceSendSignModels.size()));
					}

					hideProgress();
					imgString = null;
					reBytes = null;


					if(mFingerPaint_View != null && !mFingerPaint_View.getBitmap().isRecycled()){
						mFingerPaint_View.getBitmap().recycle();
					}

					dismiss();

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});

		thread.start();
	}
	*/

	public byte[] getBytesFromBitmap(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 70, stream);
		return stream.toByteArray();
	}

	String strFilePath;
	//test
	private void SaveBitmapToFileCache(Bitmap bitmap) 
	{
		strFilePath = PATH +mSignImageName;
		kog.e("KDH", "strFilePath = "+strFilePath);

		//		File file = new File("mnt/sdcard/"+strFilePath);  // 임의로 sdcard에 test.png로 저장
		File file = new File(strFilePath);  // 임의로 sdcard에 test.png로 저장
		OutputStream outputStream = null;
		try {
			file.createNewFile();
			outputStream = new FileOutputStream(file);
			//			mFingerPaint_View.buildDrawingCache();
			//			Bitmap bitmap2 = mFingerPaint_View.getDrawingCache();
			//			bitmap.compress(CompressFormat.PNG, 100, outputStream);
			bitmap.compress(CompressFormat.JPEG, 100, outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean MakeFolderPath(String strFilePath) {

		String str = Environment.getExternalStorageState();

		if (str.equals(Environment.MEDIA_MOUNTED)) {

			File file = new File(strFilePath);
			if (!file.exists()) // 원하는 경로에 폴더가 있는지 확인
				file.mkdirs();
			return true;
		} else {
			return false;
		}

	}

	private void clickCancel() {
		dismiss();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		//		mBitmap = null;
		
		String name = mEtName.getText().toString();
		String contact = mTvContact.getText().toString();
		mOnSignListener.onSignComplate(mMaintenanceSendSignModels, name,
				contact);
		super.onDismiss(dialog);
	}

}
