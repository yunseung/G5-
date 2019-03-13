package com.ktrental.cm.util;

import android.util.Log;

public class PrintLog {
	// �α� Ȱ��ȭ ����
	private static boolean LOGGING_ENABLED = true;

	// ���� Ŭ������, ��Ǹ�, ���γѹ��� ������� ������ 5�� �����ؾ� ��.
	private static final int STACK_TRACE_LEVELS = 5;

	public static void Print(String tag, String message)
    {
		if (LOGGING_ENABLED)
		{
			Log.e(tag, getClassNameMethodNameAndLineNumber() + message);
			
//			{INPML=40464, NIELS= , EXHIB_TX= , MATMA= , MOB_NUMBER2=0b936a4336a91a920803aa1ab75e6ae6, GUEBG2= , USRID==>000000000001NH, VKGRP_TX= , INVNR=35허6344, PMFRE_TX= , PARNR2= , PARNR2_TX= , EMCAL= , CYCMN= , NIELS_NM= , EXHIB= , PERNR_TX= , DRV_MOB=2d0f48b70fbf7fbccd84584d35a5186d7e5d3828e4d0edbbb4067239ccbb85c9, NOTIR_TX= , BKTIR= , LNTMA= , VKGRP= , LNTMA_TX= , GENMA= , GENMA_TX= , MATMA_TX= , POST_CODE=135-280, KUNNR_NM= , INNAM= , CITY=서울 강남구 대치동, CEMER= , VBELN= , CTRTY=4, VKGRP1_TX= , GSTRS=20130831, KUNNR= , DRIVN=윤성열, MAKTX=K5 (L) 2.0 렌터카 디럭스, STREET=890-59, LDATE=20110831, EQUNR=10000046, SNWTR= , CYCMN_TX= , CARCD=1KI007L2011, TIRFR= , GSUZS=000000, BSARK= , ZSEQ=1, CCMSTS=E0001, GUEEN2= , TIRBK= , CUD=C, PMFRE= , AUFNR=4400001560, EMCAL_TX= , JOINDT= , BSYM=201308, VKGRP1= , PERNR= , CCMRQ= , NOTIR= , ENAME=박선철, GUEEN= , DRV_TEL=2d0f48b70fbf7fbccd84584d35a5186d7e5d3828e4d0edbbb4067239ccbb85c9, BSARK_TX= , MPERNR=70064}
		}
    }

	/**
	 * Func Desc :�α׸� ���� ���� �ѹ��� ��ȯ�Ѵ�. Input Param : Return : SUCC :
	 * ���� �ѹ� FAIL : �ۼ��� : 2013.06.25 �ۼ��� : �迵��
	 */
	private static int getLineNumber() {
		return Thread.currentThread().getStackTrace()[STACK_TRACE_LEVELS]
				.getLineNumber();
	}

	/**
	 * Func Desc :�α׸� ���� Ŭ�������� ��ȯ�Ѵ�. Input Param : Return : SUCC :
	 * .java �� ������ Ŭ���� �� ��Ʈ�� FAIL : �ۼ��� : 2013.06.25 �ۼ��� : �迵��
	 */
	private static String getClassName() {
		return "";
	}
	/*
	private static String getClassName() {
		String fileName = Thread.currentThread().getStackTrace()[STACK_TRACE_LEVELS]
				.getFileName();

		return fileName.substring(0, fileName.length() - 5);
	}
	*/
	

	/**
	 * Func Desc :�α׸� ���� ��Ǹ��� ��ȯ�Ѵ�. Input Param : Return : SUCC : ��Ǹ�
	 * ��Ʈ��. FAIL : �ۼ��� : 2013.06.25 �ۼ��� : �迵��
	 */
	
	private static String getMethodName() {
		return "";
	}
	/*
	private static String getMethodName() {
		return Thread.currentThread().getStackTrace()[STACK_TRACE_LEVELS]
				.getMethodName();
	}
	*/

	/**
	 * Func Desc : Ŭ������, ��Ǹ�, ���γѹ��� ��ȯ�Ѵ�. Input Param : Return : SUCC :
	 * Ŭ������, ��Ǹ�, ���γѹ� ��Ƽ�� FAIL : �ۼ��� : 2013.06.25 �ۼ��� : �迵��
	 */
	/*
	private static String getClassNameMethodNameAndLineNumber() {
		return "[" + getClassName() + ":" + getMethodName() + ":"
				+ getLineNumber() + "]: ";
	}
	*/
	
	private static String getClassNameMethodNameAndLineNumber() {
		return "";
	}
	
}
