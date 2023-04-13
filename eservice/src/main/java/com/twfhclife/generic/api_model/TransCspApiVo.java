package com.twfhclife.generic.api_model;


public class TransCspApiVo {
	private ReturnHeader returnHeader;
	private Result result;
	
	public ReturnHeader getReturnHeader() {
		return returnHeader;
	}
	public void setReturnHeader(ReturnHeader returnHeader) {
		this.returnHeader = returnHeader;
	}
	
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}

	class Result{
		private CspJson cspJson;

		public CspJson getCspJson() {
			return cspJson;
		}

		public void setCspJson(CspJson cspJson) {
			this.cspJson = cspJson;
		}
		
	}
	
	class CspJson{
		private String success;
		private DataDetail data;
		public String getSuccess() {
			return success;
		}
		public void setSuccess(String success) {
			this.success = success;
		}
		public DataDetail getData() {
			return data;
		}
		public void setData(DataDetail data) {
			this.data = data;
		}
		
	}

	class DataDetail{
		private String token ;
		private String detail_status ;
		private String detail_message ;
		private String values ;
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public String getDetail_status() {
			return detail_status;
		}
		public void setDetail_status(String detail_status) {
			this.detail_status = detail_status;
		}
		public String getDetail_message() {
			return detail_message;
		}
		public void setDetail_message(String detail_message) {
			this.detail_message = detail_message;
		}
		public String getValues() {
			return values;
		}
		public void setValues(String values) {
			this.values = values;
		}
		
	}


	

	
}