package com.ktrental.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/** 
 * API for sending log output. 
 * @see OdmUtility.SHOW_LOGS if true, then logs are appeared,
 */
public class kog {

	public static boolean TEST = false; 
	
	public static boolean SHOW_LOGS = true; 
	
	public static boolean TEST_ADMIN = false;

    public static String TAG = "rental";
	
    private kog() {
    }

    public static void T(Context ctx, String msg) {
        if (SHOW_LOGS) 
        {
            Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
        }
    } 

    public static void d(String tag, String message)

    {

        if (SHOW_LOGS)

        {

            String log = buildLogMsg(message);

            Log.d(TAG, log);

        }

    }



    public static void e(String tag, String message)

    {

        if (SHOW_LOGS)

        {

            String log = buildLogMsg(message);

            Log.e(TAG, log);

        }

    }



    public static void i(String tag, String message)

    {

        if (SHOW_LOGS)

        {

            String log = buildLogMsg(message);

            Log.i(TAG, log);

        }

    }



    public static void w(String tag, String message)

    {

        if (SHOW_LOGS)

        {

            String log = buildLogMsg(message);

            Log.w(TAG, log);

        }

    }



    public static void v(String tag, String message)

    {

        if (SHOW_LOGS)

        {

            String log = buildLogMsg(message);

            Log.v(TAG, log);

        }

    }



    private static String buildLogMsg(String message)

    {

        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];

        StringBuilder sb = new StringBuilder();

        sb.append("[");

        sb.append(ste.getFileName());

        sb.append(" > ");

        sb.append(ste.getMethodName());

        sb.append(" > #");

        sb.append(ste.getLineNumber());

        sb.append("] ");

        sb.append(message);



        return sb.toString();

    }
}
