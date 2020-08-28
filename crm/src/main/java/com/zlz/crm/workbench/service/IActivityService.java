package com.zlz.crm.workbench.service;

import com.zlz.crm.vo.PageListVo;
import com.zlz.crm.workbench.domain.Activity;
import com.zlz.crm.workbench.domain.ActivityRemark;
import com.zlz.crm.workbench.domain.ClueActivityRelation;

import java.util.List;
import java.util.Map;

public interface IActivityService {
    boolean saveActivity(Activity act);


    PageListVo<Activity> getPageListVo(Map<String, Object> map);

    boolean deleteActitity(String[] id);

    Activity getActivity(String id);

    Activity getActivityDetail(String id);

    boolean editActivity(Activity activity);

    List<ActivityRemark> getRemarks(String id);

    boolean removeRemark(String id);

    boolean editRemark(ActivityRemark remark);

    boolean addRemark(ActivityRemark remark);

    Map<String, Activity> getRelation(List<ClueActivityRelation> relations);

    List<Activity> getActivityForShow(String name,List<ClueActivityRelation> relation);

    List<Activity> getSameActivityForShow(String name, List<ClueActivityRelation> relation);

    List<Activity> getActivityByLike(String name);
}
