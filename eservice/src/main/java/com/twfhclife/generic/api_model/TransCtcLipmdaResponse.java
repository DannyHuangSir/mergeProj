package com.twfhclife.generic.api_model;

import java.io.Serializable;
import java.util.List;

public class TransCtcLipmdaResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<TransCtcLipmdaVo> lipmdaList ;

	public List<TransCtcLipmdaVo> getLipmdaList() {
		return lipmdaList;
	}

	public void setLipmdaList(List<TransCtcLipmdaVo> lipmdaList) {
		this.lipmdaList = lipmdaList;
	}


	
}
