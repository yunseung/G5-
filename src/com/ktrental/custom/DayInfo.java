package com.ktrental.custom;

/**
 * �Ϸ��� ��¥������ �����ϴ� Ŭ����
 * 
 * @author croute
 * @since 2011.03.08
 */
public class DayInfo
{
    
    public String getYear()
        {
        return year;
        }

    
    public void setYear(String year)
        {
        this.year = year;
        }

    
    public String getMonth()
        {
        return month;
        }

    
    public void setMonth(String month)
        {
        this.month = month;
        }

    private String year;
    private String month;
	private String day;
	private boolean inMonth;
	
	/**
	 * ��¥�� ��ȯ�Ѵ�.
	 * 
	 * @return day ��¥
	 */
	public String getDay()
	{
		return day;
	}

	/**
	 * ��¥�� �����Ѵ�.
	 * 
	 * @param day ��¥
	 */
	public void setDay(String day)
	{
		this.day = day;
	}

	/**
	 * �̹���� ��¥���� ������ ��ȯ�Ѵ�.
	 * 
	 * @return inMonth(true/false)
	 */
	public boolean isInMonth()
	{
		return inMonth;
	}

	/**
	 * �̹���� ��¥���� ������ �����Ѵ�.
	 * 
	 * @param inMonth(true/false)
	 */
	public void setInMonth(boolean inMonth)
	{
		this.inMonth = inMonth;
	}

}