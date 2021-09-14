package com.twfhclife.generic.model;

import javax.validation.constraints.Pattern;

public class QueryVo {

	//檢驗日期格式
	//@Pattern(regexp = "(?!0000)[0-9]{4}-((0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-8])|(0[13-9]|1[0-2])-(29|30)|(0[13578]|1[02])-31)", message = "日期格式不正確!(YYYY-MM-DD)")
	@Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "日期格式不正確!(YYYY-MM-DD)")
	private String startDate;

	//檢驗日期格式
	//@Pattern(regexp = "(?!0000)[0-9]{4}-((0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-8])|(0[13-9]|1[0-2])-(29|30)|(0[13578]|1[02])-31)", message = "日期格式不正確!(YYYY-MM-DD)")
	@Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "日期格式不正確!(YYYY-MM-DD)")
	private String endDate;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
