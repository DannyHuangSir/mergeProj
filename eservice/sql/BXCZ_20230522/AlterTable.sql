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

ALTER TABLE ESERVICE.DBO.MEDICAL_TREATMENT_CLAIM ADD SIGN_AGREE NVARCHAR(5);

ALTER TABLE ESERVICE.DBO.HOSPITAL_INSURANCE_COMPANY DROP CONSTRAINT HIC_PK

ALTER TABLE ESERVICE.DBO.HOSPITAL_INSURANCE_COMPANY ALTER COLUMN FUNCTION_NAME VARCHAR(50) NOT NULL

ALTER TABLE ESERVICE.DBO.HOSPITAL_INSURANCE_COMPANY ADD CONSTRAINT HIC_PK PRIMARY KEY(INSURANCE_ID, FUNCTION_NAME)

ALTER TABLE ESERVICE.DBO.TRANS_MEDICAL_TREATMENT_CLAIM ADD APPLY_DATE datetime;

ALTER TABLE ESERVICE.DBO.TRANS_INSURANCE_CLAIM ADD APPLY_DATE datetime;

ALTER TABLE ESERVICE.DBO.TRANS_INSURANCE_CLAIM ADD SEND_CONFIRM_TIME VARCHAR(14);

ALTER TABLE ESERVICE.DBO.TRANS_MEDICAL_TREATMENT_CLAIM ADD SEND_CONFIRM_TIME VARCHAR(14);

