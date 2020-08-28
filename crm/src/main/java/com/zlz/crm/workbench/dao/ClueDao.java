package com.zlz.crm.workbench.dao;


import com.zlz.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    List<Clue> selectClues(Map<String, Object> map);

    Integer selectCount(Map<String, Object> map);

    int insertClue(Clue clue);

    Clue selectClue(String id);

    List<String> selectActivityId(String id);

    int deleteClue(String clueId);
}
