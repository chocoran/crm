package com.zlz.crm.setting.dao;

import com.zlz.crm.setting.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getValues(String code);
}
