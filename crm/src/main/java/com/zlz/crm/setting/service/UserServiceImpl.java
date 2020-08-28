package com.zlz.crm.setting.service;

import com.zlz.crm.exception.LoginException;
import com.zlz.crm.setting.dao.UserDao;
import com.zlz.crm.setting.domain.User;
import com.zlz.crm.util.DateTimeUtil;
import com.zlz.crm.util.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements IUserService {

    private UserDao dao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User loginCheck(String loginAct, String loginPwd, String ip) throws LoginException {
        // map传值账户和密码
        Map<String, String> data = new HashMap<>();
        data.put("loginAct", loginAct);
        data.put("loginPwd", loginPwd);
        // 返回结果
        User user = dao.getUser(data);
        // 为空抛出错误信息
        if (user == null) {
            throw new LoginException("用户名或密码错误");
        }
        // 不为空，代表账户密码匹配上，继续进行验证
        // 抛出账户锁定错误信息、IP地址受限信息、账户超时错误信息
        if ("0".equals(user.getLockState())) {
            throw new LoginException("该用户已被锁住");
        } else if (!user.getAllowIps().contains(ip)) {
            throw new LoginException("外部IP不允许登录");
        } else if (DateTimeUtil.getSysTime().compareTo(user.getExpireTime()) > 0) {
            throw new LoginException("该账户已超时，请联系管理员");
        }
        return user;
    }

    @Override
    public List<User> getUsers() {
        return dao.getUsers();
    }
}
