package com.twfhclife.eservice.configure;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({
        "com.twfhclife.eservice.web.dao"
})
public class MybatisConfig {

}