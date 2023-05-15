package com.twfhclife.jd.web.service;

import com.twfhclife.jd.web.model.FunctionVo;

import java.util.List;

public interface IMenuService {

    List<FunctionVo> getMenuList(String userName, String keyCloakUserId);
}
