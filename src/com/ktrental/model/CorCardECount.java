package com.ktrental.model;

public class CorCardECount {

	private String E_SUM;
	private String E_COUNT;
	private String E_PAGE;

	public CorCardECount(String E_SUM, String E_COUNT, String E_PAGE) {
		super();
		setE_SUM(E_SUM);
		setE_COUNT(E_COUNT);
		setE_PAGE(E_PAGE);
	}

	public String getE_SUM() {
		return E_SUM;
	}

	public void setE_SUM(String e_SUM) {
		E_SUM = e_SUM;
	}

	public String getE_COUNT() {
		return E_COUNT;
	}

	public void setE_COUNT(String e_COUNT) {
		E_COUNT = e_COUNT;
	}

	public String getE_PAGE() {
		return E_PAGE;
	}

	public void setE_PAGE(String e_PAGE) {
		E_PAGE = e_PAGE;
	}

}
