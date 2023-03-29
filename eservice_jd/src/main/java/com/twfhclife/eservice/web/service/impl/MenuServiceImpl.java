package com.twfhclife.eservice.web.service.impl;

import com.twfhclife.eservice.api_client.FuncMgmtClient;
import com.twfhclife.eservice.util.ApConstants;
import com.twfhclife.eservice.util.MenuUtil;
import com.twfhclife.eservice.web.model.FunctionVo;
import com.twfhclife.eservice.web.service.IMenuService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuServiceImpl implements IMenuService {

    private static final Logger logger = LogManager.getLogger(MenuServiceImpl.class);

    @Autowired
    private FuncMgmtClient funcMgmtClient;

    @Override
    public List<FunctionVo> getMenuList(String userName, String keyCloakUserId) {

        // 透過API取得menu權限
        List<FunctionVo> menuList = funcMgmtClient.getMenuList(ApConstants.SYSTEM_ID, keyCloakUserId,
                "N");

        List<FunctionVo> userMenuList = new ArrayList<FunctionVo>();
        if (!CollectionUtils.isEmpty(menuList)) {
            userMenuList = MenuUtil.convertMenuTree(userName, menuList, ApConstants.SYSTEM_ID);
        } else {
            logger.info("Unable to get menu list for user: {}", userName);
        }
        return userMenuList;
    }
}
