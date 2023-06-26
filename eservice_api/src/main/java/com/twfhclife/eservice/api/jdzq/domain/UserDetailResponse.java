package com.twfhclife.eservice.api.jdzq.domain;

import com.twfhclife.eservice.api.jdzq.model.UserDetailVo;

import java.io.Serializable;
import java.util.List;

public class UserDetailResponse implements Serializable {

    private List<UserDetailVo> UserDetailVos;

    public List<UserDetailVo> getUserDetailVos() {
        return UserDetailVos;
    }

    public void setUserDetailVos(List<UserDetailVo> userDetailVos) {
        UserDetailVos = userDetailVos;
    }
}
