package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.TblUser;
import com.bjpowernode.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    // 通过id获取用户对象
    public TblUser selectUserById(String id);

    // 通过login_act和pwd查询用户对象
    User queryByLoginActAndLoginPwd(Map<String, Object> map);
    // 查询全部
    List<User> queryAll();
}
