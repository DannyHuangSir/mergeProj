package com.twfhclife.generic.configure;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/***
 * MyBatis configuration.
 * 
 * @author All
 * @version 1.0
 */
@Configuration
@MapperScan({ "com.twfhclife.adm.dao", "com.twfhclife.keycloak.dao" })
public class MybatisConfig {

}