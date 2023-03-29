package com.twfhclife.eservice.web.service;

import com.twfhclife.eservice.web.model.FunctionVo;

import java.util.List;

public interface IMenuService {

    List<FunctionVo> getMenuList(String userName, String keyCloakUserId);
}
