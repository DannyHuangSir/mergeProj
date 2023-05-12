package com.twfhclife.eservice_api.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Entry;
//import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.twfhclife.eservice_api.service.IAuthoService;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;
import com.twfhclife.keycloak.service.KeycloakService;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.controls.SubentriesRequestControl;

@RestController
public class LdapController extends BaseController {

	private static final Logger logger = LogManager.getLogger(LdapController.class);

	// 当前配置信息
	@Value("${ldaptest.host}")
	private static String ldapHost;
	@Value("${ldaptest.port}")
	private static int ldapPort;
	@Value("${ldaptest.bindDN}")
	private static String ldapBindDN;
	@Value("${ldaptest.password}")
	private static String ldapPassword;
	private static LDAPConnection conn = null;
	private static LdapConnection connection = null;

	@Autowired
	KeycloakService keycloakService;

	@Autowired
	IAuthoService authoService;

	/** 连接LDAP */
	public static String openConnection() {
		String msg = null;
		if (conn == null) {
			try {
				conn = new LDAPConnection(ldapHost, 389, ldapBindDN, ldapPassword);
			} catch (Exception e) {
				msg = "連接LDAP出現錯誤" + e.getMessage();
				System.out.println(msg);
			}
		}
		return msg;
	}

	/** 查询 */
	public SearchResult queryLdap(String searchDN, String filter) {
		try {
			// 查询企业所有用户
			SearchRequest searchRequest = new SearchRequest(searchDN, com.unboundid.ldap.sdk.SearchScope.SUB, "(" + filter + ")");
			searchRequest.addControl(new SubentriesRequestControl());
			SearchResult searchResult = conn.search(searchRequest);
			System.out.println(">>>共查询到" + searchResult.getSearchEntries().size() + "條紀錄");
			int index = 1;
			for (SearchResultEntry entry : searchResult.getSearchEntries()) {
				System.out.println((index++) + "\t" + entry.getDN());
			}
			return searchResult;

		} catch (Exception e) {
			System.out.println("查询错误，错误信息如下：\n" + e.getMessage());
			return null;
		}
	}

	public EntryCursor testSearchWithDn(String dn) throws Exception {

		Dn systemDn = new Dn(dn);

		EntryCursor cursor = null;

		cursor = connection.search(systemDn, "(objectclass=*)",
				org.apache.directory.api.ldap.model.message.SearchScope.SUBTREE);

		for (Entry entry : cursor) {

			System.out.println(entry);

		}
		cursor.close();

		return cursor;

	}

	@GetMapping(value = "/ldap/searches", produces = { "application/json" })
	public ResponseEntity<?> searcheLdap(@RequestParam String searchDN) {
		ApiResponseObj<EntryCursor> response = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();

		// 连接LDAP
		connection = new LdapNetworkConnection(ldapHost, 389);

		try {
			EntryCursor cursor = this.testSearchWithDn(searchDN);

			response.setReturnHeader(returnHeader);
			response.setResult(cursor);
		} catch (Exception e) {
			Map<String, Object> error = Collections.singletonMap("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	@GetMapping(value = "/ldap/searches2", produces = { "application/json" })
	public ResponseEntity<?> searcheLdap2(@RequestParam String searchDN, @RequestParam(value="filter", required = false) String filter) {
		ApiResponseObj<SearchResult> response = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();

		// 连接LDAP
		String msg = openConnection();

		if (msg == null) {
			try {
				 SearchResult searchResult = this.queryLdap(searchDN, filter);

				response.setReturnHeader(returnHeader);
				response.setResult(searchResult);
				connection.close();
			} catch (Exception e) {
				Map<String, Object> error = Collections.singletonMap("error", e.getMessage());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
			}
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			Map<String, Object> error = Collections.singletonMap("error", msg);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
		}

	}

}
