package com.twfhclife.keycloak.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakUserDao {
	
	UserRepresentation findByUserName(@Param("realm") String realm, @Param("username") String username);
	
	List<Map<String, String>> findByUserAttributes(@Param("userId") String userId);

}
