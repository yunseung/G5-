package com.ktrental.model;

public class BaseMaintenanceModel
{
    protected boolean check    = false;
    protected String  CUSTOMER_NAME;
    protected String  DRIVER_NAME;
    protected String  carNum;
    protected String  address;
    protected String  tel;
    protected String  time;
    protected String  carname;
    protected String  progress_status;

    protected String  day;

    protected String  GUBUN    = " ";
    private String    imageName;

    private String    AUFNR    = "";
    private String    EQUNR    = "";
    private String    CTRTY    = "";

    private String    postCode = "";
    private String    city     = "";
    private String    street   = "";
    private String    drv_mob  = "";
    private String    GUEEN2   = "";

    private String    MDLCD    = "";
    
    private String 	 KUNNR = "";

    // /////////////
    private String    TXT30    = "";

    // /////////////

    private String VOCNUM = "";

    //2017-06-02. hjt 지연일수 추가
    private String DELAY = "";

    //2017-11-28. hjt CYCMN_TX 추가
    private String CYCMN_TX = "";
    public BaseMaintenanceModel(String aUFNR)
    {
        super();
        AUFNR = aUFNR;
    }

    public String getImageName()
    {
        return imageName;
    }

    public void setImageName(String imageName)
    {
        this.imageName = imageName;
    }

    public BaseMaintenanceModel(String customer_name, String driver_name, String _carNum, String _address, String _tel, String _time,
            String _carname, String _progress_status, String _day, String AUFNR, String _EQUNR, String _CTRTY, String postCode, String city,
            String street, String _drv_mob, String _gueen2, String _txt30, String _mdlcd, String _vocNum, String _kunnr, String delay, String cycmn_tx)
    {

        CUSTOMER_NAME = customer_name;
        DRIVER_NAME = driver_name;
        carNum = _carNum;
        address = _address;
        tel = _tel;
        time = _time;
        carname = _carname;
        progress_status = _progress_status;
        day = _day;
        this.AUFNR = AUFNR;
        EQUNR = _EQUNR;
        CTRTY = _CTRTY;
        this.postCode = postCode;
        this.city = city;
        this.street = street;
        drv_mob = _drv_mob;
        if (_drv_mob == null)
            drv_mob = "";
        GUEEN2 = _gueen2;

        KUNNR = _kunnr;
        
        // /////////////
        TXT30 = _txt30;
        // /////////////

        MDLCD = _mdlcd;
        VOCNUM = _vocNum;

        DELAY = delay;

        CYCMN_TX = cycmn_tx;
//        kog.e("Jonathan", "Hello Jonathan1 :: " + VOCNUM);
//        LogUtil.d("hjt", "hjt delay = " + delay);
    }

    public String getMDLCD()
    {
        return MDLCD;
    }

    public void setMDLCD(String _mdlcd)
    {
        MDLCD = _mdlcd;
    }

    // ///////////////////////////
    public String getTXT30()
    {
        return TXT30;
    }

    public void setTXT30(String txt30)
    {
        TXT30 = txt30;
    }

    // ///////////////////////////

    public String getGUEEN2()
    {
        return GUEEN2;
    }

    public void setGUEEN2(String gueen2)
    {
        GUEEN2 = gueen2;
    }

    public String getCUSTOMER_NAME()
    {
        return CUSTOMER_NAME;
    }

    public void setCUSTOMER_NAME(String name)
    {
        this.CUSTOMER_NAME = name;
    }
    
    
    
    public String getKUNNR()
    {
        return KUNNR;
    }

    public void setKUNNR(String KUNNR)
    {
        this.KUNNR = KUNNR;
    }
    
    
    

    public void setDRIVER_NAME(String NAME1)
    {
        this.DRIVER_NAME = NAME1;
    }

    public String getDRIVER_NAME()
    {
        return DRIVER_NAME;
    }

    public String getCarNum()
    {
        return carNum;
    }

    public void setCarNum(String carNum)
    {
        this.carNum = carNum;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public boolean isCheck()
    {
        return check;
    }

    public void setCheck(boolean check)
    {
        this.check = check;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String getCarname()
    {
        return carname;
    }

    public void setCarname(String carname)
    {
        this.carname = carname;
    }

    public String getProgress_status()
    {
        return progress_status;
    }

    public void setProgress_status(String progress_status)
    {
        this.progress_status = progress_status;
    }

    public String getTel()
    {
        return tel;
    }

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public String getDay()
    {
        return day;
    }

    public void setDay(String day)
    {
        this.day = day;
    }

    public String getGUBUN()
    {
        return GUBUN;
    }

    public void setGUBUN(String gUBUN)
    {
        GUBUN = gUBUN;
    }

    public String getAUFNR()
    {
        return AUFNR;
    }

    public void setAUFNR(String aUFNR)
    {
        AUFNR = aUFNR;
    }

    public String getEQUNR()
    {
        return EQUNR;
    }

    public void setEQUNR(String eQUNR)
    {
        EQUNR = eQUNR;
    }

    public String getCTRTY()
    {
        return CTRTY;
    }

    public void setCTRTY(String cTRTY)
    {
        CTRTY = cTRTY;
    }

    public String getPostCode()
    {
        return postCode;
    }

    public void setPostCode(String postCode)
    {
        this.postCode = postCode;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getStreet()
    {
        return street;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public String getDrv_mob()
    {
        return drv_mob;
    }

    public void setDrv_mob(String drv_mob)
    {
        this.drv_mob = drv_mob;
    }
    
    public void setVocNum(String voc_num) {
        this.VOCNUM = voc_num;
    }
    
    public String getVocNum()
    {
        return VOCNUM;
    }

    public void serDelay(String delay) { this.DELAY = delay;}

    public String getDelay() { return DELAY; }

    public void setCYCMN_TX(String cycmn_tx) { this.CYCMN_TX = cycmn_tx;}

    public String getCYCMN_TX() { return CYCMN_TX; }

}
