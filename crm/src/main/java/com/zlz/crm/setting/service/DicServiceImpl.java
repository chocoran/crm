package com.zlz.crm.setting.service;

import com.zlz.crm.setting.dao.DicTypeDao;
import com.zlz.crm.setting.dao.DicValueDao;
import com.zlz.crm.setting.domain.DicType;
import com.zlz.crm.setting.domain.DicValue;
import com.zlz.crm.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements IDicService {
    private DicTypeDao typeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao valueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);


    @Override
    public Map<String, List<DicValue>> getDics() {
        List<DicType> dicTypes = getDicTypes();
        Map<String,List<DicValue>> map = new HashMap<>();
        for (DicType type :dicTypes){
            String typeName = type.getCode();
            List<DicValue> values = valueDao.getValues(type.getCode());
            map.put(typeName,values);
        }
        return map;
    }

    @Override
    public List<DicType> getDicTypes() {
        return typeDao.getTypes();
    }


}
