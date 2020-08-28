package com.zlz.crm.setting.controller;

import com.zlz.crm.setting.domain.User;
import com.zlz.crm.setting.service.IUserService;
import com.zlz.crm.setting.service.UserServiceImpl;
import com.zlz.crm.util.MD5Util;
import com.zlz.crm.util.PrintJson;
import com.zlz.crm.util.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getServletPath();
        // 控制器模型，根据提交的请求判断执行的Servlet
        if ("/setting/user/login.do".equals(url)) {
            login(request, response);
        } else if ("/setting/user/save.do".equals(url)) {
            //save(request,response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        // 获取请求参数
        String loginAct = request.getParameter("loginAct");
        String loginPwd = MD5Util.getMD5(request.getParameter("loginPwd"));
        String ip = request.getRemoteAddr();
        // 获取事务开启的service对象
        IUserService service = (IUserService) ServiceFactory.getService(new UserServiceImpl());
        try {
            // 当账户密码与数据库匹配上，返回一个user
            // 如果匹配不上，返回null，出现异常，直接进入catch语句块，不执行下面的语句
            User user = service.loginCheck(loginAct, loginPwd, ip);
            // 将返回的user对象放到Session域中
            request.getSession().setAttribute("user", user);
            // 将本次执行的结果封装成一个json串，发给前端 {success:true}
            PrintJson.printJsonFlag(response, true);
        } catch (Exception e) {
            e.printStackTrace();
            // 返回null代表账号密码错误，登录失败
            // 同时将后台的错误信息通过json串发给前端 {success:false，msg:"..."}
            // 通过map传值
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("msg", e.getMessage());
            PrintJson.printJsonObj(response, map);
        }
    }
}
