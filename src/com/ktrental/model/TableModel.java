package com.ktrental.model;

import java.util.ArrayList;
import java.util.HashMap;

public class TableModel {
	private String tableName;
	private String[] tableNames;
	private HashMap<String, String> structTableNameMap = new HashMap<String, String>();

	private ArrayList<HashMap<String, String>> mTableArray = null;
	private HashMap<String, HashMap<String, String>> mStructMap;

	public HashMap<String, ArrayList<HashMap<String, String>>> mTableArrayMap = new HashMap<String, ArrayList<HashMap<String, String>>>();

	private String value;

	private HashMap<String, String> mResponseMap = new HashMap<String, String>();

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public TableModel(String _tableName) {
		tableName = _tableName;
	}

	public TableModel(String value, HashMap<String, String> map) {
		this.value = value;
		mResponseMap = map;
	}

	public TableModel(String aTableName,
			ArrayList<HashMap<String, String>> tableArray, String value) {
		tableName = aTableName;
		mTableArray = tableArray;
		this.value = value;
	}

	public TableModel(String aTableName,
			ArrayList<HashMap<String, String>> tableArray,
			HashMap<String, HashMap<String, String>> aStruct,
			HashMap<String, String> map, String value) {
		tableName = aTableName;
		mTableArray = tableArray;
		mStructMap = aStruct;
		this.value = value;
		mResponseMap = map;

		// for (int i = 0; i < _structTableModels.length; i++) {
		// structTableNameMap.put(_structTableModels, value)
		// }
		//
		// structTableNameMap = _structTableModels;
	}

	public TableModel(String[] aTableNames,
			HashMap<String, ArrayList<HashMap<String, String>>> tableArrayMap,
			HashMap<String, HashMap<String, String>> aStruct,
			HashMap<String, String> map, String value) {
		tableNames = aTableNames;
		mTableArrayMap = tableArrayMap;
		mStructMap = aStruct;
		mResponseMap = map;
		this.value = value;
		// for (int i = 0; i < _structTableModels.length; i++) {
		// structTableNameMap.put(_structTableModels, value)
		// }
		//
		// structTableNameMap = _structTableModels;
	}

	public String getStructTableName(String structTableName) {

		if (structTableNameMap != null)
			structTableName = structTableNameMap.get(structTableName);
		return structTableName;
	}

	public void setStructTableName(HashMap<String, String> structTableName) {
		this.structTableNameMap = structTableName;
	}

	public HashMap<String, String> getStruct(String structName) {

		HashMap<String, String> struct = null;

		if (mStructMap != null) {
			if (structTableNameMap != null
					&& structTableNameMap.containsKey(structName))
				structName = structTableNameMap.get(structName);

			struct = mStructMap.get(structName);
		}
		return struct;
	}

	public void setStruct(HashMap<String, HashMap<String, String>> aStruct) {
		this.mStructMap = aStruct;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public ArrayList<HashMap<String, String>> getTableArray() {
		return mTableArray;
	}

	public void setTableArray(ArrayList<HashMap<String, String>> aTableArray) {
		this.mTableArray = aTableArray;
	}

	public ArrayList<HashMap<String, String>> getTableArray(String tableName) {

		ArrayList<HashMap<String, String>> tableArray = mTableArrayMap
				.get(tableName);

		return tableArray;
	}

	public void setTableArray(String tableName,
			ArrayList<HashMap<String, String>> tableArray) {
		mTableArrayMap.put(tableName, tableArray);
	}

	public String getResponse(String strKey) {
		String reVal = null;

		if (mResponseMap != null) {
			if (mResponseMap.containsKey(strKey))
				reVal = mResponseMap.get(strKey);
		}

		return reVal;
	}

}
