package com.zlz.crm.setting.service;

import com.zlz.crm.exception.LoginException;
import com.zlz.crm.setting.domain.User;

import java.util.List;

public interface IUserService {
    User loginCheck(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUsers();
}
