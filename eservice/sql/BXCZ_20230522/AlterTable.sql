USE ESERVICE;

ALTER TABLE ESERVICE.DBO.TRANS_INSURANCE_CLAIM ADD SIGN_AGREE NVARCHAR(5);

ALTER TABLE ESERVICE.DBO.TRANS_MEDICAL_TREATMENT_CLAIM ADD SIGN_AGREE NVARCHAR(5);

ALTER TABLE ESERVICE.DBO.BXCZ_API_LOG ADD ACTION_ID NVARCHAR(32);

CREATE UNIQUE INDEX UNIQUE_BXCZ_ACTION_ID ON ESERVICE.DBO.BXCZ_API_LOG(ACTION_ID);

CREATE UNIQUE INDEX UNIQUE_BXCZ_RECORD_ACTION_ID ON ESERVICE.DBO.BXCZ_SIGN_RECORD(ACTION_ID);

CREATE UNIQUE INDEX UNIQUE_BXCZ_SIGN_FILEDATA ON ESERVICE.DBO.BXCZ_SIGN_FILEDATA(FILE_ID);

ALTER TABLE ESERVICE.DBO.INSURANCE_CLAIM ADD SIGN_AGREE NVARCHAR(5);

ALTER TABLE ESERVICE.DBO.TRANS_MEDICAL_TREATMENT_CLAIM ADD TO_SUB_HOSPITAL_ID varchar(100);

ALTER TABLE ESERVICE.DBO.MEDICAL_TREATMENT_CLAIM ADD TO_SUB_HOSPITAL_ID varchar(100);

