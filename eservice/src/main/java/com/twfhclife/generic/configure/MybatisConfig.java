package com.twfhclife.generic.configure;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({ 
	"com.twfhclife.generic.dao", 
	"com.twfhclife.eservice.web.dao", 
	"com.twfhclife.eservice.auth.dao",
	"com.twfhclife.eservice.onlineChange.dao",
	"com.twfhclife.eservice.dashboard.dao",
	"com.twfhclife.eservice.policy.dao",
	"com.twfhclife.eservice.partner.dao",
	"com.twfhclife.eservice.user.dao",
	"com.twfhclife.keycloak.dao",
	"com.twfhclife.eservice.attitudemail.dao"
})
public class MybatisConfig {

}