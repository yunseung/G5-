package com.ktrental.model;

import com.ktrental.util.kog;

public class DbQueryModel {
	private String tableName;
	private String[] whereCause;
	private String[] whereArgs;
	private String[] colums;
	private String orderBy;

	private boolean notFlag = false;

	public DbQueryModel(String _tableName, String[] _whereCause,
			String[] _whereArgs, String[] _coulums) {
		kog.e("KDH", "DbQueryModel _tableName = "+_tableName);
		tableName = _tableName;
		whereCause = _whereCause;
		whereArgs = _whereArgs;
		colums = _coulums;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String _tableName) {
		this.tableName = _tableName;
	}

	public String[] getWhereCause() {
		return whereCause;
	}

	public void setWhereCause(String[] _whereCause) {
		this.whereCause = _whereCause;
	}

	public String[] getWhereArgs() {
		return whereArgs;
	}

	public void setWhereArgs(String[] _whereArgs) {
		this.whereArgs = _whereArgs;
	}

	public String[] getColums() {
		return colums;
	}

	public void setColums(String[] colums) {
		this.colums = colums;
	}

	public boolean isNotFlag() {
		return notFlag;
	}

	public void setNotFlag(boolean notFlag) {
		this.notFlag = notFlag;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
}
