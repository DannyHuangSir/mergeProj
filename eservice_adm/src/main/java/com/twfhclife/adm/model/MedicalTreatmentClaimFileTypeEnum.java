package com.twfhclife.adm.model;

/**
 * Spec v1.5.2
 *
 */
public enum MedicalTreatmentClaimFileTypeEnum {
	CERTIFICATEDIAGNOSIS("CertificateDiagnosis","診斷證明書"),
	RECEIPT("Receipt","費用明細"),
	DISCHARGESUMMARY("DischargeSummary","出院病摘"),
	MEDICALIMAGE("MedicalImage","醫學影像"),
	PATHLOGY("Pathlogy","病理檢查"),
	SURGERY("Surgery","手術資料"),
	HOSPITALIZATION("Hospitalization","住院"),
	EMERGENCY("Emergency","急診");
	
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
