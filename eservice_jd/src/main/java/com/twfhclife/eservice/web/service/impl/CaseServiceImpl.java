package com.twfhclife.eservice.web.service.impl;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.jdzq.dao.JdzqDao;
import com.twfhclife.eservice.keycloak.model.KeycloakUser;
import com.twfhclife.eservice.web.dao.UsersDao;
import com.twfhclife.eservice.web.model.CaseVo;
import com.twfhclife.eservice.web.service.ICaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CaseServiceImpl implements ICaseService {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private JdzqDao caseDao;
    @Override
    public List<CaseVo> getCaseList(KeycloakUser user) {
        int isSupervisor = usersDao.checkUserRole(user.getId(), "分行主管");
        List<String> serialNums = Lists.newArrayList();
        if (isSupervisor > 0) {
            serialNums.addAll(usersDao.getSerialNumsBySupervisor(user.getId()));
        } else {
            int isIc = usersDao.checkUserRole(user.getId(), "IC人員");
            if (isIc > 0) {
                serialNums.addAll(usersDao.getSerialNumsByIc(user.getId()));
            } else {
                serialNums.addAll(usersDao.getSerialNumsByUser(user.getId()));
            }
        }
        if (!CollectionUtils.isEmpty(serialNums)) {
            return caseDao.getCaseList(serialNums);
        }

        return Lists.newArrayList();
    }
}
