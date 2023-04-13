package com.twfhclife.generic.util;

import com.microsoft.sqlserver.jdbc.StringUtils;
import com.twfhclife.generic.api_model.TransCtcSelectDataAddCodeVo;

public class TransRulesUtil {

	public static String verifyPolicyRuleByRenewAndReduce(TransCtcSelectDataAddCodeVo vo) {
		String msg = "";
		String no = vo.getLipmInsuNo();
		String fatca = vo.getProdIsFatca();
		String rcpCodes = vo.getLipmRcpCode();
		if ("1".equals(vo.getProdIsFatca()) && !"T".equals(vo.getLipmRcpCode())) {
			if ("A".equals(vo.getLipmRcpCode()) && "Y".equals(vo.getLipmFlexRcpMk())) {
				msg = "Y";
			} else {
				String st = vo.getLipmSt();
				String mk = vo.getLipmLoMk();
				if (Integer.valueOf(vo.getLipmSt()) < 12 && (StringUtils.isEmpty(vo.getLipmLoMk()) || "N".equals(vo.getLipmLoMk()))) {
					if (Integer.valueOf(vo.getLipmSt()) == 00) {
						String lipmRcpCode = "";
						lipmRcpCode = "N".equals(vo.getLipmRcpCode()) || StringUtils.isEmpty(vo.getLipmOriRcpCode())
								? vo.getLipmRcpCode() : vo.getLipmOriRcpCode();
						if (((Integer.valueOf(vo.getLipiTredRcpTms())
								/ Integer.valueOf(getLipmRcpCodeType(lipmRcpCode))) < Integer
										.valueOf(vo.getLipiPremYear()))) {
							int tms = Integer.valueOf(vo.getLipiTredRcpTms());
							int rcpCode = Integer.valueOf(getLipmRcpCodeType(lipmRcpCode));
							int year = Integer.valueOf(vo.getLipiPremYear());
							msg = "N";
						} else {
							msg = "Y";
						}
					} else {
						msg = "N";
					}
				}else {
					msg = "Y";
				}
			}
		} else {
			msg = "Y";
		}
		return msg;
	}

	private static String getLipmRcpCodeType(String lipmRcpCode) {
		switch (lipmRcpCode) {
		case "A":
			return "1";
		case "S":
			return "2";
		case "Q":
			return "4";
		case "M":
			return "12";
		default:
			return "";
		}

	}
}
