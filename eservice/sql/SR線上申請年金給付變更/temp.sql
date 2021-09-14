--ESERVICE
select * from TRANS_RULES where policy_type in ('UP','UQ','UR') order by policy_type,trans_name;

--AG、BG、UD、UJ、UL改為年給付或半年給付，可讓保戶選擇保證期間為10年、15年、20年
select * from TRANS_RULES where policy_type in ('AG') order by policy_type,trans_name;

select * from TRANS_RULES where policy_type in ('AG','BG','UD','UJ','UL') order by policy_type,trans_name;

Insert into ESERVICE.TRANS_RULES (TRANS_TYPE,TRANS_NAME,POLICY_TYPE,TEXT1,TEXT2,TEXT3,TEXT4,DENY_MSG) values ('GUARANTEE_PERIOD','保證期間','AG','10y、15y、20y','分期給付(年給付、半年給付)保證期間為10年、15年、20年',null,null,'您選擇的保單不提供申請變更保證期間!!');

--UP、UQ、UR改為年給付或半年給付，控制保證期間只有20年
select * from TRANS_RULES where policy_type in ('UP') order by policy_type,trans_name;
--select * from TRANS_RULES where policy_type in ('UP','UQ','UR') order by policy_type,trans_name;

T:一次給付
S:半年給付
A:年給付

--ESERVICE_ADM
select * from PARAMETER t where system_id='eservice' and parameter_category_id = 63;