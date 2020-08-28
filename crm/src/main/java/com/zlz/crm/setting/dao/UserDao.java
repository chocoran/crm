package com.zlz.crm.setting.dao;

import com.zlz.crm.setting.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    User getUser(Map<String, String> data);

    List<User> getUsers();
}
