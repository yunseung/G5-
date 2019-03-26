package com.ktrental.model;

import com.ktrental.util.CommonUtil;
import com.ktrental.util.LogUtil;

public class CarInfoModel extends BaseMaintenanceModel {
	/**
	 * 고객 담당자명
	 */
	private String customerName = "";

	/**
	 * 순회차량번호
	 */
	private String tourCarNum = "";
	/**
	 * 담당자 연락처
	 */
	private String representativeName = "";
	/**
	 * 타이어스펙(앞)
	 */
	private String tireFront = "";
	/**
	 * 타이어스펙(뒤)
	 */
	private String tireRear = "";
	/**
	 * 최종 주행거리
	 */
	private String lastMileage = "";
	/**
	 * 계약 번호/ 고객명(?)
	 */
	private String contractNum = "";
	/**
	 * 계약구분
	 */
	private String contractCategory = "";
	/**
	 * 영업 매니저
	 */
	private String businessManager = "";
	/**
	 * 보유 지점
	 */
	private String possetionBranch = "";
	/**
	 * 계약지점
	 */
	private String contractBranch = "";
	/**
	 * 순회정비담당자
	 */
	private String tourRepresentative = "";
	/**
	 * 순회정비담당자 연락처
	 */
	private String tourContact = "";
	/**
	 * 위탁 기간(yyyy.mm.dd ,yyyy.mm.dd, 개월수)
	 */
	private String trustTerm = "";
	/**
	 * 차량 등록 일
	 */
	private String carResistDay = "";
	/**
	 * LDW
	 */
	private String LDW = "";
	/**
	 * 정비 상품
	 */
	private String maintenanceProducts = "";
	/**
	 * 순회정비주기
	 */
	private String tourMainenancePeriod = "";
	/**
	 * 일반타이어
	 */
	private String nomalTire = "";
	/**
	 * 대차제공서비스
	 */
	private String rentalService = "";
	/**
	 * 일반정비
	 */
	private String nomalMaintenance = "";
	/**
	 * 검사정비
	 */
	private String checkMaintenance = "";
	/**
	 * 타이어펑크
	 */
	private String tireFunk = "";
	/**
	 * 긴급출동횟수
	 */
	private String emergencyResponseCount = "";
	/**
	 * 스노우타이어
	 */
	private String snowTire = "";

	/**
	 * MDLCD
	 */
	private String mdlcd = "";

	/**
	 * oil type
	 */
	private String oilType = "";
	
	/**
	 * mtqty
	 */
	private String mtqty = "";
	
	

	/**
	 * AUFNR 오더번호
	 */
	private String AUFNR = "";

	private String GUBUN = "";

	private String imageName;

	private String trustTerm2 = "";

	private String chain;

	/**
	 * 운전자연락처(mobile)
	 */
	private String drv_mob = "";
	
	/**
	 * 운전자연락처(tel)
	 */
	private String drv_tel = "";
	
	private String d_name = "";	// 운전자명
	
	private String c_name = ""; // 고객명
	
	
	// public CarInfoModel(String _name, String _carNum, String _address,
	// String _tel, String _time, String _carname,
	// String _progress_status, String _day) {
	// super(_name, _carNum, _address, _tel, _time, _carname,
	// _progress_status, _day);
	// // TODO Auto-generated constructor stub
	// }

	private String OILTYPNM = "";

	private String CEMER = "";

	private String OWNER = "";

	private String DELAY = "";

	private String TIRE_SIZE = "";

	private String apm = "";

	public CarInfoModel(String customer_name, String driver_name, String _carNum, String _address,
			String _tel, String _time, String _carname,
			String _progress_status, String _day, String customerName,
			String representativeName, String tourCarNum, String tireFront,
			String tireRear, String lastMileage, String contractNum,
			String contractCategory, String businessManager,
			String possetionBranch, String contractBranch,
			String tourRepresentative, String tourContact, String trustTerm,
			String carResistDay, String lDW, String maintenanceProducts,
			String tourMainenancePeriod, String nomalTire,
			String rentalService, String nomalMaintenance,
			String checkMaintenance, String tireFunk,
			String emergencyResponseCount, String snowTire, String mdlcd,
			String oilType, String AUFNR, String _trustTerm2, String _OILTYPNM,
			String EQUNR, String CTRTY, String _CEMER, String CHNGBN,
			String OWNER, String postCode, String city, String street, 
			String drv_mob, String drv_tel, String gueen2 , String txt30, String vocNum, String kunnr, String delay) {
		super(customer_name ,driver_name, _carNum, _address, _tel, _time, _carname,
				_progress_status, _day, AUFNR, EQUNR, CTRTY, postCode, city,
				street, _tel, gueen2, txt30, mdlcd, vocNum, kunnr, delay, null, null);
		LogUtil.d("hjt", "hjt delay = " + delay);

		this.customerName = customerName;
		this.representativeName = representativeName;
		this.tourCarNum = tourCarNum;
		this.tireFront = tireFront;
		this.tireRear = tireRear;
		this.lastMileage = lastMileage;
		this.contractNum = contractNum;
		this.contractCategory = contractCategory;
		this.businessManager = businessManager;
		this.possetionBranch = possetionBranch;
		this.contractBranch = contractBranch;
		this.tourRepresentative = tourRepresentative;
		this.tourContact = tourContact;
		this.trustTerm = trustTerm;
		this.carResistDay = carResistDay;
		LDW = lDW;
		this.maintenanceProducts = maintenanceProducts;
		this.tourMainenancePeriod = tourMainenancePeriod;
		this.nomalTire = nomalTire;
		this.rentalService = rentalService;
		this.nomalMaintenance = nomalMaintenance;
		this.checkMaintenance = checkMaintenance;
		this.tireFunk = tireFunk;
		this.emergencyResponseCount = emergencyResponseCount;
		this.snowTire = snowTire;
		this.mdlcd = mdlcd;
		this.oilType = oilType;
		this.AUFNR = AUFNR;
		this.trustTerm2 = _trustTerm2;
		OILTYPNM = _OILTYPNM;
		CEMER = _CEMER;
		this.chain = CHNGBN;
		this.OWNER = OWNER;
		this.drv_mob = drv_mob;
		this.drv_tel = drv_tel;
		this.DELAY = delay;
		
		d_name = driver_name;
		c_name = customer_name;
	}
	

	public String getDriverName() {	// 운전자명 받기
		if (d_name == null) {
			d_name = "";
		}
		return d_name;
	}

	public void setDriverName(String _name) {	// 운전자명 보내기
		d_name = _name;
	}
	
//	public String getCustomer_Name() {	// 고객명 받기
//		if (c_name == null) {
//			c_name = "";
//		}
//		return c_name;
//	}
//
//	public void setCustomer_Name(String _name) {	// 고객명 보내기
//		c_name = _name;
//	}

	public String getCustomerName() {
		if (customerName == null) {
			customerName = "";
		}
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getRepresentativeName() {
		if (representativeName == null) {
			representativeName = "";
		}
		return representativeName;
	}

	public void setRepresentativeName(String representativeName) {
		this.representativeName = representativeName;
	}

	public String getTourCarNum() {
		if (tourCarNum == null) {
			tourCarNum = "";
		}
		return tourCarNum;
	}

	public void setTourCarNum(String tourCarNum) {
		this.tourCarNum = tourCarNum;
	}

	public String getTireFront() {
		if (tireFront == null) {
			tireFront = "";
		}
		return tireFront;
	}

	public void setTireFront(String tireFront) {
		this.tireFront = tireFront;
	}

	public String getTireRear() {
		if (tireRear == null) {
			tireRear = "";
		}
		return tireRear;
	}

	public void setTireRear(String tireRear) {
		this.tireRear = tireRear;
	}

	public String getLastMileage() {
		if (lastMileage == null) {
			lastMileage = "";
		}
		return lastMileage;
	}

	public void setLastMileage(String lastMileage) {
		this.lastMileage = lastMileage;
	}

	public String getContractNum() {
		if (contractNum == null) {
			contractNum = "";
		}
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public String getContractCategory() {
		if (contractCategory == null) {
			contractCategory = "";
		}
		return contractCategory;
	}

	public void setContractCategory(String contractCategory) {
		this.contractCategory = contractCategory;
	}

	public String getBusinessManager() {
		if (businessManager == null) {
			businessManager = "";
		}
		return businessManager;
	}

	public void setBusinessManager(String businessManager) {
		this.businessManager = businessManager;
	}

	public String getPossetionBranch() {
		if (possetionBranch == null) {
			possetionBranch = "";
		}
		return possetionBranch;
	}

	public void setPossetionBranch(String possetionBranch) {
		this.possetionBranch = possetionBranch;
	}

	public String getContractBranch() {
		if (contractBranch == null) {
			contractBranch = "";
		}
		return contractBranch;
	}

	public void setContractBranch(String contractBranch) {
		this.contractBranch = contractBranch;
	}

	public String getTourRepresentative() {
		if (tourRepresentative == null) {
			tourRepresentative = "";
		}
		return tourRepresentative;
	}

	public void setTourRepresentative(String tourRepresentative) {
		this.tourRepresentative = tourRepresentative;
	}

	public String getTourContact() {
		if (tourContact == null) {
			tourContact = "";
		}
		return tourContact;
	}

	public void setTourContact(String tourContact) {
		this.tourContact = tourContact;
	}

	public String getTrustTerm() {
		if (trustTerm == null) {
			trustTerm = "";
		}
		return trustTerm;
	}

	public void setTrustTerm(String trustTerm) {
		this.trustTerm = trustTerm;
	}

	public String getCarResistDay() {
		if (carResistDay == null) {
			carResistDay = "";
		}
		return carResistDay;
	}

	public void setCarResistDay(String carResistDay) {
		this.carResistDay = carResistDay;
	}

	public String getLDW() {
		if (LDW == null) {
			LDW = "";
		}
		return LDW;
	}

	public void setLDW(String lDW) {
		LDW = lDW;
	}

	public String getMaintenanceProducts() {
		if (maintenanceProducts == null) {
			maintenanceProducts = "";
		}
		return maintenanceProducts;
	}

	public void setMaintenanceProducts(String maintenanceProducts) {
		this.maintenanceProducts = maintenanceProducts;
	}

	public String getTourMainenancePeriod() {
		if (tourMainenancePeriod == null) {
			tourMainenancePeriod = "";
		}
		return tourMainenancePeriod;
	}

	public void setTourMainenancePeriod(String tourMainenancePeriod) {
		this.tourMainenancePeriod = tourMainenancePeriod;
	}

	public String getNomalTire() {
		if (nomalTire == null) {
			nomalTire = "";
		}
		return nomalTire;
	}

	public void setNomalTire(String nomalTire) {
		this.nomalTire = nomalTire;
	}

	public String getRentalService() {
		if (rentalService == null) {
			rentalService = "";
		}
		return rentalService;
	}

	public void setRentalService(String rentalService) {
		this.rentalService = rentalService;
	}

	public String getNomalMaintenance() {
		if (nomalMaintenance == null) {
			nomalMaintenance = "";
		}
		return nomalMaintenance;
	}

	public void setNomalMaintenance(String nomalMaintenance) {
		this.nomalMaintenance = nomalMaintenance;
	}

	public String getCheckMaintenance() {
		if (checkMaintenance == null) {
			checkMaintenance = "";
		}
		return checkMaintenance;
	}

	public void setCheckMaintenance(String checkMaintenance) {
		this.checkMaintenance = checkMaintenance;
	}

	public String getTireFunk() {
		if (tireFunk == null) {
			tireFunk = "";
		}
		return tireFunk;
	}

	public void setTireFunk(String tireFunk) {
		this.tireFunk = tireFunk;
	}

	public String getEmergencyResponseCount() {
		if (emergencyResponseCount == null) {
			emergencyResponseCount = "";
		}
		return emergencyResponseCount;
	}

	public void setEmergencyResponseCount(String emergencyResponseCount) {
		this.emergencyResponseCount = emergencyResponseCount;
	}

	public String getSnowTire() {
		if (snowTire == null) {
			snowTire = "";
		}
		return snowTire;
	}

	public void setSnowTire(String snowTire) {
		this.snowTire = snowTire;
	}

	public String getMdlcd() {
		if (mdlcd == null) {
			mdlcd = "";
		}
		return mdlcd;
	}

	public void setMdlcd(String mdlcd) {
		this.mdlcd = mdlcd;
	}

	public String getOilType() {
		if (oilType == null) {
			oilType = "";
		}
		return oilType;
	}

	public void setOilType(String oilType) {
		this.oilType = oilType;
	}
	
	//Jonathan 14.11.17
	public String getMtqty() {
		if (mtqty == null) {
			mtqty = "";
		}
		return mtqty;
	}
	
	public void setMtqty(String mtqty) {
		this.mtqty = mtqty;
	}
	
	

	public String getAUFNR() {
		if (AUFNR == null) {
			AUFNR = "";
		}
		return AUFNR;
	}

	public void setAUFNR(String aUFNR) {
		AUFNR = aUFNR;
	}

	public String getGUBUN() {
		if (GUBUN == null) {
			GUBUN = "";
		}
		return GUBUN;
	}

	public void setGUBUN(String gUBUN) {
		GUBUN = gUBUN;
	}

	public String getTrustTerm2() {
		if (trustTerm2 == null) {
			trustTerm2 = "";
		}
		return trustTerm2;
	}

	public void setTrustTerm2(String trustTerm2) {
		this.trustTerm2 = trustTerm2;
	}

	public String getImageName() {
		if (imageName == null || imageName.equals("null"))
			imageName = "" + CommonUtil.getCurrentTime();
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getOILTYPNM() {
		if (OILTYPNM == null) {
			OILTYPNM = "";
		}
		return OILTYPNM;
	}

	public void setOILTYPNM(String oILTYPNM) {
		OILTYPNM = oILTYPNM;
	}

	public String getCEMER() {
		if (CEMER == null) {
			CEMER = "";
		}
		return CEMER;
	}

	public void setCEMER(String cEMER) {
		CEMER = cEMER;
	}

	public String getChain() {
		if (chain == null) {
			chain = "";
		}
		return chain;
	}

	public void setChain(String chain) {
		this.chain = chain;
	}

	public String getOWNER() {
		if (OWNER == null) {
			OWNER = "";
		}
		return OWNER;
	}

	public void setOWNER(String oWNER) {
		OWNER = oWNER;
	}
	
	public String getDrv_mob() {
		return drv_mob;
	}
	
	public void setDrv_mob(String _drv_mob) {
		drv_mob = _drv_mob;
	}
	
	public String getDrv_tel() {
		return drv_tel;
	}
	
	public void setDrv_tel(String _drv_tel) {
		drv_tel = _drv_tel;
	}

	public void setTire_size(String tire_size) {
		TIRE_SIZE = tire_size;
	}
	public String getTIRE_SIZE(){
		return TIRE_SIZE;
	}
}
