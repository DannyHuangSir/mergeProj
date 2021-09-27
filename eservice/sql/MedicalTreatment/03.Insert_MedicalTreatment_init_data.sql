
--ESERVICE.dbo.HOSPITAL INIT DATA
insert into ESERVICE.dbo.HOSPITAL
(ID,HP_ID,HP_NAME,FUNCTION_NAME,STATUS)
values
(
	(next value for HOSPITAL_CLAIM_FD_SEQ),
	'1101020018',
	'國泰醫療財團法人國泰綜合醫院',
	'MEDICAL_TREATMENT',
	'Y'
);

insert into ESERVICE.dbo.HOSPITAL
(ID,HP_ID,HP_NAME,FUNCTION_NAME,STATUS)
values
(
	(next value for HOSPITAL_CLAIM_FD_SEQ),
	'0101090517',
	'臺北市立聯合醫院',
	'MEDICAL_TREATMENT',
	'Y'
);

insert into ESERVICE.dbo.HOSPITAL
(ID,HP_ID,HP_NAME,FUNCTION_NAME,STATUS)
values
(
	(next value for HOSPITAL_CLAIM_FD_SEQ),
	'1101150011',
	'新光醫療財團法人新光吳火獅紀念醫院',
	'MEDICAL_TREATMENT',
	'Y'
);

insert into ESERVICE.dbo.HOSPITAL
(ID,HP_ID,HP_NAME,FUNCTION_NAME,STATUS)
values
(
	(next value for HOSPITAL_CLAIM_FD_SEQ),
	'1301200010',
	'臺北市立萬芳醫院',
	'MEDICAL_TREATMENT',
	'Y'
);

GO


--ESERVICE.dbo.HOSPITAL_INSURANCE_COMPANY INIT DATA
insert into ESERVICE.dbo.HOSPITAL_INSURANCE_COMPANY
(ID,INSURANCE_ID,INSURANCE_NAME,FUNCTION_NAME,STATUS)
values
(
	(next value for HOSPITAL_INSURANCE_COMPANY_CLAIM_SEQ),
	'L16',
	'遠雄人壽',
	'MEDICAL_TREATMENT',
	'Y'
);

insert into ESERVICE.dbo.HOSPITAL_INSURANCE_COMPANY
(ID,INSURANCE_ID,INSURANCE_NAME,FUNCTION_NAME,STATUS)
values
(
	(next value for HOSPITAL_INSURANCE_COMPANY_CLAIM_SEQ),
	'L64',
	'全球人壽',
	'MEDICAL_TREATMENT',
	'Y'
);

insert into ESERVICE.dbo.HOSPITAL_INSURANCE_COMPANY
(ID,INSURANCE_ID,INSURANCE_NAME,FUNCTION_NAME,STATUS)
values
(
	(next value for HOSPITAL_INSURANCE_COMPANY_CLAIM_SEQ),
	'N15',
	'國泰世紀產險',
	'MEDICAL_TREATMENT',
	'Y'
);

insert into ESERVICE.dbo.HOSPITAL_INSURANCE_COMPANY
(ID,INSURANCE_ID,INSURANCE_NAME,FUNCTION_NAME,STATUS)
values
(
	(next value for HOSPITAL_INSURANCE_COMPANY_CLAIM_SEQ),
	'L52',
	'富邦人壽',
	'MEDICAL_TREATMENT',
	'Y'
);

insert into ESERVICE.dbo.HOSPITAL_INSURANCE_COMPANY
(ID,INSURANCE_ID,INSURANCE_NAME,FUNCTION_NAME,STATUS)
values
(
	(next value for HOSPITAL_INSURANCE_COMPANY_CLAIM_SEQ),
	'N05',
	'富邦產險',
	'MEDICAL_TREATMENT',
	'Y'
);

insert into ESERVICE.dbo.HOSPITAL_INSURANCE_COMPANY
(ID,INSURANCE_ID,INSURANCE_NAME,FUNCTION_NAME,STATUS)
values
(
	(next value for HOSPITAL_INSURANCE_COMPANY_CLAIM_SEQ),
	'L02',
	'台灣人壽',
	'MEDICAL_TREATMENT',
	'Y'
);

insert into ESERVICE.dbo.HOSPITAL_INSURANCE_COMPANY
(ID,INSURANCE_ID,INSURANCE_NAME,FUNCTION_NAME,STATUS)
values
(
	(next value for HOSPITAL_INSURANCE_COMPANY_CLAIM_SEQ),
	'L05',
	'中國人壽',
	'MEDICAL_TREATMENT',
	'Y'
);

insert into ESERVICE.dbo.HOSPITAL_INSURANCE_COMPANY
(ID,INSURANCE_ID,INSURANCE_NAME,FUNCTION_NAME,STATUS)
values
(
	(next value for HOSPITAL_INSURANCE_COMPANY_CLAIM_SEQ),
	'L08',
	'新光人壽',
	'MEDICAL_TREATMENT',
	'Y'
);

insert into ESERVICE.dbo.HOSPITAL_INSURANCE_COMPANY
(ID,INSURANCE_ID,INSURANCE_NAME,FUNCTION_NAME,STATUS)
values
(
	(next value for HOSPITAL_INSURANCE_COMPANY_CLAIM_SEQ),
	'L06',
	'南山人壽',
	'MEDICAL_TREATMENT',
	'Y'
);

insert into ESERVICE.dbo.HOSPITAL_INSURANCE_COMPANY
(ID,INSURANCE_ID,INSURANCE_NAME,FUNCTION_NAME,STATUS)
values
(
	(next value for HOSPITAL_INSURANCE_COMPANY_CLAIM_SEQ),
	'L03',
	'保誠人壽',
	'MEDICAL_TREATMENT',
	'Y'
);

insert into ESERVICE.dbo.HOSPITAL_INSURANCE_COMPANY
(ID,INSURANCE_ID,INSURANCE_NAME,FUNCTION_NAME,STATUS)
values
(
	(next value for HOSPITAL_INSURANCE_COMPANY_CLAIM_SEQ),
	'L61',
	'元大人壽',
	'MEDICAL_TREATMENT',
	'Y'
);

insert into ESERVICE.dbo.HOSPITAL_INSURANCE_COMPANY
(ID,INSURANCE_ID,INSURANCE_NAME,FUNCTION_NAME,STATUS)
values
(
	(next value for HOSPITAL_INSURANCE_COMPANY_CLAIM_SEQ),
	'L21',
	'第一金人壽',
	'MEDICAL_TREATMENT',
	'Y'
);

insert into ESERVICE.dbo.HOSPITAL_INSURANCE_COMPANY
(ID,INSURANCE_ID,INSURANCE_NAME,FUNCTION_NAME,STATUS)
values
(
	(next value for HOSPITAL_INSURANCE_COMPANY_CLAIM_SEQ),
	'L04',
	'國泰人壽',
	'MEDICAL_TREATMENT',
	'Y'
);

insert into ESERVICE.dbo.HOSPITAL_INSURANCE_COMPANY
(ID,INSURANCE_ID,INSURANCE_NAME,FUNCTION_NAME,STATUS)
values
(
	(next value for HOSPITAL_INSURANCE_COMPANY_CLAIM_SEQ),
	'L11',
	'三商美邦人壽',
	'MEDICAL_TREATMENT',
	'Y'
);
go