package com.mine.oa.entity;

import javax.persistence.*;

/***
 *
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Table(name = "t_department")
public class DepartmentPO extends CommonPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer parentId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DepartmentPo{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", parentId=").append(parentId);
        sb.append(", createTime=").append(getCreateTime());
        sb.append(", createUserId=").append(getCreateUserId());
        sb.append(", updateTime=").append(getUpdateTime());
        sb.append(", updateUserId=").append(getUpdateUserId());
        sb.append(", state=").append(getState());
        sb.append('}');
        return sb.toString();
    }
}
