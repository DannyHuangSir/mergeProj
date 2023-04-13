package com.twfhclife.eservice.api.elife.domain;

import com.google.gson.annotations.SerializedName;

public class StreetNameDataVo {
	@SerializedName("street_name")
	private String streetName;

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
}
