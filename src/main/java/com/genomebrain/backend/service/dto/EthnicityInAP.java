package com.genomebrain.backend.service.dto;

public enum EthnicityInAP {

	ASHKENAZI_JEWISH("Ashkenazi Jewish"),
	ASIAN("Asian"),
	AFRICAN_AMERICAN("African American"),
	FRENCH_CANADIAN("French Canadian"),
	HISPANIC("Hispanic"),
	MEDITERRANEAN("Mediterranean"),
	MIDDLE_EASTERN("Middle Eastern"),
	NATIVE_AMERICAN("Native American"),
	PACIFIC_ISLANDER("Pacific Islander"),
	PORTUGUESE("Portuguese"),
	CAUCASIAN("Caucasian"),
	OTHER("Other"),
	UNKNOWN("Unknown"),
	EAST_ASIAN("East Asian"),
	SOUTH_ASIAN("South Asian"),
	SOUTH_EAST_ASIAN("South-East Asian"),
	FINNISH("Finnish"),
	NORTHERN_EUROPEAN("Northern European"),
	SOUTHERN_EUROPEAN("Southern European");

	private final String ethnicityInAp;

	EthnicityInAP(String ethnicityInApp){
		this.ethnicityInAp = ethnicityInApp;
	}

	public String getEthnicityInAp() {
		return ethnicityInAp;
	}
}
