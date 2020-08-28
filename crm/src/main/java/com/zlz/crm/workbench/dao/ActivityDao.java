package com.zlz.crm.workbench.dao;

import com.zlz.crm.setting.domain.User;
import com.zlz.crm.vo.ACRelationVo;
import com.zlz.crm.workbench.domain.Activity;
import com.zlz.crm.workbench.domain.ClueActivityRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    Integer saveActivity(Activity act);

    List<Activity> getActivities(Map<String, Object> map);

    Integer getCount(Map<String, Object> map);

    Integer deleteActivity(String[] s);

    Activity getActivity(String id);

    Activity getActivityByUser(String id);

    Activity getActivityDetail(String id);

    Integer editActivity(Activity activity);

    List<Activity> getActivityByShow(ACRelationVo vo);

    List<Activity> getSameActivityByShow(ACRelationVo vo);

    List<Activity> selectActivityByLike(@Param("name") String name);
}
