package com.twfhclife.generic.api_model;

import java.io.Serializable;
import java.util.List;

public class TransCtcLilipmResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<TransCtcLilipmVo> ctcLilipmListVo;

	public List<TransCtcLilipmVo> getCtcLilipmListVo() {
		return ctcLilipmListVo;
	}

	public void setCtcLilipmListVo(List<TransCtcLilipmVo> ctcLilipmListVo) {
		this.ctcLilipmListVo = ctcLilipmListVo;
	}

}
