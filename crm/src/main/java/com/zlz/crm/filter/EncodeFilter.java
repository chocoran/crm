package com.zlz.crm.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class EncodeFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 将这里的request封装解决了乱码问题的HttpServletRequest对象RequestDecorator
//        request = new EncodeDecorator((HttpServletRequest) request);
        request.setCharacterEncoding("UTF-8");
        // 解决json串的乱码问题
        response.setContentType("text/json;charset=UTF-8");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
