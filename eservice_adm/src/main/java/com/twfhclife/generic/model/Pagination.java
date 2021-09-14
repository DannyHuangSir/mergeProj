package com.twfhclife.generic.model;

public class Pagination extends QueryVo {

	/** 資料開始筆數(datatable)  */
	private int start;

	/** 每頁筆數(datatable) */
	private int length;

	/** 每頁筆數(jqGrid) */
	private int rows;

	/** 目前頁數(jqGrid)  */
	private int page;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
}
