package com.zlz.crm.listener;

import com.zlz.crm.setting.domain.DicValue;
import com.zlz.crm.setting.service.DicServiceImpl;
import com.zlz.crm.setting.service.IDicService;
import com.zlz.crm.util.ServiceFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DicListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {

        System.out.println("执行数据字典监听器");

        IDicService service = (IDicService) ServiceFactory.getService(new DicServiceImpl());

        Map<String, List<DicValue>> map = service.getDics();

        Set<String> typeCodes = map.keySet();
        for (String typeCode : typeCodes){
            event.getServletContext().setAttribute(typeCode,map.get(typeCode));
        }

        System.out.println("关闭数据字典监听器");

    }
}
