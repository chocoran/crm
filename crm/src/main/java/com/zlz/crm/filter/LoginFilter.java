package com.zlz.crm.filter;

import com.zlz.crm.setting.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 通过父对象获取HttpServletRequest和HttpServletResponse子对象
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 获取当前项目路径
        String path = request.getServletPath();
        // 判断是否为登录页面和登录控制器，这两个不通过过滤器
        if ("/login.jsp".equals(path) || "/setting/user/login.do".equals(path)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            // 获取到Session域中的user属性
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            // user不为空代表之前有登陆过，可以直接访问登陆后的页面
            if (user != null) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                // user为空，不能直接访问登陆后的页面，重定向到登录页面
                // 只能是重定向，需要从当前地址栏地址跳转到登录页面
                // 请求转发做不到
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }
    }

    @Override
    public void destroy() {

    }
}
