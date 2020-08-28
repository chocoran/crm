package com.zlz.crm.workbench.dao;

import com.zlz.crm.workbench.domain.ClueActivityRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClueActivityRelationDao {

    List<ClueActivityRelation> selectRelation(String id);

    int deleteRelation(String id);

    int deleteRelationForClueId(String clueId);

    int insertRelation(@Param("id") String id,@Param("clueId") String cId,@Param("activityId") String a);
}
