package com.twfhclife.generic.constants;

/**
 * Definite return code.
 * 
 * @author All
 * @version 1.0
 */
public interface MsgCode {

	/**
	 * Success.
	 */
	String MSG_CODE_000 = "000";

	/**
	 * Success with warnings. ({0}).
	 */
	String MSG_CODE_002 = "002";

	/**
	 * Empty input error.
	 */
	String MSG_CODE_011 = "011";

	/** Input parameter error. ({ParameterName} {ErrorReason}). */
	String MSG_CODE_012 = "012";

	/** General Authentication or Authorization failure. */
	String MSG_CODE_013 = "013";

	/** Incomplete AuthInfo retrieved. */
	String MSG_CODE_014 = "014";

	/**
	 * {ServiceName} invocation timeout.
	 */
	String MSG_CODE_015 = "015";

	/**
	 * {ServiceName} invocation failure.
	 */
	String MSG_CODE_016 = "016";

	/**
	 * SQL Exception ({ERR_Code}).
	 */
	String MSG_CODE_017 = "017";

	/** IMDG Exception ({ERR_Code}). */
	String MSG_CODE_018 = "018";

	/** Json parsing error. */
	String MSG_CODE_019 = "019";

	/**
	 * System Error (for more information please contact with IT administrator).
	 */
	String MSG_CODE_020 = "020";

}
