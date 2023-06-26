package com.twfhclife.generic.model;


import com.google.common.collect.Lists;
import com.twfhclife.adm.model.UserDetailVo;

import java.io.Serializable;
import java.util.List;

public class UserDetailResponse implements Serializable {

    private List<UserDetailVo> UserDetailVos = Lists.newArrayList();

    public List<UserDetailVo> getUserDetailVos() {
        return UserDetailVos;
    }

    public void setUserDetailVos(List<UserDetailVo> userDetailVos) {
        UserDetailVos = userDetailVos;
    }
}
