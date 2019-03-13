package com.ktrental.activity;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Config;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by jaewooklee on 2017. 5. 17..
 */

public class GPS {

    public static final String TAG = "GPS ";

    public Context mContext = null;

    public static LocationManager mLocationManager = null;

    public static double dbLat;
    public static double dbLng;



    public GPS(Context context){

        this.mContext = context;



        if(this.mContext != null){

// 1. LocationManager 객체 생성

            this.mLocationManager = (LocationManager) this.mContext.getSystemService(Context.LOCATION_SERVICE);

        }

    }



// 2. LocationListener에 해당 프로바이더 등록(GPS, NETWORK)

    public static LocationListener [] mLocationListeners = new LocationListener[]{

            new LocationListener(LocationManager.GPS_PROVIDER),

            new LocationListener(LocationManager.NETWORK_PROVIDER)

    };

// 3. LocationListener 리스너 설정

    private static class LocationListener implements android.location.LocationListener{

        Location mLastLocation;

        boolean mValid = false;

        String mProvider;



        public LocationListener(String provider){

            mProvider = provider;

            mLastLocation = new Location(mProvider);

        }

// 4 . 현재 자신의 위치가 바뀔 때마다 업데이트 되는 내용. (난 단순히 위,경도를 출력)

        //Called when the location has changed.

        public void onLocationChanged(Location newLocation){

            if (newLocation.getLatitude() == 0.0 && newLocation.getLongitude() == 0.0){

                // Hack to filter out 0.0,0.0 locations

                // MainActivity(Main Thread)에서 Context를 넘겨 받았으니, 실행이된다.

//                Toast.makeText(mContext, "Try again", Toast.LENGTH_SHORT).show();

                return;

            }

            if (newLocation != null){

                ///if(newLocation.getTime() == 0) newLocation.setTime(System.currentTimeMillis());

                String strNowLocationLatLngInfo;

                newLocation.setTime(System.currentTimeMillis());



//                Toast.makeText(mContext, "위도:"+ String.valueOf(newLocation.getLatitude()) +
//
//                                " 경도: " + String.valueOf(newLocation.getLongitude()),
//
//                        Toast.LENGTH_SHORT).show();



                if(newLocation.getLatitude() != 0.0 && newLocation.getLongitude() != 0.0 ){

                    strNowLocationLatLngInfo =   "GPS Lat:" +

                            String.valueOf(newLocation.getLatitude()) + " Lng:" +

                            String.valueOf(newLocation.getLongitude());



                    //MainActivity.dbLatitude = newLocation.getLatitude();

                    dbLat = newLocation.getLatitude();

                    dbLng = newLocation.getLongitude();



                }else{

                    strNowLocationLatLngInfo = "Not available";



//                    Toast.makeText(mContext, strNowLocationLatLngInfo,
//
//                            Toast.LENGTH_SHORT).show();

                }



                if(Config.DEBUG){

                    Log.i(TAG, "onLocationChanged in loc mgnr");

                }

            }

            mLastLocation.set(newLocation);

            mValid = true;

        }



        // Called when the provider is ㄷabled by the user.

        public void onProviderEnabled(String provider) {

        }

        // Called when the provider is disabled by the user.

        public void onProviderDisabled(String provider){

            mValid = false;

        }

        // Called when the provider status changes.

        public void onStatusChanged(String provider, int status, Bundle extras){

            if (status == LocationProvider.OUT_OF_SERVICE){

                mValid = false;

            }

        }

        /**

         * @brief 	현재 위치가 유효한지 확인

         * @return	현재 위치

         */

        public Location current(){

            return mValid ? mLastLocation : null;

        }

    };



// 5. 위치 추적

    /**

     * @ brief 위치 검색 시작

     */

    public static void startLocationReceiving(){

        if (mLocationManager != null){

            try{

// 5-1. 지정된 provider, 시간, 거리마다 해당 listener에 request됨.(결국, 이 메소드로 위치정보얻기가 실행됨)

                mLocationManager.requestLocationUpdates(

                        LocationManager.NETWORK_PROVIDER,

                        0,

                        0F,

                        mLocationListeners[1] );

            }catch (java.lang.SecurityException ex){

                if(Config.DEBUG){

                    Log.e(TAG, "SecurityException " + ex.getMessage());

                }

            }catch (IllegalArgumentException ex){

                //Log.e(TAG, "provider does not exist " + ex.getMessage());

            }



            try{

                mLocationManager.requestLocationUpdates(

                        LocationManager.GPS_PROVIDER,

                        0,

                        0F,

                        mLocationListeners[0]);

            }catch (java.lang.SecurityException ex){

                if(Config.DEBUG){

                    Log.e(TAG, "SecurityException " + ex.getMessage());

                }

            }catch (IllegalArgumentException ex){

                //Log.e(TAG, "provider does not exist " + ex.getMessage());

            }

        }

    }

// 6. 위치 추적 종료

    /**

     * @ brief 위치 검색 종료

     */

    public static void stopLocationReceiving() {

        if (mLocationManager != null) {

            for (int i = 0; i < mLocationListeners.length; i++){

                try{

                    mLocationManager.removeUpdates(mLocationListeners[i]);

                } catch (Exception ex) {

                    // ok

                }

            }

        }

    }

// 7. 현재 위치의 Location정보 얻기

    /**

     * @ brief 현재 위치 정보 검색

     */

    public Location getCurrentLocation() {

        Location l = null;



        // go in best to worst order

        for (int i = 0; i < this.mLocationListeners.length; i++) {

            l = this.mLocationListeners[i].current();

            if (l != null){

                break;

            }

        }



        return l;

    }

}


