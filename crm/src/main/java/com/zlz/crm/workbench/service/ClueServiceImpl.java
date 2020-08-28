package com.zlz.crm.workbench.service;

import com.zlz.crm.util.SqlSessionUtil;
import com.zlz.crm.util.UUIDUtil;
import com.zlz.crm.vo.PageListVo;
import com.zlz.crm.workbench.dao.ClueActivityRelationDao;
import com.zlz.crm.workbench.dao.ClueDao;
import com.zlz.crm.workbench.dao.ClueRemarkDao;
import com.zlz.crm.workbench.domain.Clue;
import com.zlz.crm.workbench.domain.ClueActivityRelation;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements IClueService {

    private ClueDao dao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao relationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao remarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    @Override
    public boolean saveClue(Clue clue) {
        boolean flag = true;
        int count = dao.insertClue(clue);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Clue getClue(String id) {
        return dao.selectClue(id);
    }

    @Override
    public PageListVo<Clue> getPageListVo(Map<String, Object> map) {
        PageListVo<Clue> vo = new PageListVo<>();
        List<Clue> clues = dao.selectClues(map);
        int count = dao.selectCount(map);
        vo.setPageList(clues);
        vo.setTotal(count);
        return vo;
    }

    @Override
    public List<ClueActivityRelation> getRelation(String id) {
        List<ClueActivityRelation> relations = relationDao.selectRelation(id);
        return relations;
    }

    @Override
    public boolean removeRelation(String id) {
        boolean flag = true;
        int count = relationDao.deleteRelation(id);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean saveRelation(String[] aId, String cId) {
        boolean flag = true;
        int count = 0;
        for (String a :aId){
            String id = UUIDUtil.getUUID();
            count += relationDao.insertRelation(id,cId,a);
        }
        if (count != aId.length){
            flag = false;
        }
        return flag;
    }

    @Override
    public int removeClue(String clueId) {
        relationDao.deleteRelationForClueId(clueId);
        remarkDao.deleteRemark(clueId);
        return dao.deleteClue(clueId);
    }


}
