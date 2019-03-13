package com.ktrental.cm.connect;

import com.ktrental.cm.util.PrintLog;
import com.ktrental.common.KtRentalApplication;
import com.ktrental.util.kog;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import phos.android.client.Column;
import phos.android.client.ParameterList;
import phos.android.client.PhosCrypto;
import phos.android.client.Row;
import phos.android.client.Structure;
import phos.android.client.Table;

public class ConnectorUtil {
	private static final String TAG = "Connector";

	public static void setParameter(String strKey, String strValue,
			ParameterList request) {
		if (strKey.equals("") || strKey.length() <= 0) {
			PrintLog.Print("", "strKey ????.");
			return;
		}

		if (strValue == null){
			PrintLog.Print("", "============================================================================================");
			PrintLog.Print("", strKey + "  VALUE null !!!!");
			PrintLog.Print("", "============================================================================================");
			strValue = "";
		}
		if (strValue.equals("") || strValue.length() <= 0) {
			PrintLog.Print("", "strValue  ????.");
			return;
		}

		// checkRequestParam();

		PrintLog.Print(TAG, "setParameter " + strKey + "::" + strValue);
		String strEnccypt = encrypt(strValue);
		request.addInputParameter(strKey, strEnccypt);

	}

	public static void setParameterNonEnCrypt(String strKey, String strValue,
									ParameterList request) {
		if (strKey.equals("") || strKey.length() <= 0) {
			PrintLog.Print("", "strKey ????.");
			return;
		}

		if (strValue == null){
			PrintLog.Print("", "============================================================================================");
			PrintLog.Print("", strKey + "  VALUE null !!!!");
			PrintLog.Print("", "============================================================================================");
			strValue = "";
		}
		if (strValue.equals("") || strValue.length() <= 0) {
			PrintLog.Print("", "strValue  ????.");
			return;
		}

		// checkRequestParam();

		PrintLog.Print(TAG, "setParameter " + strKey + "::" + strValue);
//		String strEnccypt = encrypt(strValue);
		request.addInputParameter(strKey, strValue);

	}

	public static void setStructure(String strStructureName,
			HashMap<String, String> aStructure, ParameterList request) {
		int nCnt = 0;
		try {
			if(aStructure != null) {
				nCnt = aStructure.size();
			}
			if (nCnt <= 0) {
				PrintLog.Print("", "aStructure ????.");
				return;
			}

			if (strStructureName.equals("") || strStructureName.length() <= 0) {
				PrintLog.Print("", "Structure  ????.");
				return;
			}

			Structure structure = new Structure(strStructureName); // Structure ??

			Iterator<String> it = aStructure.keySet().iterator();

			while (it.hasNext()) {
				String strKey = "";
				String strValue = "";

				strKey = it.next();
				strValue = aStructure.get(strKey);

				PrintLog.Print(TAG, "setStructure " + strKey + "::" + strValue);

				String strEnccypt = encrypt(strValue);
				structure.addParameter(strKey, strEnccypt);

			}
			request.addStructure(structure);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void setTable(String strTableName,
			ArrayList<HashMap<String, String>> aTable, ParameterList request) {
		if (aTable == null) {
			PrintLog.Print("", "aTable null ");
			return;
		}

		if (strTableName == null) {
			PrintLog.Print("", "strTableName null ");
			return;
		}

		int nCnt = 0;
		nCnt = aTable.size();
		PrintLog.Print("", "aTable size = " + nCnt);

		if (nCnt <= 0) {
			return;
		}

		PrintLog.Print("", "strTableName ");

		if (strTableName.equals("") || strTableName.length() <= 0) {
			return;
		}

		Table table = new Table(strTableName); // Table ??

		for (int i = 0; i < nCnt; i++) {
			HashMap<String, String> aMap = new HashMap<String, String>();

			aMap = aTable.get(i);

			Iterator<String> itMap = aMap.keySet().iterator();

			table.appendRow(); // ?? ???

			while (itMap.hasNext()) {

				String strKey = "";
				String strValue = "";

				strKey = itMap.next();
				strValue = aMap.get(strKey);
				PrintLog.Print(TAG, "setTable " + strKey + "::" + strValue);
				strValue = encrypt(strValue);

				table.setColumn(strKey, strValue);
			}

		}
		request.addTable(table);
	}

	public static String getParameter(String strKey, ParameterList response) {
		String strValue = "";

		if (strKey.equals("") || strKey.length() <= 0) {
			PrintLog.Print("", "strKey ????.");
			return "";
		}

		if (response == null) {
			PrintLog.Print("", "response ????.");
			return "";
		}

        strValue = decrypt(response.getValue(strKey));

		PrintLog.Print(TAG, "getParameter " + strValue);

		return strValue;
	}

	public static HashMap<String, String> getStructure(String strName,
			ParameterList response, boolean isDecrypt) {
		HashMap<String, String> resMap = new HashMap<String, String>();
		
		kog.e("Jonathan", "����� ConnectorUtil.getStructure1");

		if (strName.equals("") || strName.length() <= 0) {
			PrintLog.Print("", "strName ????.");
			return resMap;
		}

		if (response == null) {
			PrintLog.Print("", "response ????.");
			return resMap;
		}

		Structure struct = response.getStructure(strName);

		if (struct == null)
			return null;

		if (struct.getAllBytes() < 0) {
			PrintLog.Print("", "Structure ????.");
			return resMap;
		}

		ArrayList<Column> aColumn = struct.getScalarData();

		int nCnt = aColumn.size();

		for (int i = 0; i < nCnt; i++) {
			Column col = aColumn.get(i);

			String strKey = col.getName();
			String strValue = null;

			if (isDecrypt) {
                strValue = decrypt(col.getValue());
			} else {
				if (KtRentalApplication.isEncoding(strKey))
					strValue = col.getValue();
				else {
                    strValue = decrypt(col.getValue());
				}
			}

			PrintLog.Print(TAG, "getStructure " + strName + " " + strKey + "::"	+ strValue);

			resMap.put(strKey, strValue);

		}

		return resMap;
	}

	public static ArrayList<HashMap<String, String>> getTable(String strName,
			ParameterList response, boolean isDecrypt) {
		ArrayList<HashMap<String, String>> resArray = new ArrayList<HashMap<String, String>>();

		if (strName.equals("") || strName.length() <= 0) {
			PrintLog.Print("", "strName ????.");
			return resArray;
		}

		if (response == null) {
			PrintLog.Print("", "response ????.");
			return resArray;
		}

		Table table = response.getTable(strName);

		if (table == null)
			return null;

		int nCnt = table.getNumRow();

		if (nCnt <= 0) {
			PrintLog.Print("", "Table ????.");
			return resArray;
		}

		PrintLog.Print(TAG, "Table Count = " + nCnt);

		for (int i = 0; i < nCnt; i++) {
			Row row = table.getRow(i);

			ArrayList<Column> aColumn = row.getColumns();

			int nColumnCnt = aColumn.size();

			HashMap<String, String> mapColumn = new HashMap<String, String>();

			for (int j = 0; j < nColumnCnt; j++) {
				String strKey = aColumn.get(j).getName();
				String strValue = null;

				if (isDecrypt) {
                    strValue = decrypt(aColumn.get(j).getValue());
				} else {
					if (KtRentalApplication.isEncoding(strKey))
						strValue = aColumn.get(j).getValue();
					else {
                        strValue = decrypt(aColumn.get(j).getValue());
					}
				}

				// strValue = strValue.replace("(", ""); // this is what I
				// mean...
				// strValue = strValue.replace(")", "");

				// if (strValue.length() == 0)
				// PrintLog.Print(TAG, "getTable " + strKey + "::" + strValue);

				// if(strKey.equals("OILTYPNM")){
				//
				// Log.d("HONG",""+strValue);
				//
				// }

				if (strValue == null)
					strValue = "";
				mapColumn.put(strKey, strValue);
			}

			resArray.add(mapColumn);
		}

		PrintLog.Print(TAG, "Table Count end");

		return resArray;
	}

	// public static String decrypt(String strData) {
	// String strValue = "";
	//
	// // PrintLog.Print("", strData);
	//
	// strValue = PhosCrypto.decrypt(strData);
	//
	//
	//
	// // PrintLog.Print("", strValue);
	//
	// return strValue;
	// }

	public static String encrypt(String strData) {
		String strValue = "";
		if (strData == null)
			strData = "";
		strValue = PhosCrypto.encrypt(strData);
		return strValue;
	}

	public static String decrypt(String src) {

		String decrypted = "";
		if("0".startsWith(src))
		{
			decrypted = src;
			return decrypted;
		}
		try {
			decrypted = new String(cipher.doFinal(hexStringToByteArray(src)));
		} catch (Exception e) {
			// decrypted = "[ERROR] INVALID KEY.";
			decrypted = src;
		}
		return decrypted;
	}

	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0)
			return null;
		byte ba[] = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++)
			ba[i] = (byte) Integer
					.parseInt(hex.substring(2 * i, 2 * i + 2), 16);

		return ba;
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public static void init() {
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			SecretKeySpec newKey = new SecretKeySpec(secretKey.getBytes(), "AES");
			cipher.init(Cipher.DECRYPT_MODE, newKey);
//			cipher.init(2, skeySpec);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	public static String key = "_KTRENTAL!";
	public static String secretKey = (new StringBuilder(String.valueOf(sdf
			.format(new Date())))).append(key).toString();

	public static SecretKeySpec skeySpec = new SecretKeySpec(
			secretKey.getBytes(), "AES");
	public static Cipher cipher;
}
