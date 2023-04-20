package com.twfhclife.eservice.api_model;

import java.util.List;

public class PageResponseObj<T> {

	/** 目前頁數 */
	public int page;

	/** 總頁數 */
	public int totalPage;

	/** 資料總筆數 */
	public int totalRecords;

	/** 資料查詢集合 */
	public List<T> pageData;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<T> getPageData() {
		return pageData;
	}

	public void setPageData(List<T> pageData) {
		this.pageData = pageData;
	}

}
