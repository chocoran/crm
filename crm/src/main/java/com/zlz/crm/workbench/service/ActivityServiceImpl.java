package com.zlz.crm.workbench.service;

import com.zlz.crm.vo.ACRelationVo;
import com.zlz.crm.vo.PageListVo;
import com.zlz.crm.util.SqlSessionUtil;
import com.zlz.crm.workbench.dao.ActivityDao;
import com.zlz.crm.workbench.dao.ActivityRemarkDao;
import com.zlz.crm.workbench.domain.Activity;
import com.zlz.crm.workbench.domain.ActivityRemark;
import com.zlz.crm.workbench.domain.ClueActivityRelation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements IActivityService {
    private ActivityDao dao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao dao1 = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

    @Override
    public boolean saveActivity(Activity act) {
        boolean flag = true;
        int count = dao.saveActivity(act);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public PageListVo<Activity> getPageListVo(Map<String, Object> map) {
        Integer count = dao.getCount(map);
        List<Activity> pageList = dao.getActivities(map);
        PageListVo vo = new PageListVo();
        vo.setTotal(count);
        vo.setPageList(pageList);
        return vo;
    }

    @Override
    public boolean deleteActitity(String[] id) {
        boolean flag = true;
        int count1 = dao1.getRemarks(id);
        int count2 = dao1.deleteRemarks(id);
        if (count1 != count2) {
            flag = false;
        }

        int count3 = dao.deleteActivity(id);
        if (count3 != id.length) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Activity getActivity(String id) {
        return dao.getActivity(id);
    }

    @Override
    public Activity getActivityDetail(String id) {
        return dao.getActivityDetail(id);
    }

    @Override
    public boolean editActivity(Activity activity) {
        boolean flag = true;
        int count = dao.editActivity(activity);
        System.out.println(count);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public List<ActivityRemark> getRemarks(String id) {
        return dao1.getIdRemarks(id);
    }

    @Override
    public boolean removeRemark(String id) {
        boolean flag = true;
        int count = dao1.deleteRemark(id);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean editRemark(ActivityRemark remark) {
        boolean flag = true;
        int count = dao1.updateRemark(remark);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean addRemark(ActivityRemark remark) {
        boolean flag = true;
        int count = dao1.insertRemark(remark);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Activity> getRelation(List<ClueActivityRelation> relations) {
        Map<String, Activity> map = new HashMap<>();
        for (ClueActivityRelation relation : relations){
            String activityId = relation.getActivityId();
            Activity activity = dao.getActivityByUser(activityId);
            map.put(relation.getId(),activity);
        }
        return map;
    }

    @Override
    public List<Activity> getActivityForShow(String name, List<ClueActivityRelation> relation) {
//        List<String> activityIds = new ArrayList<>();
//        for (ClueActivityRelation re : relation){
//            activityIds.add(re.getActivityId());
//        }
        ACRelationVo vo = new ACRelationVo();
        vo.setName(name);
        vo.setRelation(relation);
        return dao.getActivityByShow(vo);
    }

    @Override
    public List<Activity> getSameActivityForShow(String name, List<ClueActivityRelation> relation) {
        ACRelationVo vo = new ACRelationVo();
        vo.setName(name);
        vo.setRelation(relation);
        return dao.getSameActivityByShow(vo);
    }

    @Override
    public List<Activity> getActivityByLike(String name) {
        return dao.selectActivityByLike(name);
    }


}
