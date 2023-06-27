package com.twfhclife.adm.model;

/**
 * Spec v1.5.2
 *
 */
public enum MedicalTreatmentClaimFileTypeEnum {
	CertificateDiagnosis("CertificateDiagnosis","診斷證明書"),
	Receipt("Receipt","費用明細"),
	DischargeSummary("opdData","門診"),
	MedicalImage("MedicalImage","醫學影像"),
	Pathlogy("Pathlogy","病理檢查"),
	Surgery("Surgery","手術資料"),
	Hospitalization("Hospitalization","住院"),
	Emergency("Emergency","急診");
	
	MedicalTreatmentClaimFileTypeEnum(String dType,String dTypeCht){
		this.dType = dType;
		this.dTypeCht = dTypeCht;
	}
	
	public static MedicalTreatmentClaimFileTypeEnum getMedicalTreatmentClaimFileTypeEnum(String dType) {
		for(MedicalTreatmentClaimFileTypeEnum fileType : values()) {
			if(fileType.dType.equals(dType)) {
				return fileType;
			}
		}
		return null;
	}
	
	private final String dType;
	
	private final String dTypeCht;

	public String getdType() {
		return dType;
	}

	public String getdTypeCht() {
		return dTypeCht;
	}
}
