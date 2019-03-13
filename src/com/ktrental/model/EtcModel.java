package com.ktrental.model;

public class EtcModel {
	String emergency;
	String startTime;
	String endTime;
	String remarks;
	String nextRequest;
	String contactRequest;
	String distance;
	String ISDD;
	String IEDD;

	public EtcModel(String emergency, String startTime, String endTime,
			String remarks, String nextRequest, String contactRequest,
			String _distance, String isdd, String iedd) {
		super();
		this.emergency = emergency;
		this.startTime = startTime;
		this.endTime = endTime;
		this.remarks = remarks;
		this.nextRequest = nextRequest;
		this.contactRequest = contactRequest;
		distance = _distance;
		ISDD = isdd;
		IEDD = iedd;
	}

	public EtcModel(String emergency, String startTime, String endTime,
			String remarks, String nextRequest, String contactRequest,
			String _distance) {
		super();
		this.emergency = emergency;
		this.startTime = startTime;
		this.endTime = endTime;
		this.remarks = remarks;
		this.nextRequest = nextRequest;
		this.contactRequest = contactRequest;
		distance = _distance;
	}

	public String getEmergency() {
		return emergency;
	}

	public void setEmergency(String emergency) {
		this.emergency = emergency;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getNextRequest() {
		return nextRequest;
	}

	public void setNextRequest(String nextRequest) {
		this.nextRequest = nextRequest;
	}

	public String getContactRequest() {
		return contactRequest;
	}

	public void setContactRequest(String contactRequest) {
		this.contactRequest = contactRequest;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getISDD() {
		return ISDD;
	}

	public void setISDD(String iSDD) {
		ISDD = iSDD;
	}

	public String getIEDD() {
		return IEDD;
	}

	public void setIEDD(String iEDD) {
		IEDD = iEDD;
	}

}
