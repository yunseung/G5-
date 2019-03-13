package com.ktrental.dialog;

import android.content.Context;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;

import com.ktrental.popup.BaseTouchDialog;
import com.ktrental.popup.ProgressPopup;

public class DialogC extends BaseTouchDialog {

	protected ProgressPopup pp;
	
	public DialogC(Context context) {
		super(context);
		pp = new ProgressPopup(context);
		//myung 20131121 ADD 시스템 메뉴 삭제
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
	}

    public void showProgress() {
		if (pp != null) {
			try {
				if (pp != null)
					
					pp.show();
			} catch (BadTokenException e) {
				// TODO: handle exception
			} catch (IllegalStateException e) {
				// TODO: handle exception
			}
			catch (Exception e) {
            // TODO: handle exception
        }

		}
	}

	public void showProgress(String message) {
		if (pp != null) {
			try
                {
                pp.setMessage(message);
                pp.show();
                }
            catch(Exception e)
                {
                }
		}
	}

	public void hideProgress() {
		if (pp != null) {
			pp.hide();
		}
	}
    public String getDateFormat(String date)
    {
    	
    	// 2013.12.03	ypkim
    	if ( date == null)
    	{
    		return "";
    	}
        StringBuffer sb = new StringBuffer(date);
        if(date.length()==8)
            {
            sb.insert(4, ".");
            int last = sb.length()-2;
            sb.insert(last, ".");
            }
        return sb.toString();
    }
    

    
   
    
}
