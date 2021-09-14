package com.twfhclife.adm.domain;

import java.util.List;
import java.util.Map;

import com.twfhclife.adm.model.ParameterCategoryVo;

public class PageResponseObj {
	
	public static final String SUCCESS = "SUCCESS";
	
	public static final String ERROR = "ERROR";
	
	private String result;
	
	/** jquery datatable 資料查詢集合 */
	public int iTotalDisplayRecords;

	/** jquery datatable 資料總筆數 */
	public List<Map<String, Object>> aaData;
	
	/** jqGrid 目前頁數 */
	public int page;
	
	/** jqGrid 總頁數 */
	public int total;
	
	/** jqGrid 資料總筆數 */
	public int records;
	
	/** jqGrid 資料查詢集合 */
//	public List<?> rows;
	public List<Map<String, Object>> rows;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	public List<Map<String, Object>> getAaData() {
		return aaData;
	}

	public void setAaData(List<Map<String, Object>> aaData) {
		this.aaData = aaData;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}

	public List<Map<String, Object>> getRows() {
		return rows;
	}

	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}

}
