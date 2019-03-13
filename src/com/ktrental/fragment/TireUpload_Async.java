package com.ktrental.fragment;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.os.AsyncTask;

import com.ktrental.common.DEFINE;
import com.ktrental.popup.ProgressPopup;
import com.ktrental.util.kog;

public class TireUpload_Async extends AsyncTask<String, ArrayList<String>, String>
{

    public static final String      MSG_SUCCESS   = "완료되었습니다";
    public static final String      MSG_FAIL      = "네트웍 연결 상태를 확인하세요.";
    private Context                 mContext;
    private String                  strResult;

    public String            postUrl         = DEFINE.PIC_UPLOAD_URL + "/ktrerp/file/upload2";
    public String            CRLF            = "\r\n";
    public String            twoHyphens      = "--";
    public String            boundary        = "6dsf7sdf87dsf";
    public DataOutputStream dataStream       = null;
//    private ProgressDialog dialog;
    private ProgressPopup pp;
    
    
    enum ReturnCode
        {
        noPicture, unknown, http201, http400, http401, http403, http404, http500
        };


    public TireUpload_Async(Context context)
        {
        mContext = context;
//        Log.i("어싱크","생성");
        }
    
    @Override
    protected void onPreExecute()
        {
        pp = new ProgressPopup(mContext);
        pp.setMessage("사진을 전송 중 입니다.");
        pp.show();
        }

    @Override
    protected String doInBackground(String... params)
        {
        ArrayList<String> al = new ArrayList<String>();
        String response = null;
        for(int i=0;i<params.length;i++)
            {
            File uploadFile = new File(params[i]);
            if (uploadFile.exists()) try
                {
                FileInputStream fileInputStream = new FileInputStream(uploadFile);
                URL connectURL = new URL(postUrl);
                HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
                conn.setConnectTimeout(10000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("User-Agent", "myGeodiary-V1");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.connect();
                dataStream = new DataOutputStream(conn.getOutputStream());
                writeFileField("photo1", params[i], "image/jpg", fileInputStream);
                dataStream.writeBytes(twoHyphens + boundary + twoHyphens + CRLF);
                fileInputStream.close();
                dataStream.flush();
                dataStream.close();
                dataStream = null;
                response = getResponse(conn);
                
                kog.e("Jonathan","#### 결과물 : "+response);

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                
                InputStream inputStream = new ByteArrayInputStream(response.getBytes("UTF-8"));
                parser.setInput(inputStream, "UTF-8");
                String TAG = null;
                String ATTR = null;
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) 
                    {
                    switch (eventType) 
                        {
                        case XmlPullParser.START_TAG: 
                            TAG = parser.getName(); 
                            ATTR = parser.getAttributeValue(null, "id");
                            break;
                        case XmlPullParser.TEXT: 
                            if(TAG.equals("Col")&&ATTR.equals("fileNo"))
                                {
                                String str = parser.getText().replace("\t", "");
                                if(str.equals("")) break;
                                al.add(parser.getText());
                                }
                            break;
                        }
                    eventType = parser.next();
                    }
                }
                
            catch(MalformedURLException e) { e.printStackTrace();}
            catch(IOException e) { e.printStackTrace();}
            catch(Exception e) { e.printStackTrace();}
            }

        pp.dismiss();
        publishProgress(al);
        return null;
        }
    
    private void writeFileField(
        String fieldName,
        String fieldValue,
        String type,
        FileInputStream fis)
        {
        try {
            // opening boundary line
            dataStream.writeBytes(twoHyphens + boundary + CRLF);
            dataStream.writeBytes("Content-Disposition: form-data; name=\""
                + fieldName
                + "\";filename=\""
                + fieldValue
                + "\""
                + CRLF);
            dataStream.writeBytes("Content-Type: " + type + CRLF);
            dataStream.writeBytes(CRLF);
            // create a buffer of maximum size
            int bytesAvailable = fis.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            // read file and write it into form...
            int bytesRead = fis.read(buffer, 0, bufferSize);
            while (bytesRead > 0)
                {
                dataStream.write(buffer, 0, bufferSize);
                bytesAvailable = fis.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fis.read(buffer, 0, bufferSize);
                }
            // closing CRLF
            dataStream.writeBytes(CRLF);
            }
        catch(Exception e)
            {
            System.out.println("GeoPictureUploader.writeFormField: got: " + e.getMessage());
            // Log.e(TAG, "GeoPictureUploader.writeFormField: got: " +
            // e.getMessage());
            }
        }
    
    private String getResponse(HttpURLConnection http)
        {
        try {
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null)
                {
                builder.append(str);
                }
            return builder.toString();
            }
        catch(Exception e) { e.printStackTrace(); return "Exception"; }
        }
    
    @Override
    protected void onPostExecute(String result)
        {
        if (pp != null && pp.isShowing())
            {
            pp.dismiss();
            pp = null;
            }
        }

    @Override
    protected void onCancelled()
        {
        super.onCancelled();
        if (pp != null && pp.isShowing())
            {
            pp.dismiss();
            pp = null;
            }
        }
 
}
