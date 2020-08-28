package com.zlz.crm.filter;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class EncodeDecorator extends HttpServletRequestWrapper {

    public EncodeDecorator(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        return this.getParameterValues(name)[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = super.getParameterMap();
        Map<String, String[]> newMap = new HashMap<>();
        Set<String> keys = parameterMap.keySet();
        for (String key : keys) {
            String[] values = parameterMap.get(key);
            for (int i = 0; i < values.length; i++) {
                try {
                    values[i] = new String(values[i].getBytes("ISO8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    System.out.println("****************************************************");
                }
            }
            newMap.put(key, values);
        }
        return newMap;
    }

    @Override
    public String[] getParameterValues(String name) {
        return this.getParameterMap().get(name);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return ((Vector<String>) this.getParameterMap().keySet()).elements();
    }

}
