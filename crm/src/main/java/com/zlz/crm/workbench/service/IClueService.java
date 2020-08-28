package com.zlz.crm.workbench.service;

import com.zlz.crm.vo.PageListVo;
import com.zlz.crm.workbench.domain.Clue;
import com.zlz.crm.workbench.domain.ClueActivityRelation;

import java.util.List;
import java.util.Map;

public interface IClueService {

    boolean saveClue(Clue clue);

    Clue getClue(String id);

    PageListVo<Clue> getPageListVo(Map<String, Object> map);

    List<ClueActivityRelation> getRelation(String id);

    boolean removeRelation(String id);

    boolean saveRelation(String[] aId, String cId);

    int removeClue(String clueId);
}
