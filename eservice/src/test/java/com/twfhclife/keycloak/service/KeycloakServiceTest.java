package com.twfhclife.keycloak.service;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.twfhclife.generic.api_client.SsoClient;
import com.twfhclife.generic.api_model.ApiResponseObj;
import com.twfhclife.generic.api_model.KeycloakLoginRequest;
import com.twfhclife.generic.api_model.KeycloakLoginResponse;
import com.twfhclife.keycloak.model.KeycloakUser;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KeycloakServiceTest {

	@Autowired
	private KeycloakService keycloakService;

	@Autowired
	private SsoClient ssoClient;

	@Test
	public void testLoginUseSsoClient() {
		KeycloakLoginRequest loginReq = new KeycloakLoginRequest();
		loginReq.setUsername("twhfclife0704");
		loginReq.setRealm("elife");
		loginReq.setPassword("hpe1234!@");
		loginReq.setClientId("eservice");
		loginReq.setGrantType("password");
		
		ApiResponseObj<KeycloakLoginResponse> apiResponseObj = ssoClient.login(loginReq);
		System.out.println(ToStringBuilder.reflectionToString(apiResponseObj.getResult(), 
				ToStringStyle.MULTI_LINE_STYLE));
		System.out.println(ToStringBuilder.reflectionToString(apiResponseObj.getReturnHeader(), 
				ToStringStyle.MULTI_LINE_STYLE));
	}

	@Test
	public void testLogin() {
		String username = "alan_test";
		String password = "1qaz2wsx";
		
		KeycloakUser keycloakUser = keycloakService.login(username, password);
		Assert.assertEquals(username, keycloakUser.getUsername());
	}

	@Test
	public void testCreateUser() {
		KeycloakUser kcUser = new KeycloakUser();
		kcUser.setUsername("alan_test_2");
		kcUser.setPassword("12qwaszx");
		kcUser.setRocId("A123456789");
		kcUser.setMobile("0912345678");
		kcUser.setTemporary(false);
		
		String kcUserId = "";
		try {
			kcUserId = keycloakService.createUser(kcUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertNotNull(kcUserId);
	}

	@Test
	public void testGetUser() {
		String userId = "8090eaa3-6acc-40af-ad76-ca909ba76dcf";
		
		KeycloakUser keycloakUser = keycloakService.getUser(userId);
		Assert.assertEquals("alan_test_2", keycloakUser.getUsername());
		Assert.assertEquals("A123456789", keycloakUser.getRocId());
		Assert.assertEquals("0912345678", keycloakUser.getMobile());
	}

	@Test
	public void testGetUserByEmail() {
		String email = "mailtest@gmail.com";
		KeycloakUser keycloakUser = keycloakService.getUserByEmail(email);
		Assert.assertEquals("9bf737be-ce29-4705-aaa4-420b9d919513", keycloakUser.getId());
	}

	@Test
	public void testGetUsers() {
		List<KeycloakUser> keycloakUsers = keycloakService.getUsers();
		keycloakUsers.forEach(kcUser -> {
			System.out.println(kcUser);
		});
	}

	@Test
	public void testUpdatePassword() {
		KeycloakUser kcUser = new KeycloakUser();
		kcUser.setUsername("alan_test");
		kcUser.setId("12421d40-3f1e-4aee-ba71-357d7182c2a1");

		String newPassord = "1qaz2wsx";
		keycloakService.updatePassword(kcUser, newPassord);
		
		// 測試用新密碼登入
		String username = "alan_test";
		KeycloakUser keycloakUser = keycloakService.login(username, newPassord);
		Assert.assertEquals(username, keycloakUser.getUsername());
	}

}
