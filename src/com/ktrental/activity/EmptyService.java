package com.ktrental.activity;


import com.ktrental.util.kog;


import com.ktrental.R;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class EmptyService extends Service {
	
	
	
	static Activity getClass;
	

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ad_icon);
        mBuilder.setContentTitle("롯데렌탈 차량관리");
        mBuilder.setContentText("롯데렌탈 차량관리" + " 어플이 실행중 입니다");

        Intent resultIntent = new Intent(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        if(getClass != null && getClass.getClass() != null)
        {
            resultIntent.setComponent(new ComponentName(getApplicationContext(), getClass.getClass()));
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, 0);
            mBuilder.setContentIntent(pendingIntent);
            startForeground(startId, mBuilder.build());
            return super.onStartCommand(intent, flags, startId);
        }
        else
        {
            super.onDestroy();
        }




        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
//        PrintLog.print("EmptyService onDestroy", "stopForeground");
        super.onDestroy();
    }

    
    public static void getActivity(Activity className)
    {
    	getClass = className;
    	
    	kog.e("Joanthan", "Jonathan getActivity :: "+ getClass.getClass() );
    	
    }

}

