package com.twfhclife.jd.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分頁資訊物件.
 * 
 * @author alan
 */
public class PageInfoVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int totalRow;
	
	private int pageSize;
	
	private int pageNum;

	public int getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public boolean getPrev() {
		if (pageNum == 1) {
			return false;
		}
		
		List<Integer> pageNumList = getPageNumList();
		for (Integer currentPage : pageNumList) {
			if (currentPage < pageNum) {
				return true;
			}
		}
		
		return false;
	}

	public boolean getNext() {
		List<Integer> pageNumList = getPageNumList();
		for (Integer currentPage : pageNumList) {
			if (currentPage > pageNum) {
				return true;
			}
		}
		
		return false;
	}

	public List<Integer> getPageNumList() {
		List<Integer> pageNumList = new ArrayList<>();
		if (totalRow > 0 && pageSize > 0) {
			int totalPage = (totalRow / pageSize);
			if ((totalRow % pageSize != 0)) {
				totalPage++;
			}
			for (int i = 1; i <= totalPage; i++) {
				pageNumList.add(i);
			}
		}
		return pageNumList;
	}
}
