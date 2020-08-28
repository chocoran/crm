package com.zlz.crm.setting.service;

import com.zlz.crm.setting.domain.DicType;
import com.zlz.crm.setting.domain.DicValue;

import java.util.List;
import java.util.Map;

public interface IDicService {

    Map<String, List<DicValue>> getDics();

    List<DicType> getDicTypes();
}
