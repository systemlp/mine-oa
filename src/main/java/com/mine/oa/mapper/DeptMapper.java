package com.mine.oa.mapper;

import java.util.List;

import com.mine.oa.dto.DeptDto;
import com.mine.oa.dto.DeptQueryDto;
import com.mine.oa.entity.DepartmentPo;

/***
 *
 * 〈一句话功能简述〉<br> 
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface DeptMapper {

    List<DeptDto> findByParam(DeptQueryDto param);

    List<DepartmentPo> queryByParam(DepartmentPo param);

    int getNameCount(DepartmentPo param);

    int merge(DepartmentPo param);

    int updateState(DepartmentPo param);

    int insert(DepartmentPo param);
}
