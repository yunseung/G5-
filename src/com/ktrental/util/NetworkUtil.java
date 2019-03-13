package com.ktrental.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

	public static boolean isNetwork(Context context) {

		boolean reNetwork = false;
		
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//		boolean isWifiAvail = ni.isAvailable();
		boolean isWifiConn = ni.isConnected();
		ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		
	

//		String status = "WiFi\nAvail = " + isWifiAvail + "\nConn = "
//				+ isWifiConn + "\nMobile\nAvail = " + isMobileAvail
//				+ "\nConn = " + isMobileConn + "\n";

		if (isWifiConn ) {
			reNetwork = true;
		}else{
			if (ni != null)
				reNetwork = ni.isConnected();
		}

		return reNetwork;
	}
}
