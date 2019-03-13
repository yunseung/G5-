package com.ktrental.call;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 통화가 오는 경우 호출되는 클래스이다.</br> </br> {@link CallStateListner}에 이벤트를 보내준다. <br>
 * 
 * @author hongsungil
 */
public class CallStateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
//		Log.d(CallStateListner.TAG, "CallStateReceiver >>>>>>> onReceive "
//				+ intent.getAction());
		CallStateListner callStateListner = new CallStateListner(context);
		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		// TelephonyManager�� PhoneStateListner�� ����Ѵ�
		telephony
				.listen(callStateListner, PhoneStateListener.LISTEN_CALL_STATE);
	}

}
