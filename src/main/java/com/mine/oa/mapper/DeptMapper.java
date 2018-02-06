package com.mine.oa.mapper;

import java.util.List;

import com.mine.oa.dto.DeptDto;
import com.mine.oa.dto.DeptQueryDto;
import com.mine.oa.entity.DepartmentPO;
import com.mine.oa.util.BaseMapper;

/***
 *
 * 〈一句话功能简述〉<br> 
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface DeptMapper extends BaseMapper<DepartmentPO> {

    List<DeptDto> findByParam(DeptQueryDto param);

    List<DepartmentPO> queryByParam(DepartmentPO param);

    int getNameCount(DepartmentPO param);

    int merge(DepartmentPO param);

    int updateState(DepartmentPO param);

    int insert(DepartmentPO param);
}
