package com.zlz.crm.vo;

import com.zlz.crm.workbench.domain.ClueActivityRelation;

import java.util.List;
import java.util.Objects;

public class ACRelationVo {
    private String name;
    private List<ClueActivityRelation> relation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ClueActivityRelation> getRelation() {
        return relation;
    }

    public void setRelation(List<ClueActivityRelation> relation) {
        this.relation = relation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ACRelationVo that = (ACRelationVo) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(relation, that.relation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, relation);
    }

    @Override
    public String toString() {
        return "ACRelationVo{" +
                "name='" + name + '\'' +
                ", relation=" + relation +
                '}';
    }
}
